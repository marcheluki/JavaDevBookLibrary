import React, { useEffect, useState, useCallback } from 'react';
import { Container, Typography, TextField, Button, Box } from '@mui/material';
import { useNavigate, useParams } from 'react-router-dom';
import { Book, bookService } from '../services/bookService';
import { useApi } from '../hooks/useApi';
import ErrorAlert from './ErrorAlert';

// Define props for the BookForm component
interface BookFormProps {
  id?: string; // Added optional id prop for edit mode
  onSubmit?: (bookData: Omit<Book, 'id'> | Book) => Promise<void>; // Optional onSubmit for modal usage, allow Book type for edit
  onCancel?: () => void; // Optional onCancel for modal usage
}

const BookForm: React.FC<BookFormProps> = ({ onSubmit, onCancel }) => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>(); // id will be undefined in add modal scenario

  // Use separate useApi hooks for fetching, creating, and updating
  const { loading: fetching, error: fetchError, execute: fetchBookExecute, reset: resetFetch } = useApi<Book>();
  const { loading: creating, error: createError, execute: createBookExecute, reset: resetCreate } = useApi<Book>();
  const { loading: updating, error: updateError, execute: updateBookExecute, reset: resetUpdate } = useApi<Book>();

  const [formData, setFormData] = useState<Omit<Book, 'id'>>({
    title: '',
    author: '',
    isbn: '',
    year: new Date().getFullYear(),
    copies: 1
  });

  const fetchBookData = useCallback(async (bookId: number) => {
    try {
      const data = await fetchBookExecute(() => bookService.getBookById(bookId));
      if (data) {
        setFormData({
          title: data.title,
          author: data.author,
          isbn: data.isbn,
          year: data.year,
          copies: data.copies
        });
      }
    } catch (err) {
      console.error('Error in fetchBookData:', err);
    }
  }, [fetchBookExecute]);

  // Fetch book data if in edit mode (id is present in URL)
  useEffect(() => {
    if (id) {
      fetchBookData(parseInt(id));
    }
  }, [id, fetchBookData]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'year' || name === 'copies' ? parseInt(value) || 0 : value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (id) {
        // Edit mode: call update service and navigate
        await updateBookExecute(() => bookService.updateBook(parseInt(id), { ...formData, id: parseInt(id) }));
        navigate('/books');
      } else if (onSubmit) {
        // Add mode (modal): call onSubmit prop
        await createBookExecute(() => bookService.createBook(formData));
        onSubmit(formData); // Call the onSubmit prop passed from the parent
      } else {
         // This case should ideally not be reached if used correctly
         console.error("BookForm used in add mode without onSubmit prop.");
      }
    } catch (err) {
      console.error('Error in handleSubmit:', err);
    }
  };

  // Determine loading and error states based on mode
  const isLoading = fetching || creating || updating;
  const currentError = fetchError || createError || updateError;

  return (
    <Container maxWidth={onSubmit ? false : "sm"}> {/* Adjust container width for modal */}
      <Typography variant="h4" gutterBottom>
        {id ? 'Edit Book' : 'Add New Book'}
      </Typography>
      <ErrorAlert error={currentError} onClose={() => { resetFetch(); resetCreate(); resetUpdate(); }} />
      <Box component="form" onSubmit={handleSubmit} noValidate autoComplete="off" sx={{ mt: 2 }}>
        <TextField
          name="title"
          label="Title"
          variant="outlined"
          fullWidth
          margin="normal"
          value={formData.title}
          onChange={handleChange}
          required
          disabled={isLoading}
        />
        <TextField
          name="author"
          label="Author"
          variant="outlined"
          fullWidth
          margin="normal"
          value={formData.author}
          onChange={handleChange}
          required
          disabled={isLoading}
        />
        <TextField
          name="isbn"
          label="ISBN"
          variant="outlined"
          fullWidth
          margin="normal"
          value={formData.isbn}
          onChange={handleChange}
          required
          disabled={isLoading}
        />
        <TextField
          name="year"
          label="Year"
          variant="outlined"
          fullWidth
          margin="normal"
          type="number"
          value={formData.year}
          onChange={handleChange}
          required
          disabled={isLoading}
        />
        <TextField
          name="copies"
          label="Copies"
          variant="outlined"
          fullWidth
          margin="normal"
          type="number"
          value={formData.copies}
          onChange={handleChange}
          required
          disabled={isLoading}
        />
        <Button
          type="submit"
          variant="contained"
          color="primary"
          fullWidth
          sx={{ mt: 3 }}
          disabled={isLoading}
        >
          {isLoading ? 'Saving...' : id ? 'Update Book' : 'Add Book'}
        </Button>
        {onCancel && (
          <Button
            variant="outlined"
            color="secondary"
            fullWidth
            sx={{ mt: 1 }}
            onClick={onCancel}
            disabled={isLoading}
          >
            Cancel
          </Button>
        )}
        {!onSubmit && id && (
          <Button
            variant="outlined"
            color="primary"
            fullWidth
            sx={{ mt: 1 }}
            onClick={() => navigate('/books')}
            disabled={isLoading}
          >
            Back to List
          </Button>
        )}
      </Box>
    </Container>
  );
};

export default BookForm;