import React from 'react';
import ReactDOM from 'react-dom/client';
import './style.css';
import Competition from './components/Competition';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Competition />
  </React.StrictMode>
);

reportWebVitals();