// src/App.js
import React, { useState } from 'react';
import { Layout } from 'antd';
import { Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import ProductManagement from './pages/ProductManagement';
import OrderManagement from './pages/OrderManagement';
import InventoryManagement from './pages/InventoryManagement';
import VendorManagement from './pages/VendorManagement';
import Login from './pages/Login';
import Signup from './pages/Signup';
import ProtectedRoute from './components/ProtectedRoute';
import { useAuth } from './contexts/AuthContext';

const { Content } = Layout;

const App = () => {
  const [collapsed, setCollapsed] = useState(false);
  const { user } = useAuth();

  return (
    <Layout style={{ minHeight: '100vh' }}>
      {user && <Sidebar collapsed={collapsed} />}
      <Layout className="site-layout">
        <Header setCollapsed={setCollapsed} collapsed={collapsed} />
        <Content style={{ margin: '16px' }}>
          <div className="site-layout-background" style={{ padding: 24, minHeight: 360 }}>
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/signup" element={<Signup />} />
              <Route 
                path="/" 
                element={
                  <ProtectedRoute>
                    <Dashboard />
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/product-management" 
                element={
                  <ProtectedRoute>
                    <ProductManagement />
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/order-management" 
                element={
                  <ProtectedRoute>
                    <OrderManagement />
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/inventory-management" 
                element={
                  <ProtectedRoute>
                    <InventoryManagement />
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/vendor-management" 
                element={
                  <ProtectedRoute>
                    <VendorManagement />
                  </ProtectedRoute>
                } 
              />
            </Routes>
          </div>
        </Content>
        <Footer />
      </Layout>
    </Layout>
  );
};

export default App;
