import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Customer from "./Customer";
import Account from "./Account";

function App() {
    return (
        <Router>
            <Routes>
                <Route path='/' exact element={<Customer />} />
                <Route path='/account/:id' exact element={<Account />} />
            </Routes>
        </Router>
    );
}

export default App;
