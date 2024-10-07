// mockApi.js
import axios from 'axios';

// API calls using axios
export const fetchCategories = async () => {
    const response = await axios.get('/api/Category');
    return response.data;
};

export const addCategory = async (newCategory) => {
    const response = await axios.post('/api/Category', newCategory);
    return response.data;
};

export const editCategory = async (updatedCategory) => {
    const response = await axios.put(`/api/Category/${updatedCategory.id}`, updatedCategory);
    return response.data;
};

export const deleteCategory = async (id) => {
    await axios.delete(`/api/Category/${id}`);
    return id;
};



let products = [
    { id: 1, name: 'Smartphone', category: 'Electronics', price: 500, status: 'Active' },
    { id: 2, name: 'Refrigerator', category: 'Home Appliances', price: 1200, status: 'Inactive' },
    { id: 3, name: 'T-Shirt', category: 'Clothing', price: 20, status: 'Active' },
];

export const fetchProducts = () => {
    return new Promise((resolve) => {
        setTimeout(() => resolve({ data: products }), 500);
    });
};

export const addProduct = (newProduct) => {
    return new Promise((resolve) => {
        setTimeout(() => {
            const id = products.length ? Math.max(products.map(prod => prod.id)) + 1 : 1;
            const productToAdd = { id, ...newProduct };
            products.push(productToAdd);
            resolve(productToAdd);
        }, 500);
    });
};

export const editProduct = (updatedProduct) => {
    return new Promise((resolve) => {
        setTimeout(() => {
            products = products.map(prod =>
                prod.id === updatedProduct.id ? updatedProduct : prod
            );
            resolve(updatedProduct);
        }, 500);
    });
};

export const deleteProduct = (id) => {
    return new Promise((resolve) => {
        setTimeout(() => {
            products = products.filter(prod => prod.id !== id);
            resolve(id);
        }, 500);
    });
};
