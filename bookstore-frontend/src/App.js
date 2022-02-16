import logo from './logo.svg';
import './App.css';
import UserComponent from './components/UserComponent'
import BooksComponent from './components/BooksComponent'
import { BrowserRouter as Router } from 'react-router-dom';
import { Routes ,Route, Link } from 'react-router-dom';
import LoginComponent from './components/LoginComponent'
import ViewCartComponent from './components/ViewCartComponent'
import LogoutComponent from './components/LogoutComponent'
import ManagerComponent from './components/ManagerComponent'
import jwt from 'jwt-decode'


function App() {
  return (
    <div> 
      <Router> 
      <nav className="navbar navbar-expand-lg navbar-light fixed-top">
        <div className="container">
          <div className="collapse navbar-collapse" id="navbarTogglerDemo02">
            <ul className="navbar-nav ml-auto">
              <li hidden={localStorage.getItem('token') === null ? false : true} className="nav-item">
              <Link className="nav-link" to={"/login"}>Login</Link>
              </li>
              <li  hidden={localStorage.getItem('token') === null ? false : true} className="nav-item">
                <Link className="nav-link" to={"/sign-up"}>Sign up</Link>
              </li>
              <li hidden={localStorage.getItem('token') === null ? true : false} className="nav-item">
              <Link className="nav-link" to={"/viewCart"}>Vezi cos</Link>
              </li>
              <li className="nav-item">
              <Link className="nav-link" to={"/"}>Carti</Link>
              </li>
              <li hidden={localStorage.getItem('token') === null ? true : false} className="nav-item">
                <Link className="nav-link" to={"/logout"}>Logout</Link>
              </li>
              <li hidden={
                  localStorage.getItem('role') !== null  ? false : true} className="nav-item">
                <Link className="nav-link" to={"/manager"}>Manager</Link>
              </li>
            </ul>
          </div>
        </div>
      </nav>

        <div className="container">
            <div className="container">
              <Routes> http://localhost:3000/
                  <Route path='/' element={<BooksComponent/>} />
                  <Route path='/users' element={<UserComponent/>} />
                  <Route path='/login' element={<LoginComponent/>} />
                  <Route path='/viewCart' element={<ViewCartComponent/>} />
                  <Route path='/logout' element={<LogoutComponent/>} />
                  <Route path='/manager' element={<ManagerComponent/>} />
              </Routes>
            </div>
        </div>
      </Router>
    </div>
  );
}

export default App;
