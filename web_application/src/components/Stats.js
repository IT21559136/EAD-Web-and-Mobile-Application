// src/components/Stats.js
import React, { useState } from 'react';
import { Row, Col, Card, Button } from 'antd';
import { UserOutlined, ShoppingCartOutlined, ShopOutlined } from '@ant-design/icons';
import { PieChart, Pie, Cell, Tooltip, Legend, LineChart, Line, XAxis, YAxis, CartesianGrid, ResponsiveContainer } from 'recharts';

const Stats = () => {
  const stats = {
    allUsers: 30,
    customers: 25,
    products: 50,
    newOrders: 5,
    ongoingOrders: 3,
    dispatchedOrders: 7,
    returnedOrders: 2,
  };

  // Data for Pie Chart
  const orderData = [
    { name: 'New Orders', value: stats.newOrders },
    { name: 'Ongoing Orders', value: stats.ongoingOrders },
    { name: 'Dispatched Orders', value: stats.dispatchedOrders },
    { name: 'Returned Orders', value: stats.returnedOrders },
  ];

  // Colors for Pie Chart
  const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

  // Sample Data for Line Chart for 4 weeks (can be extended)
  const weeklyData = [
    {
      week: 1,
      data: [
        { day: 'Mon', newOrders: 5, ongoingOrders: 2, dispatchedOrders: 3, returnedOrders: 0 },
        { day: 'Tue', newOrders: 3, ongoingOrders: 4, dispatchedOrders: 2, returnedOrders: 1 },
        { day: 'Wed', newOrders: 7, ongoingOrders: 2, dispatchedOrders: 6, returnedOrders: 1 },
        { day: 'Thu', newOrders: 4, ongoingOrders: 5, dispatchedOrders: 7, returnedOrders: 0 },
        { day: 'Fri', newOrders: 6, ongoingOrders: 1, dispatchedOrders: 3, returnedOrders: 0 },
        { day: 'Sat', newOrders: 2, ongoingOrders: 6, dispatchedOrders: 4, returnedOrders: 1 },
        { day: 'Sun', newOrders: 4, ongoingOrders: 3, dispatchedOrders: 5, returnedOrders: 2 },
      ],
    },
    {
      week: 2,
      data: [
        { day: 'Mon', newOrders: 8, ongoingOrders: 4, dispatchedOrders: 5, returnedOrders: 1 },
        { day: 'Tue', newOrders: 5, ongoingOrders: 3, dispatchedOrders: 4, returnedOrders: 0 },
        { day: 'Wed', newOrders: 9, ongoingOrders: 2, dispatchedOrders: 7, returnedOrders: 1 },
        { day: 'Thu', newOrders: 6, ongoingOrders: 5, dispatchedOrders: 3, returnedOrders: 2 },
        { day: 'Fri', newOrders: 4, ongoingOrders: 6, dispatchedOrders: 6, returnedOrders: 0 },
        { day: 'Sat', newOrders: 7, ongoingOrders: 4, dispatchedOrders: 2, returnedOrders: 0 },
        { day: 'Sun', newOrders: 5, ongoingOrders: 3, dispatchedOrders: 4, returnedOrders: 2 },
      ],
    },
    // Add more week data as needed
  ];

  // State for current week index
  const [currentWeekIndex, setCurrentWeekIndex] = useState(0);

  // Get current week's data
  const currentWeekData = weeklyData[currentWeekIndex]?.data;

  // Handlers for next/previous week navigation
  const handleNextWeek = () => {
    if (currentWeekIndex < weeklyData.length - 1) {
      setCurrentWeekIndex(currentWeekIndex + 1);
    }
  };

  const handlePreviousWeek = () => {
    if (currentWeekIndex > 0) {
      setCurrentWeekIndex(currentWeekIndex - 1);
    }
  };

  return (
    <div>
      <h2>Dashboard Stats</h2>
      {/* Stats Cards */}
      <Row gutter={[16, 16]}>
        <Col span={8}>
          <Card>
            <UserOutlined style={{ fontSize: '32px', color: '#1890ff' }} />
            <h3>Total Users</h3>
            <p>{stats.allUsers}</p>
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <ShoppingCartOutlined style={{ fontSize: '32px', color: '#52c41a' }} />
            <h3>Customers</h3>
            <p>{stats.customers}</p>
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <ShopOutlined style={{ fontSize: '32px', color: '#faad14' }} />
            <h3>Products Listed</h3>
            <p>{stats.products}</p>
          </Card>
        </Col>
      </Row>

      {/* Pie Chart for Orders */}
      <Row gutter={[16, 16]} style={{ marginTop: '40px' }}>
        <Col span={12}>
          <h3>Order Status Overview</h3>
          <PieChart width={400} height={300}>
            <Pie
              data={orderData}
              cx="50%"
              cy="50%"
              outerRadius={100}
              fill="#8884d8"
              dataKey="value"
              label
            >
              {orderData.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
              ))}
            </Pie>
            <Tooltip />
            <Legend />
          </PieChart>
        </Col>

        {/* Line Chart for Weekly Orders */}
        <Col span={12}>
          <h3>Weekly Orders Overview (Week {currentWeekIndex + 1})</h3>
          <Button onClick={handlePreviousWeek} disabled={currentWeekIndex === 0}>
            Previous Week
          </Button>
          <Button onClick={handleNextWeek} disabled={currentWeekIndex === weeklyData.length - 1} style={{ marginLeft: '10px' }}>
            Next Week
          </Button>

          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={currentWeekData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="day" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="newOrders" stroke="#0088FE" name="New Orders" />
              <Line type="monotone" dataKey="ongoingOrders" stroke="#00C49F" name="Ongoing Orders" />
              <Line type="monotone" dataKey="dispatchedOrders" stroke="#FFBB28" name="Dispatched Orders" />
              <Line type="monotone" dataKey="returnedOrders" stroke="#FF8042" name="Returned Orders" />
            </LineChart>
          </ResponsiveContainer>
        </Col>
      </Row>
    </div>
  );
};

export default Stats;
