import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Dropdown, Pagination } from 'react-bootstrap';
import { FaEdit, FaTrashAlt, FaToggleOn, FaToggleOff, FaPlus } from 'react-icons/fa';

const PAGE_SIZE = 6;

const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [roleFilter, setRoleFilter] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [sortConfig, setSortConfig] = useState({ key: 'name', direction: 'asc' });
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  useEffect(() => {
    const authDataString = localStorage.getItem('auth');
    const authData = authDataString ? JSON.parse(authDataString) : null;
  
    if (!authData || !authData.token) {
      console.error('Token not found');
      return;
    }
  
    console.log(authData);
    console.log(authData.token);
  
    const fetchUsers = async () => {
      try {
        const response = await axios.get('/api/Auth/users', {
          headers: {
            'Authorization': `Bearer ${authData.token}`,
            'Content-Type': 'application/json',
          },
        });
  
        // Map API response to match table structure
        const mappedUsers = response.data.map(user => ({
          id: user.id,
          name: user.username || 'N/A', // Use 'username' for 'name' and handle null values
          email: user.email || 'N/A',   // Handle possible null emails
          role: user.role,
          status: user.status === 1 ? 'Active' : 'Inactive', // Map status code to 'Active'/'Inactive'
        }));
  
        setUsers(mappedUsers);
        setTotalPages(Math.ceil(mappedUsers.length / PAGE_SIZE));
      } catch (error) {
        console.error('Error fetching users:', error);
      }
    };
  
    fetchUsers();
  }, []);

  const filteredUsers = users.filter(user =>
    (user.name.toLowerCase().includes(searchTerm.toLowerCase()) || 
    user.email.toLowerCase().includes(searchTerm.toLowerCase())) &&
    (roleFilter === '' || user.role === roleFilter) &&
    (statusFilter === '' || user.status === statusFilter)
  );

  const paginatedUsers = filteredUsers.slice(
    (currentPage - 1) * PAGE_SIZE,
    currentPage * PAGE_SIZE
  );

  useEffect(() => {
    setTotalPages(Math.ceil(filteredUsers.length / PAGE_SIZE));
  }, [filteredUsers.length]);

  const handleSort = (key) => {
    let direction = 'asc';
    if (sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });
    setUsers([...users].sort((a, b) => {
      if (a[key] < b[key]) return direction === 'asc' ? -1 : 1;
      if (a[key] > b[key]) return direction === 'asc' ? 1 : -1;
      return 0;
    }));
  };

  const handleShowModal = (user = null) => {
    setEditingUser(user);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setEditingUser(null);
    setShowModal(false);
  };

  const handleSaveUser = (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);
    const newUser = {
      id: editingUser ? editingUser.id : users.length + 1,
      name: formData.get('name'),
      email: formData.get('email'),
      role: formData.get('role'),
      status: 'Active',
    };
    const updatedUsers = editingUser
      ? users.map(user => (user.id === editingUser.id ? newUser : user))
      : [...users, newUser];
    setUsers(updatedUsers);
    handleCloseModal();
  };

  const handleDeactivateUser = (id) => {
    setUsers(
      users.map(user => user.id === id ? { ...user, status: user.status === 'Active' ? 'Inactive' : 'Active' } : user)
    );
  };

  const handlePageChange = (page) => setCurrentPage(page);

  return (
    <div className="container mx-0">
      <h2>User Management</h2>

      {/* Search, Filter, Add Button */}
      <div className="mb-3 d-flex justify-content-between">
        {/* Search */}
        <input
          type="text"
          className="form-control w-50"
          placeholder="Search by name or email"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        
        {/* Filters */}
        <div className="d-flex">
          <Dropdown onSelect={(role) => setRoleFilter(role)} className="me-2">
            <Dropdown.Toggle variant="secondary">Filter by Role</Dropdown.Toggle>
            <Dropdown.Menu>
              <Dropdown.Item eventKey="">All</Dropdown.Item>
              <Dropdown.Item eventKey="Admin">Admin</Dropdown.Item>
              <Dropdown.Item eventKey="Vendor">Vendor</Dropdown.Item>
              <Dropdown.Item eventKey="CSR">CSR</Dropdown.Item>
            </Dropdown.Menu>
          </Dropdown>
          <Dropdown onSelect={(status) => setStatusFilter(status)}>
            <Dropdown.Toggle variant="secondary">Filter by Status</Dropdown.Toggle>
            <Dropdown.Menu>
              <Dropdown.Item eventKey="">All</Dropdown.Item>
              <Dropdown.Item eventKey="Active">Active</Dropdown.Item>
              <Dropdown.Item eventKey="Inactive">Inactive</Dropdown.Item>
            </Dropdown.Menu>
          </Dropdown>
        </div>
      </div>

      {/* Add User Button */}
      <Button variant="primary" className="mb-3" onClick={() => handleShowModal()}>
        <FaPlus className="me-2" /> Add User
      </Button>

      {/* User Table */}
      <Table striped bordered hover>
        <thead>
          <tr>
            <th onClick={() => handleSort('name')}>Name {sortConfig.key === 'name' && (sortConfig.direction === 'asc' ? '↑' : '↓')}</th>
            <th onClick={() => handleSort('email')}>Email {sortConfig.key === 'email' && (sortConfig.direction === 'asc' ? '↑' : '↓')}</th>
            <th onClick={() => handleSort('role')}>Role {sortConfig.key === 'role' && (sortConfig.direction === 'asc' ? '↑' : '↓')}</th>
            <th onClick={() => handleSort('status')}>Status {sortConfig.key === 'status' && (sortConfig.direction === 'asc' ? '↑' : '↓')}</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {paginatedUsers.map(user => (
            <tr key={user.id}>
              <td>{user.name}</td>
              <td>{user.email}</td>
              <td>{user.role}</td>
              <td>{user.status}</td>
              <td>
                <Button variant="outline-warning" size="sm" onClick={() => handleShowModal(user)}><FaEdit /></Button>{' '}
                <Button variant={user.status === 'Active' ? 'outline-danger' : 'outline-success'} size="sm" onClick={() => handleDeactivateUser(user.id)}>
                  {user.status === 'Active' ? <FaToggleOff /> : <FaToggleOn />}
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Pagination */}
      <Pagination>
        <Pagination.First onClick={() => handlePageChange(1)} disabled={currentPage === 1} />
        <Pagination.Prev onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1} />
        {Array.from({ length: totalPages }, (_, index) => (
          <Pagination.Item key={index + 1} active={index + 1 === currentPage} onClick={() => handlePageChange(index + 1)}>
            {index + 1}
          </Pagination.Item>
        ))}
        <Pagination.Next onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages} />
        <Pagination.Last onClick={() => handlePageChange(totalPages)} disabled={currentPage === totalPages} />
      </Pagination>

      {/* Modal */}
      <Modal show={showModal} onHide={handleCloseModal}>
        <Modal.Header closeButton>
          <Modal.Title>{editingUser ? 'Edit User' : 'Add User'}</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSaveUser}>
          <Modal.Body>
            <Form.Group controlId="formName">
              <Form.Label>Name</Form.Label>
              <Form.Control type="text" name="name" defaultValue={editingUser?.name} required />
            </Form.Group>
            <Form.Group controlId="formEmail" className="mt-3">
              <Form.Label>Email</Form.Label>
              <Form.Control type="email" name="email" defaultValue={editingUser?.email} required />
            </Form.Group>
            <Form.Group controlId="formRole" className="mt-3">
              <Form.Label>Role</Form.Label>
              <Form.Control as="select" name="role" defaultValue={editingUser?.role} required>
                <option value="Admin">Admin</option>
                <option value="Vendor">Vendor</option>
                <option value="CSR">CSR</option>
              </Form.Control>
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>Cancel</Button>
            <Button variant="primary" type="submit">Save</Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </div>
  );
};

export default UserManagement;
