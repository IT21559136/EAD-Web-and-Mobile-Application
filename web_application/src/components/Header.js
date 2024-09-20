import React from 'react';
import { Layout, Menu, Button } from 'antd';
import { MenuUnfoldOutlined, MenuFoldOutlined } from '@ant-design/icons';

const { Header } = Layout;

const AppHeader = ({ collapsed, setCollapsed }) => {
  const toggleCollapsed = () => {
    setCollapsed(!collapsed);
  };

  return (
    <Header className="site-layout-background" style={{ padding: 0, background: '#fff' }}>
      <Button
        type="primary"
        onClick={toggleCollapsed}
        style={{ margin: '16px' }}
      >
        {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
      </Button>
      <span style={{ fontSize: '1.5rem', marginLeft: '16px' }}>My Web Application</span>
    </Header>
  );
};

export default AppHeader;
