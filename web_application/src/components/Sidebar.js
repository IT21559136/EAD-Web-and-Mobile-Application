import React from 'react';
import { Layout, Menu } from 'antd';
import { DashboardOutlined, ShoppingCartOutlined, UnorderedListOutlined, AppstoreOutlined, TeamOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';

const { Sider } = Layout;

const Sidebar = ({ collapsed }) => {
  return (
    <Sider trigger={null} collapsible collapsed={collapsed} style={{ minHeight: '100vh' }}>
      <div className="logo" style={{ color: '#fff', padding: '16px', textAlign: 'center' }}>
        {collapsed ? 'App' : 'My Web App'}
      </div>
      <Menu theme="dark" mode="inline" defaultSelectedKeys={['1']}>
        <Menu.Item key="1" icon={<DashboardOutlined />}>
          <Link to="/">Dashboard</Link>
        </Menu.Item>
        <Menu.Item key="2" icon={<AppstoreOutlined />}>
          <Link to="/product-management">Product Management</Link>
        </Menu.Item>
        <Menu.Item key="3" icon={<UnorderedListOutlined />}>
          <Link to="/order-management">Order Management</Link>
        </Menu.Item>
        <Menu.Item key="4" icon={<ShoppingCartOutlined />}>
          <Link to="/inventory-management">Inventory Management</Link>
        </Menu.Item>
        <Menu.Item key="5" icon={<TeamOutlined />}>
          <Link to="/vendor-management">Vendor Management</Link>
        </Menu.Item>
      </Menu>
    </Sider>
  );
};

export default Sidebar;
