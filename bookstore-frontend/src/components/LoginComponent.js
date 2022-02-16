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
import jwt from 'jwt-decode'

class LoginComponent extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            username: '',
            password: '',
            isAuth: false,
            token: '',
            role: ''
        };

        this.login = this.login.bind(this)
        this.onChangeUsername = this.onChangeUsername.bind(this)
        this.onChangePassword = this.onChangePassword.bind(this)
        this.isUserLoggedIn = this.isUserLoggedIn.bind(this)
    }

    isUserLoggedIn() {
        if (this.state.isAuth === false) {
            return false;            
        }
        else return true;
    }

    login() {
        let xmls='<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"\
        xmlns:gs="http://spring.io/guides/gs-producing-web-service">\
        <soapenv:Header/>\
            <soapenv:Body>\
            <gs:authRequest>\
                <gs:username>'+this.state.username+'</gs:username>\
                <gs:parola>'+this.state.password+'</gs:parola>\
            </gs:authRequest>\
            </soapenv:Body>\
        </soapenv:Envelope>';
        //console.log("login: " + xmls);
        axios.post('http://localhost:8080/login', xmls, { 
            headers: {
                'Content-Type': 'text/xml',
                "Access-Control-Allow-Origin": "*"}})
            .then(res => res.data)
            .then(data => {
                var xml = new XMLParser().parseFromString(data);
                this.setState({token: xml.children[1].children[0].children[0].value})
                console.log(this.state.token);

                localStorage.setItem('token', this.state.token)
                this.setState({isAuth: true});


                const user = jwt(this.state.token);
                console.log("AUTENTIFICARE SUCCES DECODE: ");
                console.log(user.role);
                this.setState({role: user.role});

                /////////////////
                if (user.role == "MANAGER") {
                    console.log("SUNT MANAGEEEEEEEEEEEEEEEEEEEEEEEEEEEEER");
                    localStorage.setItem('role', this.state.role);
                }
                else {
                    console.log("SUNT ORICE ALTCEVA, NU SETEZ D");
                    localStorage.getItem("role")
                }
                window.location = "/" 
            })
            .catch(err=>{alert(err)});
   }

    onChangeUsername(event) {
        console.log("onChangeUsername: " + event.target.value)
        this.setState({username: event.target.value})
    }

    onChangePassword(event) {
        console.log("onChangePassword: " + event.target.value)
        this.setState({password: event.target.value})
    }
    
    render() {
        if (this.state.isAuth) {
            return <Navigate to = {{ pathname: "/" }} />;
        }
        return (

            <form>
                <br /> <br />
                <h3>Sign In</h3>
                <br />
    
                <div className="form-group">
                    <label>Username</label>
                     <input type="text" className="form-control" placeholder="Enter username" onChange={this.onChangeUsername}/>
                </div>
                <br />
    
                <div className="form-group">
                    <label>Password</label>
                    <input type="password" className="form-control" placeholder="Enter password" onChange={this.onChangePassword}/>
                </div>
                <br />
    
                <div className="form-group">
                    <div className="custom-control custom-checkbox">
                    </div>
                </div>
    
                <Button
                    type="button"
                    variant="outline-info"
                    onClick={this.login}
                >
                Submit
                </Button>
            </form>

        );
    }
}

export default LoginComponent;
