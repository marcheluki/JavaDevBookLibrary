import React, { useEffect, useCallback } from 'react';
import { Container, Typography, Paper, Box, CircularProgress, Button } from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';
import { Book, bookService } from '../services/bookService';
import { useApi } from '../hooks/useApi';
import ErrorAlert from './ErrorAlert';

const BookDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { data: book, loading, error, execute, reset } = useApi<Book>();

  const fetchBook = useCallback(async () => {
    try {
      await execute(() => bookService.getBookById(parseInt(id!)));
    } catch (err) {
      console.error('Error in fetchBook:', err);
    }
  }, [id, execute]);

  useEffect(() => {
    if (id) {
      fetchBook();
    }
  }, [id, fetchBook]);

  const handleEdit = () => {
    navigate(`/books/edit/${id}`);
  };

  if (loading) {
    return (
      <Container sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Container>
    );
  }

  if (!book) {
    return (
      <Container>
        <Typography variant="h6" sx={{ mt: 2 }}>
          Book not found
        </Typography>
      </Container>
    );
  }

  return (
    <Container>
      <Paper elevation={3} sx={{ p: 3, mt: 3 }}>
        <Typography variant="h4" gutterBottom>
          Book Details
        </Typography>
        <ErrorAlert error={error} onClose={reset} />
        <Box sx={{ mt: 2 }}>
          <Typography variant="h6" gutterBottom>
            Title: {book.title}
          </Typography>
          <Typography variant="h6" gutterBottom>
            Author: {book.author}
          </Typography>
          <Typography variant="h6" gutterBottom>
            ISBN: {book.isbn}
          </Typography>
          <Typography variant="h6" gutterBottom>
            Year: {book.year}
          </Typography>
          <Typography variant="h6" gutterBottom>
            Copies: {book.copies}
          </Typography>
        </Box>
        <Box sx={{ mt: 3 }}>
          <Button
            variant="contained"
            color="primary"
            onClick={handleEdit}
            sx={{ mr: 2 }}
          >
            Edit Book
          </Button>
          <Button
            variant="outlined"
            color="primary"
            onClick={() => navigate('/books')}
          >
            Back to List
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default BookDetails;