// src/components/OrderList.js
import React, { useState } from 'react';
import { ordersData } from '../apiControllers/orderApi';
import { Button, Table, OverlayTrigger, Tooltip } from 'react-bootstrap';
import { FaPlus, FaMinus, FaTrashAlt } from 'react-icons/fa';

const statusStyles = {
  New: "bg-secondary text-white",
  Shipped: "bg-warning text-dark",
  Delivered: "bg-success text-white",
  Cancelled: "bg-danger text-white",
  "Partially Delivered": "bg-info text-white",
  Processing: "bg-primary text-white",
};

const OrderList = () => {
  const [orders, setOrders] = useState(ordersData);

  const handleItemStatusChange = (orderId, itemIndex, newStatus) => {
    const updatedOrders = orders.map(order => {
      if (order.id === orderId) {
        const updatedItems = order.items.map((item, index) => {
          if (index === itemIndex) {
            return { ...item, orderItemStatus: newStatus };
          }
          return item;
        });
        return { ...order, items: updatedItems, status: calculateOrderStatus(updatedItems) };
      }
      return order;
    });
    setOrders(updatedOrders);
  };

  const calculateOrderStatus = (items) => {
    const allDelivered = items.every(item => item.orderItemStatus === "Delivered");
    const anyShipped = items.some(item => item.orderItemStatus === "Shipped");

    if (allDelivered) return "Delivered";
    if (anyShipped) return "Partially Delivered";
    return "Processing";
  };

  const handleQuantityChange = (orderId, itemIndex, newQuantity) => {
    const updatedOrders = orders.map(order => {
      if (order.id === orderId) {
        const updatedItems = order.items.map((item, index) => {
          if (index === itemIndex) {
            return { ...item, quantity: newQuantity };
          }
          return item;
        });
        return { ...order, items: updatedItems, status: calculateOrderStatus(updatedItems) };
      }
      return order;
    });
    setOrders(updatedOrders);
  };

  const handleCancelOrder = (orderId) => {
    const updatedOrders = orders.map(order => {
      if (order.id === orderId) {
        const updatedItems = order.items.map(item => {
          return { ...item, orderItemStatus: "Cancelled" };
        });
        return { ...order, items: updatedItems, status: "Cancelled" };
      }
      return order;
    });
    setOrders(updatedOrders);
  };

  // Function to determine valid status changes
  const getAvailableStatusOptions = (currentStatus) => {
    const statusTransitions = {
      New: ["Shipped", "Cancelled"],
      Shipped: ["Delivered", "Cancelled"],
      Delivered: [],
      Cancelled: []
    };
    return statusTransitions[currentStatus] || [];
  };

  return (
    <div>
      <h2>Order Management</h2>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Order Details</th>
            <th>Customer Info</th>
            <th>Items</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order) => (
            <tr key={order.id}>
              <td>
                <div>{order.id}</div>
                <div>{new Date(order.orderDate).toLocaleString()}</div>
              </td>
              <td>
                <div>Customer ID</div>
                <div>{order.customerId ? order.customerId : 'null'}</div>
                <div>Customer Note</div>
                <div>{order.customerNote}</div>
              </td>
              <td>
                <Table striped bordered>
                  <thead>
                    <tr>
                      <th>Product ID</th>
                      <th>Quantity</th>
                      <th>Status</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {order.items.map((item, index) => (
                      <tr key={item.productId}>
                        <td>{item.productId}</td>
                        <td>{item.quantity}</td>
                        <td>
                          <span className={`badge ${statusStyles[item.orderItemStatus]}`}>{item.orderItemStatus}</span>
                        </td>
                        <td>
                          {item.orderItemStatus === "New" && (
                            <>
                              <OverlayTrigger
                                placement="top"
                                overlay={<Tooltip>Increase Quantity</Tooltip>}
                              >
                                <Button
                                  variant="secondary"
                                  size="sm"
                                  onClick={() => handleQuantityChange(order.id, index, item.quantity + 1)}
                                >
                                  <FaPlus />
                                </Button>
                              </OverlayTrigger>
                              <OverlayTrigger
                                placement="top"
                                overlay={<Tooltip>Decrease Quantity</Tooltip>}
                              >
                                <Button
                                  variant="secondary"
                                  size="sm"
                                  onClick={() => handleQuantityChange(order.id, index, Math.max(0, item.quantity - 1))}
                                >
                                  <FaMinus />
                                </Button>
                              </OverlayTrigger>
                            </>
                          )}
                          <OverlayTrigger
                            placement="top"
                            overlay={<Tooltip>Change Status</Tooltip>}
                          >
                            <select
                              value={item.orderItemStatus}
                              onChange={(e) => handleItemStatusChange(order.id, index, e.target.value)}
                              className="form-select form-select-sm"
                            >
                              <option value={item.orderItemStatus} disabled>{item.orderItemStatus}</option>
                              {getAvailableStatusOptions(item.orderItemStatus).map((status) => (
                                <option key={status} value={status}>{status}</option>
                              ))}
                            </select>
                          </OverlayTrigger>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              </td>
              <td>
                <span className={`badge ${statusStyles[order.status]}`}>{order.status}</span>
              </td>
              <td>
                <OverlayTrigger
                  placement="top"
                  overlay={<Tooltip>Cancel Order</Tooltip>}
                >
                  <Button
                    variant="danger"
                    onClick={() => handleCancelOrder(order.id)}
                    size="sm"
                  >
                    <FaTrashAlt />
                  </Button>
                </OverlayTrigger>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default OrderList;
