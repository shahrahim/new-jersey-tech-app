import logo from './logo.svg';
import './App.css';
import React, {useState, useEffect} from "react"
import axios from "axios"
import AppNavbar from './AppNavbar';
import { BrowserRouter as Router, Switch, Route, Link } from 'react-router-dom';
import Registration from "./Registration"
import ClassList from './ClassList';
import Home from "./Home";

const ClientProfiles = () => {

  const [clients, setClients] = useState([]);

  const fetchClients = () => {
    axios.get("http://localhost:8080/clients").then(res => {
      console.log(res);
      setClients(res.data);
    });
  }

  useEffect(() => {
    fetchClients();
  }, []);

  return clients.map((client, index) => {

    return (
      <div key={index}>
        <h1>{client.name}</h1>
        <p>{client.email}</p>
      </div>
    )
  })
};

function App() {
  return (
    <Router>
      <div>
        <AppNavbar />
        <Switch>
          <Route path="/" exact component={Home} />
          <Route path="/registration" exact component={Registration} />
          <Route path="/classList" exact component={ClassList} />
        </Switch>
      </div>
    </Router>
  );
}

export default App;
