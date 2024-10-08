// components/AdminDashboard.js
import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { Bar, Pie } from 'react-chartjs-2'; // Import both Bar and Pie components
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement // Import ArcElement for pie chart
} from 'chart.js';
import { FaEnvelope } from 'react-icons/fa'; // Import email icon

// Importing data from the dashboard data file
import {
  lowStockProductsData,
  totalOrdersCount,
  orderStatusData,
  monthlyIncomeData,
  incomeChartLabels
} from '../data/dashboardData';

// Register necessary components
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement);

const AdminDashboard = () => {
  // Chart data for total income
  const incomeData = {
    labels: incomeChartLabels,
    datasets: [
      {
        label: 'Total Income ($)',
        data: monthlyIncomeData,
        backgroundColor: 'rgba(75, 192, 192, 0.6)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1,
      },
    ],
  };

  const handleInformVendor = (vendorEmail) => {
    // Logic to send email to the vendor
    console.log(`Email sent to ${vendorEmail}`);
    alert(`Email sent to ${vendorEmail}`);
  };

  return (
    <Container className="mx-0">
      <h1>Admin Dashboard</h1>
      <Row className="mb-4">
        <Col md={6}>
          <Card>
            <Card.Body>
              <Card.Title>Low Stock Products</Card.Title>
              <Row>
                {lowStockProductsData.map((product) => (
                  <Col md={6} key={product.productId}>
                    <div className="d-flex justify-content-between align-items-center mb-2">
                      <span>{product.productName} - In Stock: {product.inStock}</span>
                      <Button
                        variant="outline-primary"
                        size="sm"
                        onClick={() => handleInformVendor(product.vendorEmail)}
                      >
                        <FaEnvelope />
                      </Button>
                    </div>
                  </Col>
                ))}
              </Row>
            </Card.Body>
          </Card>
        </Col>
        <Col md={6}>
          <Card>
            <Card.Body>
              <Card.Title>Total Orders</Card.Title>
              <h2>{totalOrdersCount}</h2>
            </Card.Body>
          </Card>
        </Col>
      </Row>
      <Row className="mb-4">
        <Col md={6}>
          <Card>
            <Card.Body>
              <Card.Title>Order Status Distribution</Card.Title>
              <Pie data={orderStatusData} options={{ responsive: true }} />
            </Card.Body>
          </Card>
        </Col>
        <Col md={6}>
          <Card>
            <Card.Body>
              <Card.Title>Total Income Last 6 Months</Card.Title>
              <Bar data={incomeData} options={{ responsive: true }} />
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default AdminDashboard;
