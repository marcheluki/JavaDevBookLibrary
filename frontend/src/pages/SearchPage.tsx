import React, { useState, useEffect } from 'react';
import { bookService, Book } from '../services/bookService';
import { useDebounce } from '../hooks/useDebounce';
import { 
  Container, 
  TextField, 
  Typography, 
  Card, 
  CardContent, 
  Grid, 
  CircularProgress,
  Box,
  InputAdornment,
  Paper,
  IconButton
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import BookIcon from '@mui/icons-material/Book';
import EditIcon from '@mui/icons-material/Edit';

const SearchPage: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState<Book[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const debouncedSearchQuery = useDebounce(searchQuery, 300);

  useEffect(() => {
    const performSearch = async () => {
      if (debouncedSearchQuery.trim()) {
        setIsLoading(true);
        try {
          const results = await bookService.searchBooks(debouncedSearchQuery);
          setSearchResults(results);
        } catch (error) {
          console.error('Error searching books:', error);
          setSearchResults([]);
        } finally {
          setIsLoading(false);
        }
      } else {
        setSearchResults([]);
      }
    };

    performSearch();
  }, [debouncedSearchQuery]);

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Typography variant="h3" component="h1" gutterBottom align="center" sx={{ mb: 4 }}>
        Search Books
      </Typography>
      
      <Paper elevation={3} sx={{ p: 3, mb: 4, borderRadius: 2 }}>
        <TextField
          fullWidth
          variant="outlined"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          placeholder="Search by title or author..."
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon color="primary" />
              </InputAdornment>
            ),
          }}
          sx={{
            '& .MuiOutlinedInput-root': {
              '&:hover fieldset': {
                borderColor: 'primary.main',
              },
            },
          }}
        />
      </Paper>

      {isLoading && (
        <Box display="flex" justifyContent="center" my={4}>
          <CircularProgress />
        </Box>
      )}

      {!isLoading && searchResults.length > 0 && (
        <Grid container spacing={3}>
          {searchResults.map((book) => (
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
                  <Typography 
                    variant="body2" 
                    color={book.copies > 0 ? 'success.main' : 'error.main'}
                    sx={{ fontWeight: 'bold' }}
                  >
                    {book.copies > 0 ? `${book.copies} copies available` : 'No copies available'}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}

      {!isLoading && searchQuery && searchResults.length === 0 && (
        <Box textAlign="center" py={4}>
          <Typography variant="h6" color="textSecondary">
            No books found matching your search.
          </Typography>
        </Box>
      )}
    </Container>
  );
};

export default SearchPage; 