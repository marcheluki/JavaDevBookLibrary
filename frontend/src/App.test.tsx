import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders library app title', () => {
  render(<App />);
  const titleElement = screen.getByRole('heading', { name: /Welcome to the Library App!/i });
  expect(titleElement).toBeInTheDocument();
});
