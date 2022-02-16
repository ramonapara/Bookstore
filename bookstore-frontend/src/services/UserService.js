import axios from 'axios'

const USERS_REST_API_URL = 'http://localhost:8080/api/bookcollection/users/';

class UserService {

    getUsers() {
        console.log("Get pe resursa");
        return axios.get(USERS_REST_API_URL);
    }
}

export default new UserService();