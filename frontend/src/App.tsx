import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { Container, CssBaseline } from '@mui/material';
import Header from './components/Header';
import BookList from './components/BookList';
import BookDetails from './components/BookDetails';
import BookForm from './components/BookForm';
import Home from './components/Home';

function App() {
  return (
    <Router>
      <CssBaseline />
      <Header />
      <Container sx={{ mt: 4 }}>
        <Routes>
          {/* Render Home component for the root path */}
          <Route path="/" element={<Home />} />
          <Route path="/books" element={<BookList />} />
          <Route path="/books/:id" element={<BookDetails />} />
          <Route path="/add" element={<BookForm />} />
          <Route path="/books/edit/:id" element={<BookForm />} />
          {/* Add a not found route if desired */}
        </Routes>
      </Container>
    </Router>
  );
}

export default App;
