import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios'; // Use axios to handle HTTP requests

// Create AuthContext
export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({ token: null, user: null });

  // Load token from local storage on mount
  useEffect(() => {
    const savedAuth = JSON.parse(localStorage.getItem('auth'));
    if (savedAuth) {
      setAuth(savedAuth);
    }
  }, []);

  // Save auth state to local storage on change
  useEffect(() => {
    if (auth.token) {
      localStorage.setItem('auth', JSON.stringify(auth));
    } else {
      localStorage.removeItem('auth');
    }
  }, [auth]);

  // API call to login using backend API
  const login = async (credentials) => {
    try {
      console.log(credentials)
      const response = await axios.post('/api/Auth/login', credentials);
      const data = response.data;
      setAuth({ token: data.token, user: { role: data.role } });
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
    } catch (error) {
      throw error;
    }
  };

  const logout = () => {
    setAuth({ token: null, user: null });
    localStorage.removeItem('auth');
    window.location.href = '/login'; // Redirect to login
  };

  return (
    <AuthContext.Provider value={{ auth, login, signup, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
