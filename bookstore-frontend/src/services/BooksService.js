import axios from 'axios'

const BOOKS_REST_API_URL = 'http://localhost:8080/api/bookcollection/books';

class BooksService {

    getAllBooks(page, items_per_page) {
        axios.get(BOOKS_REST_API_URL+"?"+"page="+page+"&items_per_page="+items_per_page)
        .then(response => response.data)
        .then((data) => {
            
        })
    }
}

export default new BooksService();