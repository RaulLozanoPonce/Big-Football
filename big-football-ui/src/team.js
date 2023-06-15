import React from 'react';
import ReactDOM from 'react-dom/client';
import './style.css';
import Team from './components/Team';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Team />
  </React.StrictMode>
);

reportWebVitals();