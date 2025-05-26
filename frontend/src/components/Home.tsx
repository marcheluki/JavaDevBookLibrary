import React from 'react';
import { Container, Typography, Box } from '@mui/material';

const Home: React.FC = () => {
  return (
    <Box
      sx={{
        minHeight: 'calc(100vh - 64px)', // Adjust based on your header height
        backgroundColor: '#ffffff', // Changed to white
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        textAlign: 'center',
        padding: 4,
      }}
    >
      <Container maxWidth="md">
        <Typography variant="h2" component="h1" gutterBottom>
          Welcome to the Library App!
        </Typography>
        <Typography variant="h5" component="p">
          Discover and manage your book collection with ease.
        </Typography>
        {/* You can add images of books here */}
        {/* For example: */}
        {/* <Box sx={{ mt: 4 }}>
          <img src="/path/to/your/book-image.png" alt="Books" style={{ maxWidth: '100%', height: 'auto' }} />
        </Box> */}
      </Container>
    </Box>
  );
};

export default Home; 