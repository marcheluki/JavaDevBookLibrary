import React, { useEffect, useState, useCallback } from 'react';
import { 
  Container, 
  Typography, 
  Box, 
  IconButton, 
  CircularProgress, 
  Button,
  Grid,
  Card,
  CardContent,
  Dialog,
  DialogTitle,
  DialogContent,
  Snackbar,
  Alert
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import BookIcon from '@mui/icons-material/Book';
import { Book, bookService } from '../services/bookService';
import { useApi } from '../hooks/useApi';
import ErrorAlert from './ErrorAlert';
import AddBookForm from './AddBookForm';
import EditBookForm from './EditBookForm';

const BookList: React.FC = () => {
  const [books, setBooks] = useState<Book[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const { execute: deleteBookExecute, error: deleteError, reset: resetDelete } = useApi<void>();
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingBookId, setEditingBookId] = useState<number | null>(null);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');

  const fetchBooks = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await bookService.getAllBooks();
      setBooks(data);
    } catch (err) {
      console.error('Error fetching books:', err);
      setError('Failed to fetch books.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchBooks();
  }, [fetchBooks]);

  const handleDelete = async (id: number) => {
    try {
      await deleteBookExecute(async () => {
        await bookService.deleteBook(id);
      });
      setBooks(prevBooks => prevBooks.filter(book => book.id !== id));
      setSnackbarMessage('Book deleted successfully!');
      setSnackbarOpen(true);
    } catch (err) {
      console.error('Error in handleDelete:', err);
    }
  };

  const handleEdit = (id: number) => {
    setEditingBookId(id);
    setIsEditModalOpen(true);
    setError(null);
    resetDelete();
  };

  const handleOpenAddModal = () => {
    setIsAddModalOpen(true);
    setError(null);
    resetDelete();
  };

  const handleCloseAddModal = () => {
    setIsAddModalOpen(false);
  };

  const handleCloseEditModal = () => {
    setIsEditModalOpen(false);
    setEditingBookId(null);
  };

  const handleAddBookSubmit = async (bookData: Omit<Book, 'id'>) => {
    try {
      const newBook = await bookService.createBook(bookData);
      setBooks(prevBooks => [...prevBooks, newBook]);
      setSnackbarMessage('Book added successfully!');
      setSnackbarOpen(true);
      handleCloseAddModal();
    } catch (err) {
      console.error('Error adding book:', err);
    }
  };

  const handleEditBookSubmit = async (bookData: Book) => {
    try {
      const updatedBook = await bookService.updateBook(bookData.id!, bookData);
      setBooks(prevBooks => prevBooks.map(book => book.id === updatedBook.id ? updatedBook : book));
      setSnackbarMessage('Book updated successfully!');
      setSnackbarOpen(true);
      handleCloseEditModal();
    } catch (err) {
      console.error('Error updating book:', err);
    }
  };

  const handleSnackbarClose = (event?: React.SyntheticEvent | Event, reason?: string) => {
    if (reason === 'clickaway') {
      return;
    }
    setSnackbarOpen(false);
  };

  if (loading) {
    return (
      <Container sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Container>
    );
  }

  const currentError = error || deleteError;

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={4}>
        <Typography variant="h3" component="h1">
          Book Collection
        </Typography>
        <Button 
          variant="contained" 
          color="primary" 
          onClick={handleOpenAddModal}
          sx={{
            backgroundColor: '#185519',
            borderRadius: 2,
            textTransform: 'none',
            px: 3
          }}
        >
          Add New Book
        </Button>
      </Box>

      {currentError && (
        <ErrorAlert 
          error={currentError} 
          onClose={() => { setError(null); resetDelete(); }} 
        />
      )}

      {books.length === 0 && !loading && !currentError ? (
        <Typography variant="h6" align="center" color="textSecondary">
          No books available.
        </Typography>
      ) : (
        <Grid container spacing={3}>
          {books.map((book) => (
            <Grid item xs={12} sm={6} md={4} key={book.id}>
              <Card 
                elevation={2}
                sx={{
                  height: '100%',
                  display: 'flex',
                  flexDirection: 'column',
                  transition: 'transform 0.2s, box-shadow 0.2s',
                  '&:hover': {
                    transform: 'translateY(-4px)',
                    boxShadow: 4,
                  },
                }}
              >
                <CardContent>
                  <Box display="flex" alignItems="center" mb={2}>
                    <BookIcon sx={{ mr: 1, color: '#A0C878' }} />
                    <Typography variant="h6" component="h2" noWrap>
                      {book.title}
                    </Typography>
                  </Box>
                  <Typography color="textSecondary" gutterBottom>
                    By {book.author}
                  </Typography>
                  <Typography variant="body2" color="textSecondary" paragraph>
                    ISBN: {book.isbn}
                  </Typography>
                  <Typography variant="body2" color="textSecondary" paragraph>
                    Year: {book.year}
                  </Typography>
                  <Box display="flex" justifyContent="space-between" alignItems="center">
                    <Typography 
                      variant="body2" 
                      color={book.copies > 0 ? 'success.main' : 'error.main'}
                      sx={{ fontWeight: 'bold' }}
                    >
                      {book.copies > 0 ? `${book.copies} copies available` : 'No copies available'}
                    </Typography>
                    <Box>
                      <IconButton 
                        onClick={() => handleEdit(book.id!)} 
                        sx={{ color: '#597445' }}
                        size="small"
                      >
                        <EditIcon />
                      </IconButton>
                      <IconButton 
                        onClick={() => handleDelete(book.id!)} 
                        color="error"
                        size="small"
                      >
                        <DeleteIcon />
                      </IconButton>
                    </Box>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}

      {/* Add Book Modal */}
      <Dialog open={isAddModalOpen} onClose={handleCloseAddModal} maxWidth="sm" fullWidth>
        <DialogTitle>Add New Book</DialogTitle>
        <DialogContent>
          <AddBookForm onSubmit={handleAddBookSubmit} onCancel={handleCloseAddModal} />
        </DialogContent>
      </Dialog>

      {/* Edit Book Modal */}
      <Dialog open={isEditModalOpen} onClose={handleCloseEditModal} maxWidth="sm" fullWidth>
        <DialogTitle>Edit Book</DialogTitle>
        <DialogContent>
          {editingBookId && (
            <EditBookForm 
              book={books.find(b => b.id === editingBookId)}
              onSubmit={handleEditBookSubmit} 
              onCancel={handleCloseEditModal} 
            />
          )}
        </DialogContent>
      </Dialog>

      {/* Snackbar for success messages */}
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={6000}
        onClose={handleSnackbarClose}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert onClose={handleSnackbarClose} severity="success" sx={{ width: '100%' }}>
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default BookList;