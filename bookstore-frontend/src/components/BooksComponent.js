import React from 'react'
import axios from 'axios'
import BooksService from '../services/BooksService'
import {

    Button,
    InputGroup,

  } from "react-bootstrap";


const BOOKS_REST_API_URL = 'http://localhost:8080/api/bookcollection/books';
let token;

class BooksComponent extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            books: [],
            page: 1,
            items_per_page: 3,
            gen: "",
            an: 2020,
            role: ""
            
        };
        this.onChangeGen = this.onChangeGen.bind(this)
        this.onChangeAn = this.onChangeAn.bind(this)
        this.filterBooks = this.filterBooks.bind(this)
    }

    componentDidMount() {
        this.setState({role: localStorage.getItem('token')})
        console.log('BooksComponent');
        this.getAllBooks(this.state.page, this.state.items_per_page);
    }

    getAllBooks(page, items_per_page) {
        console.log("call getAllBooks")
        axios.get(BOOKS_REST_API_URL+"?"+"page="+page+"&items_per_page="+items_per_page)
        .then((response) => {
            this.setState({books: response.data._embedded.bookList, page: page})
            console.log(this.state.books.length)
        })
        console.log(this.state.books.length)
    }

    onChangeGen(event) {
        console.log(event.target.value)
        this.setState({gen: event.target.value})
    }

    onChangeAn(event) {
        console.log(event.target.value)
        this.setState({an: event.target.value})
    }

    filterBooks() {
        console.log("filterBooks")
        axios.get(BOOKS_REST_API_URL+"/filter?"+"genre="+this.state.gen+"&year="+this.state.an)
        .then((response) => {
            console.log(response.data)
            this.setState({books: response.data, page:1})
        })
    }

    nextPage = () => {
        console.log("nextPage: " + this.state.page)
        if (
          this.state.books.length == this.state.items_per_page
        ) {
      //      this.setState({page: this.state.page + 1});
            this.getAllBooks(this.state.page+1, this.state.items_per_page);
        }
    };

    prevPage = () => {
        console.log("prevPage: "+this.state.page)
        if (this.state.page > 1) {

        //    this.setState({page: this.state.page - 1});
            this.getAllBooks(this.state.page-1, this.state.items_per_page);
        }
    };

    addToCart = (index) => {
        console.log("Add to cart");
        console.log(this.state.books[index].isbn);
        const item = {
            ISBN: this.state.books[index].isbn,
            title: this.state.books[index].titlu,
            price: this.state.books[index].pret,
            quantity: 1
        };
        const headers = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+localStorage.getItem('token')
        }
        axios.post("http://localhost:8080/api/bookcollection/orders/addToCart", item, {headers: headers})
            .then(response => {
                console.log(response)
            })
    }

    render() {
        return (
            <div>
                <h1 className="text-center">Lista carti </h1>
                <table className="table table-striped">
                    <thead>
                        <tr>
                            <td> ISBN </td>
                            <td> Titlu </td>
                            <td> Editura </td>
                            <td> An Publicare </td>
                            <td> Gen Literar </td>
                            <td> Stoc </td>
                            <td> Pret </td>
                        </tr>
                    </thead>

                    <tbody>
                        {
                            this.state.books.map((books, index) => {
                                return (
                                <tr key={index}>
                                    <td>{books.isbn}</td>
                                    <td>{books.titlu}</td>
                                    <td>{books.editura}</td>
                                    <td>{books.anPublicare}</td>
                                    <td>{books.genLiterar}</td>
                                    <td>{books.stoc}</td>
                                    <td>{books.pret}</td>
                                    <td>
                                    <Button
                                    type="button"
                                    variant="outline-info"
                                    disabled={localStorage.getItem('token') === null ? true : false}
                                    onClick={() => this.addToCart(index)}
                                    >
                                    Adauga in cos
                                    </Button>
                                    </td>
                                </tr>
                                );
                            })
                        }

                    </tbody>
                </table>
                <div style={{ float: "left" }}>
                    Showing Page {this.state.page}
                </div>
                <div style={{ float: "right" }}>
                    <Button
                      type="button"
                      variant="outline-info"
                      disabled={this.state.page === 0 ? true : false}
                      onClick={this.prevPage}
                    >
                    Prev Page
                    </Button>
                    <Button
                      type="button"
                      variant="outline-info"
                      disabled={this.state.items_per_page !== this.state.books.length ? true : false}
                      onClick={this.nextPage}
                    >
                    Next Page
                    </Button>
                        <br />
                        Cauta dupa gen sau an
                        <br />
                        <label> Gen Literar: </label>
                        <input type="text" value={this.state.gen} onChange={this.onChangeGen} />
                        <label> An Publicare </label>
                        <input type="number" value={this.state.an} onChange={this.onChangeAn} />
                        <Button
                            type="button"
                            variant="outline-info"
                            onClick={this.filterBooks}
                        >
                        Submit
                    </Button>
                    

                </div>
            </div>
        );
    }
}

export default BooksComponent;

