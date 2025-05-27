import React, { useEffect, useState, useCallback } from 'react';
import { Container, Typography, List, ListItem, Box, IconButton, CircularProgress, Button } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { Book, bookService } from '../services/bookService';
import { useApi } from '../hooks/useApi';
import ErrorAlert from './ErrorAlert';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import AddBookForm from './AddBookForm';
import EditBookForm from './EditBookForm';
import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';
import CloseIcon from '@mui/icons-material/Close';

const BookList: React.FC = () => {
  // We will manage books state locally now
  const [books, setBooks] = useState<Book[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  // Keep error state for fetching initial list and delete operations
  const [error, setError] = useState<string | null>(null);

  // Use useApi only for delete operation now
  const { execute: deleteBookExecute, error: deleteError, reset: resetDelete } = useApi<void>();

  // State for the Add Book modal
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  // State for the Edit Book modal
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingBookId, setEditingBookId] = useState<number | null>(null);

  // State for Snackbar success message
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

  // Fetch books on component mount
  useEffect(() => {
    fetchBooks();
  }, [fetchBooks]); // Added fetchBooks to dependency array as it's now wrapped in useCallback

  const handleDelete = async (id: number) => {
    try {
      await deleteBookExecute(async () => {
        await bookService.deleteBook(id);
      });
      // Update state directly after deletion
      setBooks(prevBooks => prevBooks.filter(book => book.id !== id));
      setSnackbarMessage('Book deleted successfully!');
      setSnackbarOpen(true);
    } catch (err) {
      console.error('Error in handleDelete:', err);
      // Error is handled by the useApi hook and displayed by ErrorAlert
    }
  };

  const handleEdit = (id: number) => {
    // Open the edit modal instead of navigating
    setEditingBookId(id);
    setIsEditModalOpen(true);
    // Reset errors related to BookList operations when opening modal
    setError(null);
    resetDelete(); // Also reset delete errors if any
  };

  // Handle opening and closing the Add Book modal
  const handleOpenAddModal = () => {
    setIsAddModalOpen(true);
    // Reset errors related to BookList operations when opening modal
    setError(null);
    resetDelete(); // Also reset delete errors if any
  };

  const handleCloseAddModal = () => {
    setIsAddModalOpen(false);
  };

  // Handle closing the Edit Book modal
  const handleCloseEditModal = () => {
    setIsEditModalOpen(false);
    setEditingBookId(null); // Clear the editing book ID
  };

  // Handle submitting the form in the Add Book modal
  const handleAddBookSubmit = async (bookData: Omit<Book, 'id'>) => {
    try {
      const newBook = await bookService.createBook(bookData);
      // Update state directly after adding
      setBooks(prevBooks => [...prevBooks, newBook]);
      setSnackbarMessage('Book added successfully!');
      setSnackbarOpen(true);
      handleCloseAddModal(); // Close the modal on success
    } catch (err) {
      console.error('Error adding book:', err);
      // Error is now handled and displayed by the BookForm component within the modal
    }
  };

  // Handle submitting the form in the Edit Book modal
  const handleEditBookSubmit = async (bookData: Book) => {
    try {
      const updatedBook = await bookService.updateBook(bookData.id!, bookData);
      // Update state directly after editing
      setBooks(prevBooks => prevBooks.map(book => book.id === updatedBook.id ? updatedBook : book));
      setSnackbarMessage('Book updated successfully!');
      setSnackbarOpen(true);
      handleCloseEditModal(); // Close the modal on success
    } catch (err) {
      console.error('Error updating book:', err);
      // Error is now handled and displayed by the BookForm component within the modal
    }
  };

  // Handle Snackbar close
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

  // Combine errors from initial fetching and deleting
  const currentError = error || deleteError;

  return (
    <Container sx={{ backgroundColor: '#ffffff', padding: 3 }}>
      <Typography variant="h4" gutterBottom>
        Book List
      </Typography>
      {/* Add Button to open Add Book Modal */}
      <Button variant="contained" sx={{ mb: 2 }} onClick={handleOpenAddModal}>
        Add New Book
      </Button>

      {/* Display errors from fetching or deleting */}
      {currentError && <ErrorAlert error={currentError} onClose={() => { setError(null); resetDelete(); }} />}
      
      {/* Bookshelf Styling */}
      <List sx={{ 
        border: '1px solid #d3d3d3', 
        borderRadius: '4px', 
        backgroundColor: '#f5f5dc', // Beige color for bookshelf background
        padding: 2
      }}>
        {books.length === 0 && !loading && !currentError ? (
          <Typography variant="h6" sx={{ textAlign: 'center' }}>No books available.</Typography>
        ) : (
          books?.map((book) => (
            <ListItem 
              key={book.id} 
              sx={
                {
                  borderBottom: '1px solid #d3d3d3', // Shelf line
                  '&:last-child': { borderBottom: 'none' },
                  paddingY: 1,
                  backgroundColor: '#ffffff', // White background for each book item
                  marginY: 1, // Space between books
                  borderRadius: '4px',
                  boxShadow: '0 1px 3px rgba(0,0,0,0.1)', // Subtle shadow for book effect
                  display: 'flex', // Use flex to align items
                  alignItems: 'center', // Vertically align items
                }
              }
            >
              <Box sx={{ flexGrow: 1 }}> {/* Allow text to take available space */}
                <Typography variant="h6">{book.title}</Typography>
                <Typography variant="body2" color="textSecondary">
                  by {book.author} ({book.year})
                </Typography>
              </Box>
              <Box>
                <IconButton onClick={() => handleEdit(book.id!)} color="primary">
                  <EditIcon />
                </IconButton>
                <IconButton onClick={() => handleDelete(book.id!)} color="error">
                  <DeleteIcon />
                </IconButton>
              </Box>
            </ListItem>
          ))
        )}
      </List>

      {/* Add Book Dialog/Modal */}
      <Dialog open={isAddModalOpen} onClose={handleCloseAddModal} fullWidth maxWidth="sm">
        <DialogTitle>
          Add New Book
          <IconButton
            aria-label="close"
            onClick={handleCloseAddModal}
            sx={{
              position: 'absolute',
              right: 8,
              top: 8,
              color: (theme) => theme.palette.grey[500],
            }}
          >
             <CloseIcon />
          </IconButton>
        </DialogTitle>
        <DialogContent>
          <AddBookForm onSubmit={handleAddBookSubmit} onCancel={handleCloseAddModal} />
        </DialogContent>
      </Dialog>

      {/* Edit Book Dialog/Modal */}
      <Dialog open={isEditModalOpen} onClose={handleCloseEditModal} fullWidth maxWidth="sm">
        <DialogTitle>
          Edit Book
          <IconButton
            aria-label="close"
            onClick={handleCloseEditModal}
            sx={{
              position: 'absolute',
              right: 8,
              top: 8,
              color: (theme) => theme.palette.grey[500],
            }}
          >
             <CloseIcon />
          </IconButton>
        </DialogTitle>
        <DialogContent>
          {/* Pass the book data to EditBookForm */}
          {editingBookId !== null && (
            <EditBookForm 
              book={books.find(book => book.id === editingBookId)} 
              onSubmit={handleEditBookSubmit} 
              onCancel={handleCloseEditModal} 
            />
          )}
        </DialogContent>
      </Dialog>

      {/* Success Snackbar */}
      <Snackbar open={snackbarOpen} autoHideDuration={6000} onClose={handleSnackbarClose}>
        <Alert onClose={handleSnackbarClose} severity="success" sx={{ width: '100%' }}>
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default BookList;