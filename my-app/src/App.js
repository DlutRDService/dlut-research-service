import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import Home from './pages/home'; // Make sure the path to home.js is correct
import './App.css'
import Dashboard from './pages/Dashboard'

function App() {
  return (
    <Router>
    <div>
      <Switch>
        <Route exact path="/" component={Home} />
        <Route path="/dashboard" component={Dashboard} />
        {/* 其他路由... */}
      </Switch>
    </div>
  </Router>
  );
}

export default App;
