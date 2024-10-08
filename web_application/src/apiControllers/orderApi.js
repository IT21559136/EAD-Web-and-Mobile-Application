// orderApi.js

import axios from 'axios';

export const ordersData = [
    {
        id: "6700e1f1b22d35f5e90776bc",
        customerId: null,
        items: [
            { productId: "66fe5526e91963f6c3b043eb", quantity: 5, vendorEmail: "lokuge@gmail.com", orderItemStatus: "New" },
            { productId: "66fe5526e91966f6c3b043eb", quantity: 10, vendorEmail: "nisal@gmail.com", orderItemStatus: "New" }
        ],
        status: "Processing",
        orderDate: "2024-10-05T06:51:29.329Z",
        customerNote: "Hello please quickly deliver"
    },
    {
        id: "6700e78ca44e24f7c19c5429",
        customerId: null,
        items: [
            { productId: "66fe5526e91963f6c3b043eb", quantity: 2, vendorEmail: "lokuge@gmail.com", orderItemStatus: "New" },
            { productId: "66fe5526e91963f6c3b063eb", quantity: 4, vendorEmail: "nipun@gmail.com", orderItemStatus: "Delivered" },
        ],
        status: "Partially Delivered",
        orderDate: "2024-10-05T07:15:22.783Z",
        customerNote: "Hello please quickly deliver"
    },
    {
        id: "6700ea8cef3612066ecf65d2",
        customerId: "66fe6f667e56b8a7ba693fc0",
        items: [
            { productId: "66fe5526e91963f6c3b043eb", quantity: 1, vendorEmail: "lokuge@gmail.com", orderItemStatus: "Cancelled" }
        ],
        status: "Cancelled",
        orderDate: "2024-10-05T07:28:11.468Z",
        customerNote: "Hello please quickly deliver"
    },
    {
        id: "670144e38809de3b698e321a",
        customerId: "66fe6f667e56b8a7ba693fc0",
        items: [
            { productId: "67012ae43e5c587b0537db13", quantity: 5, vendorEmail: "lokuge@gmail.com", orderItemStatus: "New" }
        ],
        status: "Processing",
        orderDate: "2024-10-05T13:53:39.6Z",
        customerNote: "Hello please quickly deliver"
    },
    {
        id: "670415a18465d8af11fdbb86",
        customerId: "670171b22b7bb4322fa765e1",
        items: [
            { productId: "67012be0325978d62e77c5b8", quantity: 10, vendorEmail: "lokuge@gmail.com", orderItemStatus: "New" }
        ],
        status: "Processing",
        orderDate: "2024-10-07T17:08:48.782Z",
        customerNote: "Hello please quickly deliver"
    },
    {
        id: "67042e19f030040289c02fb5",
        customerId: "67025e267a149523e54d1bf8",
        items: [
            { productId: "67012be0325978d62e77c5b8", quantity: 1, vendorEmail: "lokuge@gmail.com", orderItemStatus: "New" }
        ],
        status: "Processing",
        orderDate: "2024-10-07T18:53:13.358Z",
        customerNote: "Need immediately"
    },
    {
        id: "67042e2bf030040289c02fb7",
        customerId: "67025e267a149523e54d1bf8",
        items: [
            { productId: "67012bc6325978d62e77c5b7", quantity: 3, vendorEmail: "lokuge@gmail.com", orderItemStatus: "New" }
        ],
        status: "Processing",
        orderDate: "2024-10-07T18:53:31.678Z",
        customerNote: "Need immediately"
    },
    {
        id: "67042e3cf030040289c02fb9",
        customerId: "67025e267a149523e54d1bf8",
        items: [
            { productId: "67012bc6325978d62e77c5b7", quantity: 3, vendorEmail: "lokuge@gmail.com", orderItemStatus: "New" }
        ],
        status: "Processing",
        orderDate: "2024-10-07T18:53:47.896Z",
        customerNote: "Need immediately"
    },
    {
        id: "67043776624c3674653b0c69",
        customerId: null,
        items: [
            { productId: "67012bc6325978d62e77c5b7", quantity: 10, vendorEmail: "lokuge@gmail.com", orderItemStatus: "New" },
            { productId: "67012be0325978d62e77c5b8", quantity: 5, vendorEmail: "lokuge@gmail.com", orderItemStatus: "New" }
        ],
        status: "Partially Delivered",
        orderDate: "2024-10-07T19:33:09.834Z",
        customerNote: "no note"
    },
    {
        id: "67043937624c3674653b0c6c",
        customerId: null,
        items: [
            { productId: "67012bc6325978d62e77c5b7", quantity: 10, vendorEmail: "lokuge@gmail.com", orderItemStatus: "New" },
            { productId: "67012be0325978d62e77c5b8", quantity: 5, vendorEmail: "lokuge@gmail.com", orderItemStatus: "New" }
        ],
        status: "Processing",
        orderDate: "2024-10-07T19:40:39.434Z",
        customerNote: "no note"
    },
    {
        id: "67043937824c3674653b0c7a",
        customerId: null,
        items: [
            { productId: "67012ab6325978d62e77c5b1", quantity: 7, vendorEmail: "vendor@example.com", orderItemStatus: "New" }
        ],
        status: "Processing",
        orderDate: "2024-10-07T19:41:39.434Z",
        customerNote: "Please deliver"
    },
    {
        id: "67043937824c3674653b0c7b",
        customerId: null,
        items: [
            { productId: "67012ab6325978d62e77c5b1", quantity: 3, vendorEmail: "vendor2@example.com", orderItemStatus: "Shipped" }
        ],
        status: "Delivered",
        orderDate: "2024-10-07T19:42:39.434Z",
        customerNote: "Urgent"
    },
    {
        id: "67043937824c3674653b0c7c",
        customerId: null,
        items: [
            { productId: "67012bb6325978d62e77c5b2", quantity: 2, vendorEmail: "vendor3@example.com", orderItemStatus: "New" }
        ],
        status: "Processing",
        orderDate: "2024-10-07T19:43:39.434Z",
        customerNote: "Handle with care"
    }
];

