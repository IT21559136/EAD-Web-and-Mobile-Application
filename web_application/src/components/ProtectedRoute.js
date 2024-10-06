// src/components/ProtectedRoute.js
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const ProtectedRoute = ({ element }) => {
  const { token } = useAuth(); // Get authentication token from AuthContext

  // If not authenticated, redirect to the login page
  return token ? element : <Navigate to="/login" />;
};

export default ProtectedRoute;
