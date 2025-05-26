import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import BookList from '../BookList';
import { bookService } from '../../services/bookService';

jest.mock('axios');
// Mock the bookService
jest.mock('../../services/bookService');

describe('BookList', () => {
  const mockBooks = [
    { id: 1, title: 'Book 1', author: 'Author 1', year: 2020, isbn: '123', copies: 1 },
    { id: 2, title: 'Book 2', author: 'Author 2', year: 2021, isbn: '456', copies: 2 },
  ];

  beforeEach(() => {
    (bookService.getAllBooks as jest.Mock).mockResolvedValue(mockBooks);
    (bookService.deleteBook as jest.Mock).mockResolvedValue(undefined);
  });

  it('renders book list', async () => {
    render(
      <BrowserRouter>
        <BookList />
      </BrowserRouter>
    );

    // Check loading state
    expect(screen.getByRole('progressbar')).toBeInTheDocument();

    // Wait for first book to load
    await waitFor(() => {
      expect(screen.getByText('Book 1')).toBeInTheDocument();
    });

    // Check second book
    expect(screen.getByText('Book 2')).toBeInTheDocument();
  });

  it('handles delete operation', async () => {
    render(
      <BrowserRouter>
        <BookList />
      </BrowserRouter>
    );

    // Wait for books to load
    await waitFor(() => {
      expect(screen.getByText('Book 1')).toBeInTheDocument();
    });

    // Click delete button for first book
    const deleteButtons = screen.getAllByRole('button', { name: /delete/i });
    fireEvent.click(deleteButtons[0]);

    // Verify delete was called
    await waitFor(() => {
      expect(bookService.deleteBook).toHaveBeenCalledWith(1);
    });
  });

  it('handles error state', async () => {
    const errorMessage = 'Failed to fetch books';
    (bookService.getAllBooks as jest.Mock).mockRejectedValue(new Error(errorMessage));

    render(
      <BrowserRouter>
        <BookList />
      </BrowserRouter>
    );

    // Wait for error to appear
    await waitFor(() => {
      expect(screen.getByText(errorMessage)).toBeInTheDocument();
    });
  });
}); 