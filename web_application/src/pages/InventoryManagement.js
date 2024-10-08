// components/InventoryManagement.js
import React, { useState } from 'react';
import { Table, Badge, Button, Form } from 'react-bootstrap';

// Importing inventory data from the new data file
import { inventoryItemsData } from '../data/inventoryData';

const InventoryManagement = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [inventory, setInventory] = useState(inventoryItemsData);

  const handleSearchChange = (event) => {
    setSearchQuery(event.target.value);
  };

  // Function to filter inventory based on the search query
  const filteredInventory = inventory.filter((item) => {
    const lowerCaseQuery = searchQuery.toLowerCase();
    return (
      item.productName.toLowerCase().includes(lowerCaseQuery) ||
      item.productId.toLowerCase().includes(lowerCaseQuery) ||
      item.vendorName.toLowerCase().includes(lowerCaseQuery) ||
      item.vendorEmail.toLowerCase().includes(lowerCaseQuery)
    );
  });

  const handleInformVendor = (vendorEmail) => {
    // Logic to send email to the vendor
    console.log(`Email sent to ${vendorEmail}`);
    alert(`Email sent to ${vendorEmail}`);
  };

  const getStockBadge = (quantity) => {
    if (quantity === 0) {
      return <Badge bg="danger">No Stock</Badge>;
    } else if (quantity < 10) {
      return <Badge bg="warning">Low Stock</Badge>;
    } else {
      return <Badge bg="success">In Stock</Badge>;
    }
  };

  return (
    <div>
      <h2>Inventory Management</h2>
      <Form className="mb-3">
        <Form.Group controlId="search">
          <Form.Control
            type="text"
            placeholder="Search by Product ID, Name, Vendor Name, or Email"
            value={searchQuery}
            onChange={handleSearchChange}
          />
        </Form.Group>
      </Form>

      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Product Details</th>
            <th>Vendor Details</th>
            <th>Category</th>
            <th>Stock Count</th>
            <th>In Stock</th>
            <th>Price</th>
            <th>Pending Orders</th>
            <th>Dispatched Last Month</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredInventory.length > 0 ? (
            filteredInventory.map((item) => (
              <tr key={item.id}>
                <td>
                  <div>{item.productId}</div>
                  <div>{item.productName}</div>
                </td>
                <td>
                  <div>{item.vendorName}</div>
                  <div>{item.vendorEmail}</div>
                </td>
                <td>{item.category}</td>
                <td>{item.inStock}</td>
                <td>
                  {getStockBadge(item.inStock)}
                </td>
                <td>${item.price.toFixed(2)}</td>
                <td>{item.pendingOrders}</td>
                <td>{item.dispatchedLastMonth}</td>
                <td>
                  {item.inStock < 10 && (
                    <Button
                      variant="outline-warning"
                      className="ms-2"
                      onClick={() => handleInformVendor(item.vendorEmail)}
                    >
                      Inform Vendor
                    </Button>
                  )}
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="10" className="text-center">
                No products found.
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
};

export default InventoryManagement;
