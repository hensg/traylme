import React from 'react';
import FormAndListUrls from './components/FormAndListUrls';
import Footer from './components/Footer';
import './App.css';

function App() {
  return (
    <div className="App">
      <div className="main">
        <h1>trayl.me</h1>
        <p>We make short powerful links</p>
        <FormAndListUrls />
      </div>
      <Footer/>
    </div>
  );
}

export default App;
