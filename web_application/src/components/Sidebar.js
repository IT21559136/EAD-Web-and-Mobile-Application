import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Button } from 'react-bootstrap';
import { useContext } from 'react';
import { AuthContext } from '../contexts/AuthContext';

const Sidebar = ({ isOpen }) => {
  const location = useLocation();
  const { logout, auth } = useContext(AuthContext);
  
  // Define menu items with roles
  const menuItems = [
    { path: '/', label: 'Admin Dashboard', icon: 'fas fa-users', roles: ['Admin'] },
    { path: '/vendor-dashboard', label: 'Vendor Dashboard', icon: 'fas fa-shopping-cart', roles: ['Vendor'] },
    { path: '/user-management', label: 'User Management', icon: 'fas fa-users', roles: ['Admin'] },
    { path: '/category-management', label: 'Category Management', icon: 'fas fa-tags', roles: ['Admin'] },
    { path: '/product-management', label: 'Product Management', icon: 'fas fa-box', roles: ['Admin', 'Vendor'] },
    { path: '/inventory-management', label: 'Inventory Management', icon: 'fas fa-warehouse', roles: ['Admin'] },
    { path: '/order-management', label: 'Order Management', icon: 'fas fa-shopping-cart', roles: ['Admin'] },
    { path: '/vendor-order-management', label: 'Vendor Order Management', icon: 'fas fa-shopping-cart', roles: ['Vendor'] },
  ];

  // Filter menu items based on user's role
  const allowedMenuItems = menuItems.filter(item => 
    item.roles.includes(auth.user?.role)
  );

  return (
    <div className={`bg-dark text-white sidebar ${isOpen ? 'open' : ''}`}>
      <ul className="list-unstyled p-3">
        {allowedMenuItems.map((item) => (
          <li key={item.path}>
            <Link
              to={item.path}
              className={`sidebar-link ${location.pathname === item.path ? 'active' : ''}`}
              title={item.label} // Tooltip
            >
              <i className={item.icon}></i> <span>{item.label}</span>
            </Link>
          </li>
        ))}
      </ul>
      {/* Centered logout button */}
      <div className="logout-container mt-auto d-flex justify-content-center flex-column align-items-center">
        <Button variant="danger" onClick={logout} className="logout-btn">
          Logout
        </Button>
        <div className="version-info text-center mt-2">
          <p className="mb-1 small">App Version: 1.0.0</p>  {/* Smaller font */}
          <p className="small">Build Version: 2024.09.30</p>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
