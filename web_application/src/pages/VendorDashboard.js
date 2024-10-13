// components/VendorDashboard.js
import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { Bar, Pie } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement
} from 'chart.js';
import { FaWarehouse } from 'react-icons/fa'; // Import restock icon

import {
  lowStockProductsDataVendor,
  totalOrdersCountVendor,
  orderStatusDataVendor,
  monthlyIncomeDataVendor,
  incomeChartLabelsVendor
} from '../data/dashboardData';

// Register necessary components
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement);

const VendorDashboard = () => {
  const incomeData = {
    labels: incomeChartLabelsVendor,
    datasets: [
      {
        label: 'Total Income ($)',
        data: monthlyIncomeDataVendor,
        backgroundColor: 'rgba(75, 192, 192, 0.6)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1,
      },
    ],
  };

  const handleRestockProduct = (productName) => {
    // Logic to handle restocking the product
    console.log(`Restock requested for ${productName}`);
    alert(`Restock requested for ${productName}`);
  };

  return (
    <Container className="mx-0">
      <h1>Vendor Dashboard</h1>
      <Row className="mb-4">
        <Col md={6}>
          <Card>
            <Card.Body>
              <Card.Title>Low Stock Products</Card.Title>
              <Row>
                {lowStockProductsDataVendor.map((product) => (
                  <Col md={6} key={product.productId}>
                    <div className="d-flex justify-content-between align-items-center mb-2">
                      <span>{product.productName} - In Stock: {product.inStock}</span>
                      <Button
                        variant="outline-primary"
                        size="sm"
                        onClick={() => handleRestockProduct(product.productName)}
                      >
                        <FaWarehouse /> {/* Changed icon */}
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
              <h2>{totalOrdersCountVendor}</h2>
            </Card.Body>
          </Card>
        </Col>
      </Row>
      <Row className="mb-4">
        <Col md={6}>
          <Card>
            <Card.Body>
              <Card.Title>Order Status Distribution</Card.Title>
              <Pie data={orderStatusDataVendor} options={{ responsive: true }} />
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

export default VendorDashboard;
