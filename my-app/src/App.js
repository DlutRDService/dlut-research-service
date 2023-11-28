import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Home from './pages/home'; // Make sure the path to home.js is correct
import './App.css'
import Dashboard from './pages/Dashboard'

function App() {
  return (
    <Router>
    <Routes>
        <Route exact path="/" element={<Home />} />
        <Route path="/dashboard" component={<Dashboard />} />
        {/* 其他路由... */}
    </Routes>
  </Router>
  );
}

export default App;
