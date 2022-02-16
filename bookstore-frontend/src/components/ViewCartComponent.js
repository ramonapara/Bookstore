import React from 'react'
import axios from 'axios'
import { useNavigate } from 'react-router-dom';
import { withRouter } from 'react-router-dom';
import BooksComponent from './BooksComponent'
import { BrowserRouter as Router } from 'react-router-dom';
import { Routes ,Route, Link } from 'react-router-dom';
import {

    Button,
    InputGroup,

  } from "react-bootstrap";
import { Navigate } from 'react-router-dom';

  class ViewCartComponent extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],  
        };

    }

    componentDidMount() {
        console.log('ViewCartComponent');
        this.viewCart();
        
    }


    viewCart() {
        console.log("View cart");

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+localStorage.getItem('token')
        }
        axios.get("http://localhost:8080/api/bookcollection/orders/viewCart",{headers: headers})
            .then((response) => {
                this.setState({items: response.data.items})
            })
    }

    confirmOrder = () => {
        console.log("Confirm Order");
        console.log(localStorage.getItem('token'))

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+localStorage.getItem('token')
        }
        axios.put("http://localhost:8080/api/bookcollection/orders/confirmOrder", "",{headers: headers})
            .then(response => {
                console.log(response)
                this.setState({items: []})
                alert("Ai plasat o comanda!")
            })
    }

    render() {
        return (
            <div>
                <h1 className="text-center">Cos cumparaturi </h1>
                <table className="table table-striped">
                    <thead>
                        <tr>
                            <td> ISBN </td>
                            <td> Titlu </td>
                            <td> Pret </td>
                            <td> Cantitate </td>
                        </tr>
                    </thead>

                    <tbody>
                        {
                            this.state.items.map((items, index) => {
                                return (
                                <tr key={index}>
                                    <td>{items.isbn}</td>
                                    <td>{items.title}</td>
                                    <td>{items.price}</td>
                                    <td>{items.quantity}</td>
                                </tr>
                                );
                            })
                        }

                    </tbody>
                    </table>
                    <div style={{ float: "right" }}>
                    <Button
                        type="button"
                        variant="outline-info"
                        onClick={this.confirmOrder}
                        >
                        Confirma comanda
                    </Button>
                </div>
            </div>
        );
    }
}

export default ViewCartComponent;