import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Sidebar from './components/Sidebar';
import Footer from './components/Footer';
import './App.css';
import { Container } from 'react-bootstrap';
import AdminDashboard from './pages/AdminDashboard';
import UserManagement from './pages/UserManagement';
import ProductManagement from './pages/ProductManagement';
import InventoryManagement from './pages/InventoryManagement';
import OrderManagement from './pages/OrderManagement';
import Login from './pages/Login';
import Signup from './pages/Signup';
import { AuthProvider } from './contexts/AuthContext';
import PrivateRoute from './contexts/PrivateRoute';
import CategoryManagement from './pages/CategoryManagement';
import VendorDashboard from './pages/VendorDashboard';
import VendorOrderManagement from './pages/VendorOrderList';

const App = () => {
  const [isSidebarOpen, setSidebarOpen] = useState(false);

  const toggleSidebar = () => {
    setSidebarOpen(!isSidebarOpen);
  };

  return (
    <Router>
      <AuthProvider>
        <div className="App">
          <Header toggleSidebar={toggleSidebar} />
          <div className="d-flex flex-grow-1 content-with-header">
            <Sidebar isOpen={isSidebarOpen} />
            <Container fluid className={`content ${isSidebarOpen ? 'with-sidebar' : 'without-sidebar'}`}>
              <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<Signup />} />
                
                <Route path="/" element={<PrivateRoute element={AdminDashboard} allowedRoles={['Admin']} />} />
                <Route path="/vendor-dashboard" element={<PrivateRoute element={VendorDashboard} allowedRoles={['Vendor', 'Admin']} />} />
                <Route path="/user-management" element={<PrivateRoute element={UserManagement} allowedRoles={['Admin']} />} />
                <Route path="/product-management" element={<PrivateRoute element={ProductManagement} allowedRoles={['Admin', 'Vendor']} />} />
                <Route path="/inventory-management" element={<PrivateRoute element={InventoryManagement} allowedRoles={['Vendor', 'Admin']} />} />
                <Route path="/order-management" element={<PrivateRoute element={OrderManagement} allowedRoles={['Vendor', 'Admin']} />} />
                <Route path="/vendor-order-management" element={<PrivateRoute element={VendorOrderManagement} allowedRoles={['Vendor', 'Admin']} />} />
                <Route path="/category-management" element={<PrivateRoute element={CategoryManagement} allowedRoles={['Vendor', 'Admin']} />} />
              </Routes>
            </Container>
          </div>
          <Footer />
        </div>
      </AuthProvider>
    </Router>
  );
};

export default App;
