import React, { useState } from 'react';
import { Layout } from 'antd';
import { Route, Routes } from 'react-router-dom';  // Use Routes instead of Switch
import Header from './components/Header';
import Footer from './components/Footer';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import ProductManagement from './pages/ProductManagement';
import OrderManagement from './pages/OrderManagement';
import InventoryManagement from './pages/InventoryManagement';
import VendorManagement from './pages/VendorManagement';
import './App.css';

const { Content } = Layout;

const App = () => {
  const [collapsed, setCollapsed] = useState(false);

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sidebar collapsed={collapsed} />
      <Layout className="site-layout">
        <Header setCollapsed={setCollapsed} collapsed={collapsed} />
        <Content style={{ margin: '16px' }}>
          <div className="site-layout-background" style={{ padding: 24, minHeight: 360 }}>
            {/* Define Routes for different pages */}
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/product-management" element={<ProductManagement />} />
              <Route path="/order-management" element={<OrderManagement />} />
              <Route path="/inventory-management" element={<InventoryManagement />} />
              <Route path="/vendor-management" element={<VendorManagement />} />
            </Routes>
          </div>
        </Content>
        <Footer />
      </Layout>
    </Layout>
  );
};

export default App;
