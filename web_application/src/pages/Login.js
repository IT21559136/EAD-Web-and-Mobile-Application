// src/pages/Login.js
import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Login = () => {
  const { login } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    await login(email, password);
    navigate('/');
  };

  return (
    <div style={{ maxWidth: '400px', margin: 'auto', padding: '20px' }}>
      <h2>Login</h2>
      <Form onSubmitCapture={handleSubmit}>
        <Form.Item
          label="Email"
          name="email"
          // rules={[
          //   { required: true, message: 'Please input your email!' },
          //   { type: 'email', message: 'Please enter a valid email!' }
          // ]}
        >
          <Input 
            value={email} 
            onChange={(e) => setEmail(e.target.value)} 
            placeholder="Enter your email" 
          />
        </Form.Item>
        <Form.Item
          label="Password"
          name="password"
          // rules={[
          //   { required: true, message: 'Please input your password!' },
          //   { min: 6, message: 'Password must be at least 6 characters.' }
          // ]}
        >
          <Input.Password 
            value={password} 
            onChange={(e) => setPassword(e.target.value)} 
            placeholder="Enter your password" 
          />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" style={{ width: '100%' }}>
            Login
          </Button>
        </Form.Item>
      </Form>
      <p>
        Don't have an account? <Link to="/signup">Sign up here</Link>
      </p>
    </div>
  );
};

export default Login;
