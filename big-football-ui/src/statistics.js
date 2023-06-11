import React from 'react';
import ReactDOM from 'react-dom/client';
import './statistics.css';
import Statistics from './components/Statistics';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Statistics />
  </React.StrictMode>
);

reportWebVitals();