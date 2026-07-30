import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor: Inject JWT Token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response Interceptor: Handle Global Errors (401, 409)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      // 401 Unauthorized - Token Expired or Invalid
      if (error.response.status === 401) {
        console.warn('Session expired or unauthorized. Redirecting to login...');
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login'; 
      }
      
      // 409 Conflict - Optimistic Locking (JPA @Version)
      if (error.response.status === 409) {
        console.warn('Data conflict detected.');
        alert('Cette ressource a été modifiée par un autre utilisateur. La page va se rafraîchir pour obtenir les dernières informations.');
        window.location.reload();
      }
    }
    return Promise.reject(error);
  }
);

export default api;
