import React, { useEffect, useState } from 'react';
import { Container, Typography, TextField, Button, Box } from '@mui/material';
import { Book, bookService } from '../services/bookService';
import { useApi } from '../hooks/useApi';
import ErrorAlert from './ErrorAlert';

interface EditBookFormProps {
  book?: Book;
  onSubmit?: (bookData: Book) => Promise<void>;
  onCancel?: () => void;
}

const EditBookForm: React.FC<EditBookFormProps> = ({ book, onSubmit, onCancel }) => {
  const { loading: updating, error: updateError, execute: updateBookExecute, reset: resetUpdate } = useApi<Book>();

  const [formData, setFormData] = useState<Book>({
    title: '',
    author: '',
    isbn: '',
    year: new Date().getFullYear(),
    copies: 1
  });

  useEffect(() => {
    if (book) {
      setFormData(book);
    }
  }, [book]);

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
      if (onSubmit) {
        await updateBookExecute(() => bookService.updateBook(formData.id!, formData));
        onSubmit(formData);
      }
    } catch (err) {
      console.error('Error in handleSubmit:', err);
    }
  };

  const isLoading = updating;
  const currentError = updateError;

  return (
    <Container maxWidth={onSubmit ? false : "sm"}>
      <Typography variant="h4" gutterBottom>
        Edit Book
      </Typography>
      <ErrorAlert error={currentError} onClose={() => { resetUpdate(); }} />
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
          disabled={true}
          required
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
          {isLoading ? 'Updating...' : 'Update Book'}
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
      </Box>
    </Container>
  );
};

export default EditBookForm; 