import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Container, CssBaseline } from '@mui/material';
import Header from './components/Header';
import Home from './components/Home';
import BookList from './components/BookList';
import BookDetails from './components/BookDetails';
import EditBookForm from './components/EditBookForm';
import SearchPage from './pages/SearchPage';

function App() {
  return (
    <Router>
      <CssBaseline />
      <Header />
      {/* <Container sx={{ mt: 4 }}> */}
        <Routes>
          {/* Render Home component for the root path */}
          <Route path="/" element={<Home />} />
          <Route path="/books" element={<BookList />} />
          <Route path="/books/:id" element={<BookDetails />} />
          <Route path="/books/edit/:id" element={<EditBookForm />} />
          <Route path="/search" element={<SearchPage />} />
          {/* Add a not found route if desired */}
        </Routes>
      {/* </Container> */}
    </Router>
  );
}

export default App;
