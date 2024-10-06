// src/contexts/AuthContext.js
import React, { createContext, useState, useContext } from 'react';
import { message } from 'antd';
import { useNavigate } from 'react-router-dom';

// Create the context
const AuthContext = createContext();

// Custom hook for convenience
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

const mockApi = (url, data) => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (url === '/login') {
        if (data.username === 'admin' && data.password === 'admin') {
          resolve({ token: 'mocked-jwt-token', user: { username: 'admin', role: 'Admin' } });
        } else {
          reject(new Error('Invalid credentials'));
        }
      } else if (url === '/signup') {
        resolve({ message: 'User created successfully' });
      } else {
        reject(new Error('Unknown endpoint'));
      }
    }, 1000); // Mock delay
  });
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const navigate = useNavigate(); // Use navigate for redirection

  const login = async (username, password) => {
    try {
      const response = await mockApi('/login', { username, password });
      setUser(response.user);
      setToken(response.token);
      localStorage.setItem('token', response.token);
      message.success('Logged in successfully');
      navigate('/'); // Redirect to dashboard on successful login
    } catch (error) {
      message.error(error.message);
    }
  };

  const signup = async (username, password) => {
    try {
      const response = await mockApi('/signup', { username, password });
      message.success(response.message);
    } catch (error) {
      message.error(error.message);
    }
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('token');
    message.success('Logged out successfully');
    navigate('/login'); // Redirect to login page on logout
  };

  return (
    <AuthContext.Provider value={{ user, token, login, signup, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
