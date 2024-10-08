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
