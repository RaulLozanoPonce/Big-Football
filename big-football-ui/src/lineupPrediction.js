import React from 'react';
import ReactDOM from 'react-dom/client';
import './style.css';
import LineupPrediction from './components/LineupPrediction';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <LineupPrediction />
  </React.StrictMode>
);

reportWebVitals();