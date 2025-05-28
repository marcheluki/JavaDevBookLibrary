# Library Management System Frontend

This is the React application frontend for the full-stack Library Management System.
It consumes the API microservice to provide a web interface for managing books.


## Available Scripts

To run the full application (API and Frontend), use Docker Compose from the project root directory (see main README.md).

If you want to run the frontend independently for development (requires the API to be running separately), in the project directory, you can run:

### `npm start`

Runs the app in the development mode.
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.
You will also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

## Features

- **Book Management:**
  - View list of all books with a modern bookshelf-style interface
  - View detailed information for each book
  - Add new books through a modal form with validation
  - Edit existing books through a modal form:
    - Pre-filled with current book data
    - ISBN field is disabled to prevent changes
    - All other fields (title, author, year, copies) can be modified
  - Delete books with confirmation
- **Modern UI:**
  - Built with Material-UI components
  - Responsive design
  - Modal forms for add/edit operations
  - Snackbar notifications for operation feedback
  - Bookshelf-style list view with visual separation between books
  - Updated UI colors for header, buttons, and icons for a cohesive look.
  - Full-width background image on the Home page.
  - Dedicated Search page with real-time search results.
- **Real-time Updates:**
  - Automatic list refresh after add/update/delete operations
  - No page refresh required
  - Immediate feedback on operation success/failure
- **Error Handling:**
  - Form validation for required fields
  - Error alerts for API failures
  - Graceful handling of network issues

## Project Structure

```
frontend/
├── .babelrc
├── .gitignore
├── build/
├── Dockerfile
├── jest.config.js
├── node_modules/
├── nginx.conf
├── package.json
├── package-lock.json
├── public/
│   └── images/ # For static assets like the background image
├── src/
│   ├── components/
│   │   ├── BookList.tsx
│   │   ├── AddBookForm.tsx
│   │   ├── EditBookForm.tsx
│   │   ├── BookDetails.tsx
│   │   ├── Header.tsx
│   │   └── ErrorAlert.tsx
│   ├── hooks/ # Custom hooks (e.g., useDebounce)
│   ├── pages/ # Dedicated pages (e.g., SearchPage)
│   │   └── SearchPage.tsx
│   ├── services/   # API service layer
│   ├── config/     # Configuration files
│   └── App.tsx     # Main application component
└── tsconfig.json
```

## Development

The frontend is built with:
- React 18
- TypeScript
- Material-UI
- Axios for API communication
- React Router for navigation

## Docker

The frontend is containerized using Docker and served through Nginx. The Nginx configuration handles:
- Serving the static React build files
- Proxying API requests to the backend service
- Handling client-side routing
