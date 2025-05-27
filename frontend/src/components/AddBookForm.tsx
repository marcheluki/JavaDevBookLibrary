import React, { useState } from 'react';
import { Container, Typography, TextField, Button, Box } from '@mui/material';
import { Book, bookService } from '../services/bookService';
import { useApi } from '../hooks/useApi';
import ErrorAlert from './ErrorAlert';

interface AddBookFormProps {
  onSubmit?: (bookData: Omit<Book, 'id'>) => Promise<void>;
  onCancel?: () => void;
}

const AddBookForm: React.FC<AddBookFormProps> = ({ onSubmit, onCancel }) => {
  const { loading: creating, error: createError, execute: createBookExecute, reset: resetCreate } = useApi<Book>();

  const [formData, setFormData] = useState<Omit<Book, 'id'>>({
    title: '',
    author: '',
    isbn: '',
    year: new Date().getFullYear(),
    copies: 1
  });

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
        await createBookExecute(() => bookService.createBook(formData));
        onSubmit(formData);
      }
    } catch (err) {
      console.error('Error in handleSubmit:', err);
    }
  };

  return (
    <Container maxWidth={onSubmit ? false : "sm"}>
      <Typography variant="h4" gutterBottom>
        Add New Book
      </Typography>
      <ErrorAlert error={createError} onClose={resetCreate} />
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
          disabled={creating}
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
          disabled={creating}
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
          disabled={creating}
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
          disabled={creating}
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
          disabled={creating}
        />
        <Button
          type="submit"
          variant="contained"
          color="primary"
          fullWidth
          sx={{ mt: 3 }}
          disabled={creating}
        >
          {creating ? 'Adding...' : 'Add Book'}
        </Button>
        {onCancel && (
          <Button
            variant="outlined"
            color="secondary"
            fullWidth
            sx={{ mt: 1 }}
            onClick={onCancel}
            disabled={creating}
          >
            Cancel
          </Button>
        )}
      </Box>
    </Container>
  );
};

export default AddBookForm;