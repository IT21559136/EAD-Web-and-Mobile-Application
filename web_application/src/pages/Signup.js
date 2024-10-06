// src/pages/Signup.js
import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Signup = () => {
  const { signup } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      message.error('Passwords do not match!');
      return;
    }
    await signup(email, password);
    //navigate('/login');
  };

  return (
    <div style={{ maxWidth: '400px', margin: 'auto', padding: '20px' }}>
      <h2>Sign Up</h2>
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
        <Form.Item
          label="Confirm Password"
          name="confirm"
          // rules={[
          //   { required: true, message: 'Please confirm your password!' },
          //   { min: 6, message: 'Password must be at least 6 characters.' }
          // ]}
        >
          <Input.Password 
            value={confirmPassword} 
            onChange={(e) => setConfirmPassword(e.target.value)} 
            placeholder="Confirm your password" 
          />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" style={{ width: '100%' }}>
            Sign Up
          </Button>
        </Form.Item>
      </Form>
      <p>
        Already have an account? <Link to="/login">Log in here</Link>
      </p>
    </div>
  );
};

export default Signup;
