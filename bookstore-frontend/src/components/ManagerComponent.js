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

class ManagerComponent extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            ISBN: '',
            titlu: '',
            editura: '',
            anPublicare: '',
            genLiterar: '',
            stoc: 0, 
            pret: 0
        };

        this.onChangeISBN = this.onChangeISBN.bind(this)
        this.onChangeTitle = this.onChangeTitle.bind(this)
        this.onChangeEditura = this.onChangeEditura.bind(this)
        this.onChangeAnPublicare = this.onChangeAnPublicare.bind(this)
        this.onChangeGenLiterar = this.onChangeGenLiterar.bind(this)
        this.onChangeStoc = this.onChangeStoc.bind(this)
        this.onChangePret = this.onChangePret.bind(this)

    }

    onChangeISBN(event) {
        console.log("onChangeISBN: " + event.target.value)
        this.setState({ISBN: event.target.value})
    }

    onChangeTitle(event) {
        console.log("onChangeTitle: " + event.target.value)
        this.setState({titlu: event.target.value})
    }

    onChangeEditura(event) {
        console.log("onChangeEditura: " + event.target.value)
        this.setState({editura: event.target.value})
    }

    onChangeAnPublicare(event) {
        console.log("onChangeAnPublicare: " + event.target.value)
        this.setState({anPublicare: event.target.value})
    }

    onChangeStoc(event) {
        console.log("onChangeStoc: " + event.target.value)
        this.setState({stoc: event.target.value})
    }

    onChangeGenLiterar(event) {
        console.log("onChangeGenLiterar: " + event.target.value)
        this.setState({genLiterar: event.target.value})
    }

    onChangePret(event) {
        console.log("onChangePret: " + event.target.value)
        this.setState({pret: event.target.value})
    }

    addBook() {
        console.log("addBook")

        let book = {
            ISBN: this.state.ISBN,
            titlu: this.state.titlu,
            editura: this.state.editura,
            anPublicare: this.state.anPublicare,
            genLiterar: this.state.genLiterar,
            stoc: this.state.stoc,
            pret: this.state.pret
        }
        axios.post('http://localhost:8080/api/bookcollection/book"', book, { 
            headers: {
                'Content-Type': 'text/xml',
                "Access-Control-Allow-Origin": "*"}})
            .then(res => console.log(res))
            .catch(err=>{alert(err)});
   }

    componentDidMount() {
        
    }

    render() {
        return (
            <form>
                <br /> <br />
                <h3>Adauga carte</h3>
                <br />
    
                <div className="form-group">
                    <label>ISBN</label>
                     <input type="text" className="form-control" placeholder="Enter ISBN" onChange={this.onChangeISBN}/>
                </div>
                <br />
    
                <div className="form-group">
                    <label>Titlu</label>
                    <input type="text" className="form-control" placeholder="Enter title" onChange={this.onChangeTitle}/>
                </div>
                <br />

                <div className="form-group">
                    <label>Editura</label>
                    <input type="text" className="form-control" placeholder="Enter editura" onChange={this.onChangeEditura}/>
                </div>
                <br />

                <div className="form-group">
                    <label>An publicare</label>
                    <input type="number" className="form-control" placeholder="Enter an publicare" onChange={this.onChangeAnPublicare}/>
                </div>
                <br />


                <div className="form-group">
                    <label>Gen Literar</label>
                    <input type="text" className="form-control" placeholder="Enter gen literar" onChange={this.onChangeGenLiterar}/>
                </div>
                <br />

                <div className="form-group">
                    <label>Stoc</label>
                    <input type="number" className="form-control" placeholder="Enter stoc" onChange={this.onChangeStoc}/>
                </div>
                <br />

                <div className="form-group">
                    <label>Pret</label>
                    <input type="number" className="form-control" placeholder="Enter pret" onChange={this.onChangePret}/>
                </div>
                <br />

    
                <div className="form-group">
                    <div className="custom-control custom-checkbox">
                    </div>
                </div>
    
                <Button
                    type="button"
                    variant="outline-info"
                    onClick={this.addBook}
                >
                Add
                </Button>
            </form>
        );
    }
}

export default ManagerComponent;
