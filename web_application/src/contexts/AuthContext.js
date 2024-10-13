import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // Import useNavigate

// Create AuthContext
export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({ token: null, user: null });
  const navigate = useNavigate(); // Initialize useNavigate

  // Helper function to check if the token is expired
  const isTokenExpired = (expirationTime) => {
    const currentTime = new Date().getTime();
    return currentTime > expirationTime;
  };

  // Load token from local storage on mount
  useEffect(() => {
    const savedAuth = JSON.parse(localStorage.getItem('auth'));
    const expirationTime = localStorage.getItem('tokenExpiration');

    if (savedAuth && expirationTime) {
      // Check if the token has expired
      if (isTokenExpired(expirationTime)) {
        logout(); // If token has expired, log out the user
      } else {
        setAuth(savedAuth);
        const remainingTime = expirationTime - new Date().getTime();
        // Set a timer to automatically logout after remaining time
        setTimeout(logout, remainingTime);
      }
    }
  }, []);

  // Save auth state to local storage on change
  useEffect(() => {
    if (auth.token) {
      localStorage.setItem('auth', JSON.stringify(auth));
      const expirationTime = new Date().getTime() + 60 * 60 * 1000; // 1 hour from now
      localStorage.setItem('tokenExpiration', expirationTime);
      // Set a timer to automatically logout after one hour
      setTimeout(logout, 60 * 60 * 1000); // 1 hour
    } else {
      localStorage.removeItem('auth');
      localStorage.removeItem('tokenExpiration');
    }
  }, [auth]);

  // API call to login using backend API
  const login = async (credentials) => {
    try {
      const response = await axios.post('/api/Auth/login', credentials);
      const data = response.data;
      setAuth({ token: data.token, user: { role: data.role } });

      // Redirect based on user role
      if (data.role === 'Admin') {
        navigate('/'); // Admin Dashboard
      } else if (data.role === 'Vendor') {
        navigate('/vendor-dashboard'); // Vendor Dashboard
      }
      // Add other roles if necessary
    } catch (error) {
      throw error;
    }
  };

  // API call to signup using backend API
  const signup = async (userData) => {
    try {
      const response = await axios.post('/api/Auth/register', userData);
      const data = response.data;
      setAuth({ token: data.token, user: { role: data.role } });
      navigate('/'); // Redirect after signup, you can customize this
    } catch (error) {
      throw error;
    }
  };

  const logout = () => {
    setAuth({ token: null, user: null });
    localStorage.removeItem('auth');
    localStorage.removeItem('tokenExpiration');
    navigate('/login'); // Redirect to login
  };

  return (
    <AuthContext.Provider value={{ auth, login, signup, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
