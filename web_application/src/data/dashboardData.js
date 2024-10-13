// components/dashboardData.js

export const lowStockProductsData = [
    {
        productId: 'P001',
        productName: 'Samsung Galaxy S21',
        inStock: 3,
        vendorName: 'Tech World Inc.',
        vendorEmail: 'vendor1@techworld.com'
    },
    {
        productId: 'P005',
        productName: 'Sony WH-1000XM5 Headphones',
        inStock: 5,
        vendorName: 'Audio Gear Pro',
        vendorEmail: 'vendor5@audiogearpro.com'
    },
    {
        productId: 'P002',
        productName: 'Dyson V11 Vacuum Cleaner',
        inStock: 2,
        vendorName: 'Home Essentials LLC',
        vendorEmail: 'vendor2@homeessentials.com'
    },
    {
        productId: 'P003',
        productName: 'Nike Air Max 270',
        inStock: 4,
        vendorName: 'Fashion Store Ltd.',
        vendorEmail: 'vendor3@fashionstore.com'
    },
];

export const totalOrdersCount = 150;

export const orderStatusData = {
    labels: ['Processing', 'Delivered', 'Canceled', 'Returned'],
    datasets: [
        {
            label: 'Order Status Distribution',
            data: [40, 90, 10, 10], // Example counts for each status
            backgroundColor: [
                'rgba(255, 206, 86, 0.6)', // Processing
                'rgba(75, 192, 192, 0.6)', // Delivered
                'rgba(255, 99, 132, 0.6)',  // Canceled
                'rgba(153, 102, 255, 0.6)', // Returned
            ],
            borderColor: [
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(255, 99, 132, 1)',
                'rgba(153, 102, 255, 1)',
            ],
            borderWidth: 1,
        },
    ],
};

export const monthlyIncomeData = [1200, 1500, 2000, 2500, 1800, 3000]; // Monthly income data for the last 6 months

export const incomeChartLabels = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];

export const lowStockProductsDataVendor = [
    {
        productId: 'P001',
        productName: 'Samsung Galaxy S21',
        inStock: 1,  // Reduced stock
        vendorName: 'Tech World Inc.',
        vendorEmail: 'vendor1@techworld.com'
    },
    {
        productId: 'P005',
        productName: 'Sony WH-1000XM5 Headphones',
        inStock: 2,  // Reduced stock
        vendorName: 'Audio Gear Pro',
        vendorEmail: 'vendor5@audiogearpro.com'
    }
];

export const totalOrdersCountVendor = 20; // Reduced total orders count

export const orderStatusDataVendor = {
    labels: ['Processing', 'Delivered', 'Canceled', 'Returned'],
    datasets: [
        {
            label: 'Order Status Distribution',
            data: [5, 10, 2, 3], // Example counts for each status with reduced numbers
            backgroundColor: [
                'rgba(255, 206, 86, 0.6)', // Processing
                'rgba(75, 192, 192, 0.6)', // Delivered
                'rgba(255, 99, 132, 0.6)',  // Canceled
                'rgba(153, 102, 255, 0.6)', // Returned
            ],
            borderColor: [
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(255, 99, 132, 1)',
                'rgba(153, 102, 255, 1)',
            ],
            borderWidth: 1,
        },
    ],
};

export const monthlyIncomeDataVendor = [300, 500, 800, 1000, 700, 1200]; // Reduced monthly income data for the last 6 months

export const incomeChartLabelsVendor = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
