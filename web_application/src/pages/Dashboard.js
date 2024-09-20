// src/Dashboard.js
import React from 'react';
import { Tabs } from 'antd';
import Stats from '../components/Stats';
import UserManagement from '../components/UserManagement';

const { TabPane } = Tabs;

const Dashboard = () => {
  return (
    <Tabs defaultActiveKey="1">
      {/* Stats Tab */}
      <TabPane tab="Stats" key="1">
        <Stats />
      </TabPane>

      {/* User Management Tab */}
      <TabPane tab="User Management" key="2">
        <UserManagement />
      </TabPane>
    </Tabs>
  );
};

export default Dashboard;
