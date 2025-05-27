import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import BookList from '../BookList';
import { bookService } from '../../services/bookService';
import { Book } from '../../services/bookService';

// Mock the bookService
jest.mock('../../services/bookService');

// Mock React Router's useNavigate
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
  // Add future flags to opt-in to v7 behavior
  UNSAFE_useFutureFlags: () => ({
    v7_startTransition: true,
    v7_relativeSplatPath: true
  })
}));

// Mock console.error to prevent error output in tests
const originalConsoleError = console.error;
beforeAll(() => {
  console.error = jest.fn();
});

afterAll(() => {
  console.error = originalConsoleError;
});

describe('BookList', () => {
  const mockBooks: Book[] = [
    { id: 1, title: 'Book 1', author: 'Author 1', year: 2020, isbn: '123', copies: 1 },
    { id: 2, title: 'Book 2', author: 'Author 2', year: 2021, isbn: '456', copies: 2 },
  ];

  beforeEach(() => {
    // Reset all mocks before each test
    jest.clearAllMocks();
    (bookService.getAllBooks as jest.Mock).mockResolvedValue(mockBooks);
    (bookService.deleteBook as jest.Mock).mockResolvedValue(undefined);
    (bookService.createBook as jest.Mock).mockResolvedValue({ id: 3, title: 'New Book', author: 'New Author', year: 2024, isbn: '789', copies: 1 });
    (bookService.updateBook as jest.Mock).mockResolvedValue({ ...mockBooks[0], title: 'Updated Book' });
  });

  const renderComponent = () => {
    return render(
      <BrowserRouter future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
        <BookList />
      </BrowserRouter>
    );
  };

  it('renders book list with bookshelf styling', async () => {
    renderComponent();

    // Check loading state
    expect(screen.getByRole('progressbar')).toBeInTheDocument();

    // Wait for first book to load
    await waitFor(() => {
      expect(screen.getByText('Book 1')).toBeInTheDocument();
    });

    // Check second book
    expect(screen.getByText('Book 2')).toBeInTheDocument();

    // Check for bookshelf styling
    expect(screen.getByText('by Author 1 (2020)')).toBeInTheDocument();
    expect(screen.getByText('by Author 2 (2021)')).toBeInTheDocument();
  });

  it('opens add book modal and creates new book', async () => {
    renderComponent();

    // Wait for books to load
    await waitFor(() => {
      expect(screen.getByText('Book 1')).toBeInTheDocument();
    });

    // Click add book button
    const addButton = screen.getByRole('button', { name: 'Add New Book' });
    fireEvent.click(addButton);

    // Wait for modal to open
    await waitFor(() => {
      expect(screen.getByRole('dialog')).toBeInTheDocument();
    });

    // Fill in the form
    fireEvent.change(screen.getByLabelText(/title/i), { target: { value: 'New Book' } });
    fireEvent.change(screen.getByLabelText(/author/i), { target: { value: 'New Author' } });
    fireEvent.change(screen.getByLabelText(/isbn/i), { target: { value: '789' } });
    fireEvent.change(screen.getByLabelText(/year/i), { target: { value: '2024' } });
    fireEvent.change(screen.getByLabelText(/copies/i), { target: { value: '1' } });

    // Submit the form
    const submitButton = screen.getByRole('button', { name: 'Add Book' });
    fireEvent.click(submitButton);

    // Verify create was called
    await waitFor(() => {
      expect(bookService.createBook).toHaveBeenCalledWith({
        title: 'New Book',
        author: 'New Author',
        isbn: '789',
        year: 2024,
        copies: 1
      });
    });

    // Verify success message
    await waitFor(() => {
      expect(screen.getByText('Book added successfully!')).toBeInTheDocument();
    });
  });

  it('opens edit book modal and updates book', async () => {
    renderComponent();

    // Wait for books to load
    await waitFor(() => {
      expect(screen.getByText('Book 1')).toBeInTheDocument();
    });

    // Click edit button
    const editButtons = screen.getAllByTestId('EditIcon');
    fireEvent.click(editButtons[0]);

    // Wait for modal to open and check pre-filled data
    await waitFor(() => {
      expect(screen.getByRole('dialog')).toBeInTheDocument();
    });

    expect(screen.getByDisplayValue('Book 1')).toBeInTheDocument();
    expect(screen.getByDisplayValue('Author 1')).toBeInTheDocument();
    expect(screen.getByDisplayValue('123')).toBeInTheDocument();

    // Update the title
    fireEvent.change(screen.getByLabelText(/title/i), { target: { value: 'Updated Book' } });

    // Submit the form
    const submitButton = screen.getByRole('button', { name: 'Update Book' });
    fireEvent.click(submitButton);

    // Verify update was called
    await waitFor(() => {
      expect(bookService.updateBook).toHaveBeenCalledWith(1, expect.objectContaining({
        title: 'Updated Book',
        author: 'Author 1',
        isbn: '123'
      }));
    });

    // Verify success message
    await waitFor(() => {
      expect(screen.getByText('Book updated successfully!')).toBeInTheDocument();
    });
  });

  it('handles delete operation with confirmation', async () => {
    renderComponent();

    // Wait for books to load
    await waitFor(() => {
      expect(screen.getByText('Book 1')).toBeInTheDocument();
    });

    // Click delete button
    const deleteButtons = screen.getAllByTestId('DeleteIcon');
    fireEvent.click(deleteButtons[0]);

    // Verify delete was called
    await waitFor(() => {
      expect(bookService.deleteBook).toHaveBeenCalledWith(1);
    });

    // Verify success message
    await waitFor(() => {
      expect(screen.getByText('Book deleted successfully!')).toBeInTheDocument();
    });
  });

  it('handles error state', async () => {
    const errorMessage = 'Failed to fetch books';
    (bookService.getAllBooks as jest.Mock).mockRejectedValue(new Error(errorMessage));

    renderComponent();

    // Wait for error to appear
    await waitFor(() => {
      expect(screen.getByRole('alert')).toHaveTextContent(errorMessage);
    });
  });
}); 