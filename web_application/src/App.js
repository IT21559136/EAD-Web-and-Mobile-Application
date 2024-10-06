// src/App.js
import React, { useState } from 'react';
import { Layout } from 'antd';
import { Route, Routes, Navigate } from 'react-router-dom'; // Add Navigate for redirection
import Header from './components/Header';
import Footer from './components/Footer';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import ProductManagement from './pages/ProductManagement';
import OrderManagement from './pages/OrderManagement';
import InventoryManagement from './pages/InventoryManagement';
import VendorManagement from './pages/VendorManagement';
import Login from './pages/Login'; // Import your Login page
import { useAuth } from './contexts/AuthContext'; // Import AuthContext
import './App.css';

const { Content } = Layout;

const App = () => {
  const [collapsed, setCollapsed] = useState(false);
  const { user } = useAuth(); // Get user state from AuthContext

  return (
    <Layout style={{ minHeight: '100vh' }}>
      {user && <Sidebar collapsed={collapsed} />} {/* Show sidebar only if user is logged in */}
      <Layout className="site-layout">
        <Header setCollapsed={setCollapsed} collapsed={collapsed} />
        <Content style={{ margin: '16px' }}>
          <div className="site-layout-background" style={{ padding: 24, minHeight: 360 }}>
            <Routes>
              <Route path="/" element={user ? <Dashboard /> : <Navigate to="/login" />} />
              <Route path="/product-management" element={user ? <ProductManagement /> : <Navigate to="/login" />} />
              <Route path="/order-management" element={user ? <OrderManagement /> : <Navigate to="/login" />} />
              <Route path="/inventory-management" element={user ? <InventoryManagement /> : <Navigate to="/login" />} />
              <Route path="/vendor-management" element={user ? <VendorManagement /> : <Navigate to="/login" />} />
              <Route path="/login" element={<Login />} /> {/* Add your login route */}
            </Routes>
          </div>
        </Content>
        <Footer />
      </Layout>
    </Layout>
  );
};

export default App;
