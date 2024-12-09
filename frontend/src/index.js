import React from 'react';
import ReactDOM from 'react-dom/client';  // שים לב לשימוש ב-react-dom/client
import App from './App';

const root = ReactDOM.createRoot(document.getElementById('root'));  // יצירת root חדש
root.render(<App />);  // רינדור של האפליקציה
