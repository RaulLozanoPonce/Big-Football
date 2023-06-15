import React from 'react';
import ReactDOM from 'react-dom/client';
import './style.css';
import Season from './components/Season';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Season />
  </React.StrictMode>
);

reportWebVitals();