import React from 'react'
import UserService from '../services/UserService'

class UserComponent extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            users: []
        }
    }

    componentDidMount() {
        
        UserService.getUsers().then((response) => {
            this.setState({ users: response.data })
        })
        console.log("Afisez lista de useri");
        console.log(this.state);
    }

    render() {
        return (
            <div>
                <h1 className="text-center">Users List </h1>
                <table className="table table-striped">
                    <thead>
                        <tr>
                            <td> User Id </td>
                            <td> User Username </td>
                            <td> User Role </td>
                        </tr>
                    </thead>

                    <tbody>
                        {
                            this.state.users.map(
                                users => 
                                <tr key={users.id}>
                                    <td>{users.username}</td>
                                    <td>{users.role}</td>
                                </tr>
                            )
                        }

                    </tbody>
                </table>
            </div>
        )
    }
}

export default UserComponent
