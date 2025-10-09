import './styles/globals.css';
import { createElement } from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { router } from 'Frontend/generated/routes.js';

function App() {
    return <RouterProvider router={router} />;
}

createRoot(document.getElementById('outlet')!).render(createElement(App));


