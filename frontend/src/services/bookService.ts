import api from './api';

export interface Book {
  id?: number;
  title: string;
  author: string;
  isbn: string;
  year: number;
  copies: number;
}

export const bookService = {
  // Get all books
  getAllBooks: async (): Promise<Book[]> => {
    const response = await api.get('/api/books');
    return response.data;
  },

  // Get a single book by ID
  getBookById: async (id: number): Promise<Book> => {
    const response = await api.get(`/api/books/${id}`);
    return response.data;
  },

  // Create a new book
  createBook: async (book: Omit<Book, 'id'>): Promise<Book> => {
    const response = await api.post('/api/books', book);
    return response.data;
  },

  // Update an existing book
  updateBook: async (id: number, book: Book): Promise<Book> => {
    const response = await api.put(`/api/books/${id}`, book);
    return response.data;
  },

  // Delete a book
  deleteBook: async (id: number): Promise<void> => {
    await api.delete(`/api/books/${id}`);
  },

  // Search books
  searchBooks: async (query: string): Promise<Book[]> => {
    const response = await api.get(`/api/books/search?query=${encodeURIComponent(query)}`);
    return response.data;
  }
}; 