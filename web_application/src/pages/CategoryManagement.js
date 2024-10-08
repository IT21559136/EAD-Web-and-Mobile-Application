import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form } from 'react-bootstrap';
import { fetchCategories, addCategory, editCategory, deleteCategory } from '../apiControllers/categoryApi';

const CategoryManagement = () => {
  const [categories, setCategories] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [newCategory, setNewCategory] = useState({ id: '', name: '', status: 'Active' });

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    const data = await fetchCategories();
    setCategories(data);
  };

  const handleCreateOrEditCategory = async () => {
    if (newCategory.id) {
      // If id exists, it means we are editing
      const updatedCategory = await editCategory(newCategory);
      setCategories(categories.map(category => category.id === updatedCategory.id ? updatedCategory : category));
    } else {
      // Otherwise, we are adding a new category
      const addedCategory = await addCategory(newCategory);
      setCategories([...categories, addedCategory]);
    }
    setShowModal(false);
    resetNewCategory();
  };

  const handleEditCategory = (category) => {
    setNewCategory(category);
    setShowModal(true);
  };

  const handleDeleteCategory = async (id) => {
    await deleteCategory(id);
    setCategories(categories.filter(category => category.id !== id));
  };

  const toggleCategoryStatus = async (id) => {
    const categoryToEdit = categories.find(category => category.id === id);
    const updatedCategory = { ...categoryToEdit, status: categoryToEdit.status === 'Active' ? 'Inactive' : 'Active' };
    await editCategory(updatedCategory);
    setCategories(categories.map(category => category.id === id ? updatedCategory : category));
  };

  const resetNewCategory = () => {
    setNewCategory({ id: '', name: '', status: 'Active' });
  };

  return (
    <div>
      <h2>Category Management</h2>
      <Button onClick={() => { resetNewCategory(); setShowModal(true); }}>Add Category</Button>
      <Table striped bordered hover className="mt-3">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {categories.map((category) => (
            <tr key={category.id}>
              <td>{category.id}</td>
              <td>{category.name}</td>
              <td>{category.status}</td>
              <td>
                <Button variant="warning" onClick={() => handleEditCategory(category)}>
                  <i className="fas fa-edit"></i>
                </Button>
                <Button variant="danger" onClick={() => handleDeleteCategory(category.id)}>
                  <i className="fas fa-trash"></i>
                </Button>
                <Button 
                  variant={category.status === 'Active' ? 'secondary' : 'success'} 
                  onClick={() => toggleCategoryStatus(category.id)}
                >
                  {category.status === 'Active' ? <i className="fas fa-toggle-off"></i> : <i className="fas fa-toggle-on"></i>}
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Add/Edit Category Modal */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{newCategory.id ? 'Edit Category' : 'Add New Category'}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="categoryName">
              <Form.Label>Category Name</Form.Label>
              <Form.Control
                type="text"
                value={newCategory.name}
                onChange={(e) => setNewCategory({ ...newCategory, name: e.target.value })}
              />
            </Form.Group>
            <Form.Group controlId="categoryStatus">
              <Form.Label>Status</Form.Label>
              <Form.Control
                as="select"
                value={newCategory.status}
                onChange={(e) => setNewCategory({ ...newCategory, status: e.target.value })}
              >
                <option>Active</option>
                <option>Inactive</option>
              </Form.Control>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Close
          </Button>
          <Button variant="primary" onClick={handleCreateOrEditCategory}>
            {newCategory.id ? 'Update Category' : 'Save Category'}
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default CategoryManagement;
