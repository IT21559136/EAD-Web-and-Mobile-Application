// src/components/Sidebar.js
import React from 'react';
import { Layout, Menu, Button } from 'antd';
import {
  DashboardOutlined,
  AppstoreAddOutlined, // Use a valid icon
  ShoppingCartOutlined,
  StockOutlined,
  UserAddOutlined,
} from '@ant-design/icons';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext'; // Import the AuthContext

const { Sider } = Layout;

const Sidebar = ({ collapsed }) => {
  const { logout } = useAuth(); // Use logout function from AuthContext

  return (
    <Sider trigger={null} collapsible collapsed={collapsed}>
      <div className="logo" style={{ color: 'white', textAlign: 'center', padding: '16px 0' }}>
        My App
      </div>
      <Menu theme="dark" mode="inline">
        <Menu.Item key="1" icon={<DashboardOutlined />}>
          <Link to="/">Dashboard</Link>
        </Menu.Item>
        <Menu.Item key="2" icon={<AppstoreAddOutlined />}>
          <Link to="/product-management">Product Management</Link>
        </Menu.Item>
        <Menu.Item key="3" icon={<ShoppingCartOutlined />}>
          <Link to="/order-management">Order Management</Link>
        </Menu.Item>
        <Menu.Item key="4" icon={<StockOutlined />}>
          <Link to="/inventory-management">Inventory Management</Link>
        </Menu.Item>
        <Menu.Item key="5" icon={<UserAddOutlined />}>
          <Link to="/vendor-management">Vendor Management</Link>
        </Menu.Item>
      </Menu>
      <div style={{ position: 'absolute', bottom: '16px', left: '0', right: '0', padding: '16px' }}>
        <Button 
          type="primary" 
          danger 
          onClick={logout} 
          style={{ width: '100%' }} // Full-width button
        >
          Logout
        </Button>
      </div>
    </Sider>
  );
};

export default Sidebar;
