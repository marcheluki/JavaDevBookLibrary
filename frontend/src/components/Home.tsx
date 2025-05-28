import React from 'react';
import { Container, Typography, Box } from '@mui/material';

const Home: React.FC = () => {
  return (
    <Box
      sx={{
        width: '100vw',            // ancho completo
        height: 'calc(100vh - 64px)', // alto completo menos header (64px)
        backgroundImage: 'url(/image/backgroundPhoto.png)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        textAlign: 'center',
        backgroundColor: 'rgba(255,255,255,0.5)',
      }}
    >
      <Container maxWidth="md">
        <Typography variant="h2" component="h1" gutterBottom sx={{ color: 'white' }}>
          Welcome to the Library App!
        </Typography>
        <Typography variant="h5" component="p" sx={{ color: 'white' }}>
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