// VendorOrderManagement.js

import React, { useState, useEffect } from 'react';
import {
    vendorFetchOrders,
    vendorCreateOrder,
    vendorUpdateOrder,
    vendorDeleteOrder,
    vendorFilterOrdersByStatus
} from '../apiControllers/orderApi';
import { Container, Row, Col, Form, Button, Table } from 'react-bootstrap';

const VendorOrderManagement = () => {
    const [orders, setOrders] = useState([]);
    const [newVendorOrder, setNewVendorOrder] = useState({ items: [], status: 'New', customerNote: '' });
    const [filterStatus, setFilterStatus] = useState('');

    useEffect(() => {
        const loadOrders = async () => {
            const fetchedOrders = await vendorFetchOrders();
            setOrders(fetchedOrders);
        };
        loadOrders();
    }, []);

    const handleCreateOrder = async () => {
        const createdOrder = await vendorCreateOrder(newVendorOrder);
        setOrders([...orders, createdOrder]);
        setNewVendorOrder({ items: [], status: 'New', customerNote: '' }); // Reset form
    };

    const handleUpdateOrder = async (orderId, updatedDetails) => {
        const updatedOrder = await vendorUpdateOrder(orderId, updatedDetails);
        setOrders(orders.map(order => (order.id === orderId ? updatedOrder : order)));
    };

    const handleCancelOrder = async (orderId) => {
        const updatedOrder = await vendorUpdateOrder(orderId, { status: 'Cancelled' });
        setOrders(orders.map(order => (order.id === orderId ? updatedOrder : order)));
    };

    const handleFilterOrders = async () => {
        const filteredOrders = await vendorFilterOrdersByStatus(filterStatus);
        setOrders(filteredOrders);
    };

    return (
        <div>
            <h1 className="mt-4">Vendor Order Management</h1>
            <Row className="mb-4">
                <Col md={6}>
                    <h5>Filter Vendor Orders</h5>
                    <Form.Group controlId="filterStatus">
                        <Form.Label>Filter by Status</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter status to filter"
                            value={filterStatus}
                            onChange={e => setFilterStatus(e.target.value)}
                        />
                    </Form.Group>
                    <Button variant="secondary" onClick={handleFilterOrders}>Filter Orders</Button>
                </Col>
            </Row>

            <h5>Vendor Order List</h5>
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Product Name</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {orders.map(order => (
                        <tr key={order.id}>
                            <td>{order.id}</td>
                            <td>{order.productName}</td> {/* Assuming productName is part of the order data */}
                            <td>{order.status}</td>
                            <td>
                                {order.status === 'Processing' && (
                                    <Button
                                        variant="success"
                                        onClick={() => handleUpdateOrder(order.id, { status: 'Shipped' })}
                                        className="me-2"
                                    >
                                        Mark as Shipped
                                    </Button>
                                )}
                                {order.status === 'Shipped' && (
                                    <Button
                                        variant="warning"
                                        onClick={() => handleUpdateOrder(order.id, { status: 'Delivered' })}
                                        className="me-2"
                                    >
                                        Mark as Delivered
                                    </Button>
                                )}
                                {order.status === 'Cancelled' ? (
                                    <span>No actions available</span>
                                ) : (
                                    <Button
                                        variant="danger"
                                        onClick={() => handleCancelOrder(order.id)}
                                    >
                                        Cancel
                                    </Button>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>
        </div>
    );
};

export default VendorOrderManagement;
