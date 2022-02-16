import React from 'react'
import axios from 'axios'

import {

    Button,
    InputGroup,

  } from "react-bootstrap";
import XMLParser from 'react-xml-parser';
import { Navigate } from 'react-router-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import { Routes ,Route, Link } from 'react-router-dom';
import LoginComponent from './LoginComponent';

class LogoutComponent extends React.Component {
    constructor(props) {
        super(props)
        this.state = {

        };
    }

    componentDidMount() {
        console.log('LogoutComponent');
        localStorage.removeItem("token")
        localStorage.removeItem("role")
        window.location = "/login" 
    }

 
    render() {
        return (
            <div>
            <br />
            <br />
            <br />
            <li hidden={localStorage.getItem('token') === null ? true : false} className="nav-item">
                    <Link className="nav-link" to={"/login"}>Login</Link>
            </li>
      
              <div className="container">
                  <div className="container">
                    <Routes> http://localhost:3000/
                        <Route path='/login' element={<LoginComponent/>} />
                    </Routes>
                  </div>
              </div>
              </div>

 
        );
    }
}

export default LogoutComponent;
