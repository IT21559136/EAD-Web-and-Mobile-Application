import React, { useContext } from 'react';
import { Route, Navigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';

const PrivateRoute = ({ element: Component, allowedRoles, ...rest }) => {
  const { auth } = useContext(AuthContext);

  // If user is logged in and has the appropriate role, render the component, else redirect to login
  return auth.token && allowedRoles.includes(auth.user.role) ? (
    <Component {...rest} />
  ) : (
    <Navigate to="/login" />
  );
};

export default PrivateRoute;
