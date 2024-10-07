import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form } from 'react-bootstrap';
import { fetchProducts, fetchCategories, addProduct, editProduct, deleteProduct } from './mockApi';
import { storage } from '../contexts/firebaseConfig'; // Import your Firebase storage configuration
import { ref, uploadBytes, getDownloadURL } from "firebase/storage";

const ProductManagement = () => {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [categories, setCategories] = useState([]); // Ensure categories is initialized as an empty array
  const [showModal, setShowModal] = useState(false);
  const [newProduct, setNewProduct] = useState({ id: 0, name: '', price: 0, category: '', status: 'Active', stock: 0, imageUrl: '' });
  const [selectedCategory, setSelectedCategory] = useState('');
  const [sortConfig, setSortConfig] = useState({ key: 'id', direction: 'ascending' });
  const [imageFile, setImageFile] = useState(null);

  useEffect(() => {
    const loadData = async () => {
      await fetchProductsData();
      await fetchCategoriesData();
    };

    loadData();
  }, []);

  const fetchProductsData = async () => {
    try {
      const productResponse = await fetchProducts();
      setProducts(productResponse.data);
      setFilteredProducts(productResponse.data); // Initialize filteredProducts with all products
    } catch (error) {
      console.error("Error fetching products: ", error);
    }
  };

  const fetchCategoriesData = async () => {
    try {
      const categoryResponse = await fetchCategories();
      setCategories(categoryResponse || []); // Ensure categories is set to an empty array if undefined
    } catch (error) {
      console.error("Error fetching categories: ", error);
    }
  };

  const handleCreateProduct = () => {
    const productToAdd = { ...newProduct };

    // Upload image and then add product
    if (imageFile) {
      const storageRef = ref(storage, `images/${imageFile.name}`);
      uploadBytes(storageRef, imageFile).then(() => {
        getDownloadURL(storageRef).then((url) => {
          productToAdd.imageUrl = url; // Set imageUrl to the uploaded file URL
          addProduct(productToAdd).then((addedProduct) => {
            setProducts([...products, addedProduct]);
            setFilteredProducts([...filteredProducts, addedProduct]);
            resetForm();
          });
        });
      });
    } else {
      // If no image is uploaded, proceed without it
      addProduct(productToAdd).then((addedProduct) => {
        setProducts([...products, addedProduct]);
        setFilteredProducts([...filteredProducts, addedProduct]);
        resetForm();
      });
    }
  };

  const resetForm = () => {
    setShowModal(false);
    setNewProduct({ id: 0, name: '', price: 0, category: '', status: 'Active', stock: 0, imageUrl: '' });
    setImageFile(null);
  };

  const handleEditProduct = (product) => {
    setNewProduct(product);
    setShowModal(true);
  };

  const handleDeleteProduct = (id) => {
    deleteProduct(id).then(() => {
      setProducts(products.filter(product => product.id !== id));
      setFilteredProducts(filteredProducts.filter(product => product.id !== id)); // Update filteredProducts as well
    });
  };

  const toggleProductStatus = (id) => {
    const productToEdit = products.find(product => product.id === id);
    const updatedProduct = { ...productToEdit, status: productToEdit.status === 'Active' ? 'Inactive' : 'Active' };
    editProduct(updatedProduct).then(() => {
      setProducts(products.map(product => product.id === id ? updatedProduct : product));
      setFilteredProducts(filteredProducts.map(product => product.id === id ? updatedProduct : product)); // Update filteredProducts as well
    });
  };

  const handleCategoryChange = (e) => {
    const category = e.target.value;
    setSelectedCategory(category);

    if (category) {
      const filtered = products.filter(product => product.category === category);
      setFilteredProducts(filtered);
    } else {
      setFilteredProducts(products); // Reset to all products if no category is selected
    }
  };

  const requestSort = (key) => {
    let direction = 'ascending';
    if (sortConfig.key === key && sortConfig.direction === 'ascending') {
      direction = 'descending';
    }
    setSortConfig({ key, direction });

    const sortedProducts = [...filteredProducts].sort((a, b) => {
      if (a[key] < b[key]) {
        return direction === 'ascending' ? -1 : 1;
      }
      if (a[key] > b[key]) {
        return direction === 'ascending' ? 1 : -1;
      }
      return 0;
    });
    setFilteredProducts(sortedProducts);
  };

  return (
    <div>
      <h2>Product Management</h2>
      <Button onClick={() => setShowModal(true)}>Add Product</Button>

      <Form.Group controlId="categorySelect" className="mt-3">
        <Form.Label>Select Category</Form.Label>
        <Form.Control as="select" value={selectedCategory} onChange={handleCategoryChange}>
          <option value="">All Categories</option>
          {Array.isArray(categories) && categories.map((category) => ( // Check if categories is an array
            <option key={category.id} value={category.name}>
              {category.name}
            </option>
          ))}
        </Form.Control>
      </Form.Group>

      <Table striped bordered hover className="mt-3">
        <thead>
          <tr>
            <th onClick={() => requestSort('id')} style={{ cursor: 'pointer' }}>
              ID
            </th>
            <th onClick={() => requestSort('name')} style={{ cursor: 'pointer' }}>
              Name
            </th>
            <th onClick={() => requestSort('category')} style={{ cursor: 'pointer' }}>
              Category
            </th>
            <th onClick={() => requestSort('price')} style={{ cursor: 'pointer' }}>
              Price
            </th>
            <th onClick={() => requestSort('stock')} style={{ cursor: 'pointer' }}>
              Stock
            </th>
            <th onClick={() => requestSort('status')} style={{ cursor: 'pointer' }}>
              Status
            </th>
            <th>Image</th> {/* New column for Image */}
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredProducts.map((product) => (
            <tr key={product.id}>
              <td>{product.id}</td>
              <td>{product.name}</td>
              <td>{product.category}</td>
              <td>{product.price}</td>
              <td>{product.stock}</td>
              <td>{product.status}</td>
              <td>
                {product.imageUrl && (
                  <img src={product.imageUrl} alt={product.name} style={{ width: '50px', height: '50px', objectFit: 'cover' }} />
                )}
              </td>
              <td>
                <Button variant="warning" onClick={() => handleEditProduct(product)}>
                  <i className="fas fa-edit"></i>
                </Button>
                <Button variant="danger" onClick={() => handleDeleteProduct(product.id)}>
                  <i className="fas fa-trash"></i>
                </Button>
                <Button 
                  variant={product.status === 'Active' ? 'secondary' : 'success'} 
                  onClick={() => toggleProductStatus(product.id)}
                >
                  {product.status === 'Active' ? <i className="fas fa-toggle-off"></i> : <i className="fas fa-toggle-on"></i>}
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Add/Edit Product Modal */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{newProduct.id ? 'Edit Product' : 'Add New Product'}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="productName">
              <Form.Label>Product Name</Form.Label>
              <Form.Control
                type="text"
                value={newProduct.name}
                onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
              />
            </Form.Group>
            <Form.Group controlId="productPrice">
              <Form.Label>Price</Form.Label>
              <Form.Control
                type="number"
                value={newProduct.price}
                onChange={(e) => setNewProduct({ ...newProduct, price: parseFloat(e.target.value) })}
              />
            </Form.Group>
            <Form.Group controlId="productCategory">
              <Form.Label>Category</Form.Label>
              <Form.Control
                as="select"
                value={newProduct.category}
                onChange={(e) => setNewProduct({ ...newProduct, category: e.target.value })}
              >
                {Array.isArray(categories) && categories.map((category) => ( // Check if categories is an array
                  <option key={category.id} value={category.name}>
                    {category.name}
                  </option>
                ))}
              </Form.Control>
            </Form.Group>
            <Form.Group controlId="productStock">
              <Form.Label>Stock</Form.Label>
              <Form.Control
                type="number"
                value={newProduct.stock}
                onChange={(e) => setNewProduct({ ...newProduct, stock: parseInt(e.target.value, 10) })}
              />
            </Form.Group>
            <Form.Group controlId="productStatus">
              <Form.Label>Status</Form.Label>
              <Form.Control
                as="select"
                value={newProduct.status}
                onChange={(e) => setNewProduct({ ...newProduct, status: e.target.value })}
              >
                <option value="Active">Active</option>
                <option value="Inactive">Inactive</option>
              </Form.Control>
            </Form.Group>
            <Form.Group controlId="productImage">
              <Form.Label>Image Upload</Form.Label>
              <Form.Control
                type="file"
                onChange={(e) => setImageFile(e.target.files[0])}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Close
          </Button>
          <Button variant="primary" onClick={handleCreateProduct}>
            {newProduct.id ? 'Save Changes' : 'Add Product'}
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default ProductManagement;