// API response with axios interceptor
axios.interceptors.response.use((config) => {
    if (config.url === '/api/orders') {
        return { data: ordersData };
    }
    return config;
});


// API call to fetch orders
export const fetchOrders = async () => {
    try {
        return ordersData;
    } catch (error) {
        console.error('Error fetching orders:', error);
        throw error;
    }
};


// function to simulate canceling an order
export const cancelOrder = (orderId) => {
    return new Promise((resolve) => {
        console.log(`Cancel order: ${orderId}`);
        resolve(`Order ${orderId} canceled`);
    });
};

// function to simulate editing an order
export const editOrder = (orderId) => {
    return new Promise((resolve) => {
        console.log(`Edit order: ${orderId}`);
        resolve(`Order ${orderId} edited`);
    });
};

// function to simulate deleting an order
export const deleteOrder = (orderId) => {
    return new Promise((resolve) => {
        console.log(`Delete order: ${orderId}`);
        resolve(`Order ${orderId} deleted`);
    });
};

// function to simulate canceling an order item
export const cancelOrderItem = (orderId, itemId) => {
    return new Promise((resolve) => {
        console.log(`Cancel item: ${itemId} in order: ${orderId}`);
        resolve(`Item ${itemId} in order ${orderId} canceled`);
    });
};

// function to simulate editing an order item
export const editOrderItem = (orderId, itemId) => {
    return new Promise((resolve) => {
        console.log(`Edit item: ${itemId} in order: ${orderId}`);
        resolve(`Item ${itemId} in order ${orderId} edited`);
    });
};

// function to simulate setting an order item status
export const setOrderItemStatus = (orderId, itemId) => {
    return new Promise((resolve) => {
        console.log(`Set status for item: ${itemId} in order: ${orderId}`);
        resolve(`Status set for item ${itemId} in order ${orderId}`);
    });
};
