import axios from 'axios';
import env from '../config/env';

const api = axios.create({
  baseURL: '/',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor for logging in development
if (env.environment === 'development') {
  api.interceptors.request.use(request => {
    console.log('Starting Request:', request);
    return request;
  });

  api.interceptors.response.use(
    response => {
      console.log('Response:', response);
      return response;
    },
    error => {
      console.error('Response Error:', error);
      return Promise.reject(error);
    }
  );
}

export default api; 