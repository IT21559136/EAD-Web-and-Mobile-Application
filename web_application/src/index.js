import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom'; // Import BrowserRouter
import 'antd/dist/reset.css'; // Import Ant Design CSS
import './index.css'; 
import App from './App';

ReactDOM.render(
  <BrowserRouter> {/* Wrap App inside BrowserRouter */}
    <App />
  </BrowserRouter>,
  document.getElementById('root')
);
