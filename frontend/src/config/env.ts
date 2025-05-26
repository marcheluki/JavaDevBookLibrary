interface EnvConfig {
  apiUrl: string;
  environment: string;
}

const env: EnvConfig = {
  apiUrl: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
  environment: process.env.REACT_APP_ENV || 'development',
};

export default env; 