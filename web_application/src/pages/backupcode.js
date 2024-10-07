// mockApi.js

// Mock data for categories and products
let categories = [
    { id: 1, name: 'Electronics', status: 'Active' },
    { id: 2, name: 'Clothing', status: 'Active' },
    { id: 3, name: 'Home Appliances', status: 'Inactive' },
];

// Mock API calls using axios

export const fetchCategories = () => {
    return new Promise((resolve) => {
        setTimeout(() => resolve({ data: categories }), 500);
    });
};

export const addCategory = (newCategory) => {
    return new Promise((resolve) => {
        setTimeout(() => {
            const id = categories.length ? Math.max(categories.map(cat => cat.id)) + 1 : 1;
            const categoryToAdd = { id, ...newCategory };
            categories.push(categoryToAdd);
            resolve(categoryToAdd);
        }, 500);
    });
};

export const deleteCategory = (id) => {
    return new Promise((resolve) => {
        setTimeout(() => {
            categories = categories.filter(cat => cat.id !== id);
            resolve(id);
        }, 500);
    });
};

export const editCategory = (updatedCategory) => {
    return new Promise((resolve) => {
        setTimeout(() => {
            categories = categories.map(cat =>
                cat.id === updatedCategory.id ? updatedCategory : cat
            );
            resolve(updatedCategory);
        }, 500);
    });
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
