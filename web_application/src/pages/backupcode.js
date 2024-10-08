import React, { useState, useEffect } from 'react';
import { Table, Button, Collapse } from 'react-bootstrap';
import { FaEye, FaEyeSlash, FaEdit, FaTrashAlt, FaTimesCircle, FaCheckCircle } from 'react-icons/fa';
import { cancelOrder, editOrder, deleteOrder, cancelOrderItem, editOrderItem, setOrderItemStatus, fetchOrders } from '../apiControllers/orderApi'; // Import the API functions

// (fetchOrders and getStatusColor functions remain unchanged)

const OrderManagement = () => {
  const [orders, setOrders] = useState([]);
  const [openOrder, setOpenOrder] = useState(null); // Track which order is open

  useEffect(() => {
    const loadOrders = async () => {
      const orderData = await fetchOrders();
      setOrders(orderData);
    };
    loadOrders();
  }, []);

  // Toggle the collapse for a specific order
  const toggleOrder = (orderId) => {
    setOpenOrder(openOrder === orderId ? null : orderId); // If already open, close; otherwise, open
  };

  // Updated action handlers to APIs
  const handleCancelOrder = async (orderId) => {
    const response = await cancelOrder(orderId);
    console.log(response); // Log or handle the response as needed
  };

  const handleEditOrder = async (orderId) => {
    const response = await editOrder(orderId);
    console.log(response); // Log or handle the response as needed
  };

  const handleDeleteOrder = async (orderId) => {
    const response = await deleteOrder(orderId);
    console.log(response); // Log or handle the response as needed
  };

  const handleCancelOrderItem = async (orderId, itemId) => {
    const response = await cancelOrderItem(orderId, itemId);
    console.log(response); // Log or handle the response as needed
  };

  const handleEditOrderItem = async (orderId, itemId) => {
    const response = await editOrderItem(orderId, itemId);
    console.log(response); // Log or handle the response as needed
  };

  const handleSetOrderItemStatus = async (orderId, itemId) => {
    const response = await setOrderItemStatus(orderId, itemId);
    console.log(response); // Log or handle the response as needed
  };

  // Function to get color based on status
  const getStatusColor = (status) => {
    switch (status) {
      case "New":
        return { backgroundColor: '#28a745', color: '#ffffff' }; // Bright green for New
      case "Delivered":
        return { backgroundColor: '#007bff', color: '#ffffff' }; // Bright blue for Delivered
      case "Partially Delivered":
        return { backgroundColor: '#ffc107', color: '#ffffff' }; // Bright yellow for Partially Delivered
      case "Processing":
        return { backgroundColor: '#17a2b8', color: '#ffffff' }; // Bright cyan for Processing
      case "Cancelled":
        return { backgroundColor: '#dc3545', color: '#ffffff' }; // Bright red for Cancelled
      default:
        return { backgroundColor: '#6c757d', color: '#ffffff' }; // Default gray for unknown statuses
    }
  };

  return (
    <div>
      <h2>Order Management</h2>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Customer ID</th>
            <th>Status</th>
            <th>Order Date</th>
            <th>Customer Note</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order) => (
            <React.Fragment key={order.id}>
              {/* Main Order Row */}
              <tr>
                <td>{order.id}</td>
                <td>{order.customerId || 'N/A'}</td>
                <td style={getStatusColor(order.status)}>{order.status}</td>
                <td>{new Date(order.orderDate).toLocaleString()}</td>
                <td>{order.customerNote}</td>
                <td>
                  <Button onClick={() => toggleOrder(order.id)} variant="link">
                    {openOrder === order.id ? <FaEyeSlash /> : <FaEye />} {/* Show/Hide Items Icon */}
                  </Button>
                  <Button onClick={() => handleCancelOrder(order.id)} variant="link">
                    <FaTimesCircle /> {/* Cancel Order Icon */}
                  </Button>
                  <Button onClick={() => handleEditOrder(order.id)} variant="link">
                    <FaEdit /> {/* Edit Order Icon */}
                  </Button>
                  <Button onClick={() => handleDeleteOrder(order.id)} variant="link">
                    <FaTrashAlt /> {/* Delete Order Icon */}
                  </Button>
                </td>
              </tr>

              {/* Nested Order Items Row */}
              <tr>
                <td colSpan="6">
                  <Collapse in={openOrder === order.id}>
                    <div>
                      <Table size="sm" bordered>
                        <thead>
                          <tr>
                            <th>Product ID</th>
                            <th>Quantity</th>
                            <th>Vendor Email</th>
                            <th>Order Item Status</th>
                            <th>Actions</th>
                          </tr>
                        </thead>
                        <tbody>
                          {order.items.map((item, index) => (
                            <tr key={index}>
                              <td>{item.productId}</td>
                              <td>{item.quantity}</td>
                              <td>{item.vendorEmail}</td>
                              <td style={getStatusColor(item.orderItemStatus)}>{item.orderItemStatus}</td>
                              <td>
                                <Button onClick={() => handleCancelOrderItem(order.id, item.productId)} variant="link">
                                  <FaTimesCircle /> {/* Cancel Item Icon */}
                                </Button>
                                <Button onClick={() => handleEditOrderItem(order.id, item.productId)} variant="link">
                                  <FaEdit /> {/* Edit Item Icon */}
                                </Button>
                                <Button onClick={() => handleSetOrderItemStatus(order.id, item.productId)} variant="link">
                                  <FaCheckCircle /> {/* Set Item Status Icon */}
                                </Button>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </Table>
                    </div>
                  </Collapse>
                </td>
              </tr>
            </React.Fragment>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default OrderManagement;
