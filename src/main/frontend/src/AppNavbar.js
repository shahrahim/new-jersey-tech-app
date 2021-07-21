import React, {Component} from 'react';
import {Navbar, NavbarBrand} from 'reactstrap';
import {Link, NavLink} from 'react-router-dom';

export default class AppNavbar extends Component {
    constructor(props) {
        super(props);
        this.state = {isOpen: false};
        this.toggle = this.toggle.bind(this);
    }

    toggle() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }

    render() {
        return (
            <Navbar bg="white" color="dark" dark expand="md">
                <NavbarBrand>
                    <NavLink style={{color: 'white', textDecoration: 'none'}} tag={Link} to="/">Home</NavLink>
                </NavbarBrand>
                <NavbarBrand bg="white">
                    <NavLink style={{color: 'white', textDecoration: 'none'}} tag={Link} to="/registration">Registration</NavLink>
                </NavbarBrand>
                <NavbarBrand>
                    <NavLink style={{color: 'white', textDecoration: 'none'}} tag={Link} to="/classList">ClassList</NavLink>
                </NavbarBrand>
            </Navbar>
        );
    }
}