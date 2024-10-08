import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form } from 'react-bootstrap';
import { fetchProducts, fetchCategories, addProduct, editProduct, deleteProduct } from './mockApi';
import { storage } from '../contexts/firebaseConfig';
import { ref, uploadBytes, getDownloadURL } from "firebase/storage";
import axios from 'axios';

const ProductManagement = () => {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [sortConfig, setSortConfig] = useState({ key: 'id', direction: 'ascending' });
  const [imageFile, setImageFile] = useState(null);
  const [selectedProductStatus, setSelectedProductStatus] = useState('');
  const [selectedCreatedDate, setSelectedCreatedDate] = useState('');
  const [newProduct, setNewProduct] = useState({
    id: 0,
    name: '',
    price: 0,
    category: '',
    status: 'false',
    stock: 0,
    imageUrl: '',
    description: '',
    createdDate: ''
  });

  useEffect(() => {
    const loadData = async () => {
      await fetchFilteredProductsData('', '', '');
      await fetchCategoriesData();
    };
    loadData();
  }, []);

  const fetchFilteredProductsData = async (category, productStatus, startDate) => {
    try {
      const authDataString = localStorage.getItem('auth');
      const authData = authDataString ? JSON.parse(authDataString) : null;
      if (!authData || !authData.token) {
        console.error('Token not found');
        return;
      }

      const response = await axios.get('/api/Product/products-by-status', {
        params: { category, productStatus, startDate },
        headers: {
          'Authorization': `Bearer ${authData.token}`,
          'Content-Type': 'application/json',
        }
      });

      const data = response.data;
      setProducts(data);
      setFilteredProducts(data);
    } catch (error) {
      console.error("Error fetching filtered products: ", error);
    }
  };

  const fetchCategoriesData = async () => {
    try {
      const categoryResponse = await fetchCategories();
      setCategories(categoryResponse || []);
    } catch (error) {
      console.error("Error fetching categories: ", error);
    }
  };

  const handleProductStatusChange = (e) => {
    setSelectedProductStatus(e.target.value);
    applyFilters(selectedCategory, e.target.value, selectedCreatedDate);
  };

  const handleCategoryChange = (e) => {
    const category = e.target.value;
    setSelectedCategory(category);
    applyFilters(category, selectedProductStatus, selectedCreatedDate);
  };

  const handleCreatedDateChange = (e) => {
    setSelectedCreatedDate(e.target.value);
    applyFilters(selectedCategory, selectedProductStatus, e.target.value);
  };

  const applyFilters = (category, productStatus, startDate) => {
    fetchFilteredProductsData(category, productStatus, startDate);
  };

  const handleCreateProduct = () => {
    const productToAdd = {
      productName: newProduct.name,
      price: newProduct.price,
      availableQuantity: newProduct.stock,
      category: newProduct.category,
      description: newProduct.description,
      createdDate: newProduct.createdDate,
      image: newProduct.imageUrl
    };

    if (imageFile) {
      const storageRef = ref(storage, `images/${imageFile.name}`);
      uploadBytes(storageRef, imageFile).then(() => {
        getDownloadURL(storageRef).then((url) => {
          productToAdd.image = url;
          sendProductToApi(productToAdd);
        });
      });
    } else {
      sendProductToApi(productToAdd);
    }
  };

  const sendProductToApi = async (productData) => {
    try {
      const authDataString = localStorage.getItem('auth');
      const authData = authDataString ? JSON.parse(authDataString) : null;
      if (!authData || !authData.token) {
        console.error('Token not found');
        return;
      }

      const response = await axios.post('/api/Product/create', productData, {
        headers: {
          'Authorization': `Bearer ${authData.token}`,
          'Content-Type': 'application/json',
        }
      });

      const addedProduct = response.data;
      setProducts([...products, addedProduct]);
      setFilteredProducts([...filteredProducts, addedProduct]);
      resetForm();
    } catch (error) {
      console.error("Error adding product: ", error);
    }
  };

  const resetForm = () => {
    setShowModal(false);
    setNewProduct({ id: 0, name: '', price: 0, category: '', status: 'false', stock: 0, imageUrl: '', description: '', createdDate: '' });
    setImageFile(null);
  };

  const handleEditProduct = (product) => {
    setNewProduct(product);
    setShowModal(true);
  };

  const handleDeleteProduct = async (id) => {

    try {
      const authDataString = localStorage.getItem('auth');
      const authData = authDataString ? JSON.parse(authDataString) : null;
      if (!authData || !authData.token) {
        console.error('Token not found');
        return;
      }

      // Make the API call to delete the product
      await axios.delete(`/api/Product/delete/${id}`, {

        headers: {
          'Authorization': `Bearer ${authData.token}`,
          'Content-Type': 'application/json',
        }

      });

      // Remove the product from the state after successful deletion
      setProducts(products.filter(product => product.id !== id));
      setFilteredProducts(filteredProducts.filter(product => product.id !== id));
    } catch (error) {
      console.error("Error deleting product: ", error);
    }
  };

  const toggleProductStatus = async (id) => {
    console.log('toggleProductStatus', id);
    try {
      const productToEdit = products.find(product => product.id === id);
      const newStatus = productToEdit.status === 'Active' ? false : true;
  
      const authDataString = localStorage.getItem('auth');
      const authData = authDataString ? JSON.parse(authDataString) : null;
      if (!authData || !authData.token) {
        console.error('Token not found');
        return;
      }

      console.log('newStatus', newStatus);  
  
      // Make API call to update the product status
      await axios.put(`/api/Product/product-status/${id}`, newStatus, {
        headers: {
          'Authorization': `Bearer ${authData.token}`,
          'Content-Type': 'application/json',
        }
      });
      
  
      // Update the product status locally after a successful API call
      const updatedProduct = { ...productToEdit, status: newStatus ? 'Active' : 'Inactive' };
      setProducts(products.map(product => product.id === id ? updatedProduct : product));
      setFilteredProducts(filteredProducts.map(product => product.id === id ? updatedProduct : product));
      
    } catch (error) {
      console.error("Error updating product status: ", error);
    }
  };
  
  
  

  const requestSort = (key) => {
    let direction = 'ascending';
    if (sortConfig.key === key && sortConfig.direction === 'ascending') {
      direction = 'descending';
    }
    setSortConfig({ key, direction });

    const sortedProducts = [...filteredProducts].sort((a, b) => {
      if (a[key] < b[key]) return direction === 'ascending' ? -1 : 1;
      if (a[key] > b[key]) return direction === 'ascending' ? 1 : -1;
      return 0;
    });
    setFilteredProducts(sortedProducts);
  };

  return (
    <div>
      <h2>Product Management</h2>
      <Button onClick={() => setShowModal(true)}>Add Product</Button>

      <div className="row mb-3">
        <div className="col-md-4">
          <Form.Group controlId="categorySelect">
            <Form.Label>Select Category</Form.Label>
            <Form.Control as="select" value={selectedCategory} onChange={handleCategoryChange}>
              <option value="">All Categories</option>
              {categories.map((category) => (
                <option key={category.id} value={category.name}>
                  {category.name}
                </option>
              ))}
            </Form.Control>
          </Form.Group>
        </div>

        <div className="col-md-4">
          <Form.Group controlId="ProductStatusSelect">
            <Form.Label>Product Status</Form.Label>
            <Form.Control as="select" value={selectedProductStatus} onChange={handleProductStatusChange}>
              <option value="">All</option>
              <option value="true">True</option>
              <option value="false">False</option>
            </Form.Control>
          </Form.Group>
        </div>

        <div className="col-md-4">
          <Form.Group controlId="CreatedDateSelect">
            <Form.Label>Created Date</Form.Label>
            <Form.Control
              type="date"
              value={selectedCreatedDate}
              onChange={handleCreatedDateChange}
            />
          </Form.Group>
        </div>
      </div>


      <Table striped bordered hover className="mt-3">
        <thead>
          <tr>
            <th onClick={() => requestSort('id')} style={{ cursor: 'pointer' }}>ID</th>
            <th onClick={() => requestSort('name')} style={{ cursor: 'pointer' }}>Name</th>
            <th onClick={() => requestSort('category')} style={{ cursor: 'pointer' }}>Category</th>
            <th onClick={() => requestSort('price')} style={{ cursor: 'pointer' }}>Price</th>
            <th onClick={() => requestSort('stock')} style={{ cursor: 'pointer' }}>Stock</th>
            <th onClick={() => requestSort('status')} style={{ cursor: 'pointer' }}>Status</th>
            <th>Image</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredProducts.map((product) => (
            <tr key={product.id}>
              <td>{product.vendorEmail}</td>
              <td>{product.productName}</td>
              <td>{product.category}</td>
              <td>{product.price}</td>
              <td>{product.availableQuantity}</td>
              <td>{product.productStatus ? 'Active' : 'Inactive'}</td>
              <td>
                {product.image && (
                  <img src={product.image} alt={product.productName} style={{ width: '50px', height: '50px', objectFit: 'cover' }} />
                )}
              </td>
              <td>
                <Button variant="warning" onClick={() => handleEditProduct(product)}>Edit</Button>{' '}
                <Button variant="danger" onClick={() => handleDeleteProduct(product.id)}>Delete</Button>{' '}
                <Button
                  variant={product.productStatus ? "info" : "success"}
                  onClick={() => toggleProductStatus(product.id)}
                >
                  {product.productStatus ? 'Deactivate' : 'Activate'}
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{newProduct.id ? 'Edit Product' : 'Add Product'}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group>
              <Form.Label>Product Name</Form.Label>
              <Form.Control
                type="text"
                value={newProduct.name}
                onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
              />
            </Form.Group>

            <Form.Group>
              <Form.Label>Category</Form.Label>
              <Form.Control
                as="select"
                value={newProduct.category}
                onChange={(e) => setNewProduct({ ...newProduct, category: e.target.value })}
              >
                <option value="">Select Category</option>
                {categories.map((category) => (
                  <option key={category.id} value={category.name}>
                    {category.name}
                  </option>
                ))}
              </Form.Control>
            </Form.Group>

            <Form.Group>
              <Form.Label>Price</Form.Label>
              <Form.Control
                type="number"
                value={newProduct.price}
                onChange={(e) => setNewProduct({ ...newProduct, price: parseFloat(e.target.value) })}
              />
            </Form.Group>

            <Form.Group>
              <Form.Label>Stock</Form.Label>
              <Form.Control
                type="number"
                value={newProduct.stock}
                onChange={(e) => setNewProduct({ ...newProduct, stock: parseInt(e.target.value, 10) })}
              />
            </Form.Group>

            <Form.Group>
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                value={newProduct.description}
                onChange={(e) => setNewProduct({ ...newProduct, description: e.target.value })}
              />
            </Form.Group>

            <Form.Group>
              <Form.Label>Created Date</Form.Label>
              <Form.Control
                type="date"
                value={newProduct.createdDate}
                onChange={(e) => setNewProduct({ ...newProduct, createdDate: e.target.value })}
              />
            </Form.Group>

            <Form.Group>
              <Form.Label>Upload Image</Form.Label>
              <Form.Control type="file" onChange={(e) => setImageFile(e.target.files[0])} />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>Close</Button>
          <Button variant="primary" onClick={handleCreateProduct}>Save Changes</Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default ProductManagement;
