import axios from 'axios';

// Create an Axios instance
const axiosInstance = axios.create();

// Add a request interceptor
axiosInstance.interceptors.request.use(
    (config) => {
        console.log("Idk");
        const token = localStorage.getItem('token'); // Get the token from localStorage
        console.log(token);
        if (token) {
            config.headers.Authorization = `Bearer ${token}`; // Add the token to headers
        }
        return config;
    },
    (error) => {
        return Promise.reject(error); // Handle errors
    }
);

export default axiosInstance;
