// src/components/ProtectedRoute.js
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const ProtectedRoute = ({ children }) => {
  const { token } = useAuth();

  // Redirect to login if token is not present
  return token ? children : <Navigate to="/login" />;
};

export default ProtectedRoute;
