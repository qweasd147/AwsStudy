import React from "react";
import "../App.css";
import logo from "../logo.svg";
import { Link } from "react-router-dom";

export default function page2() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <span>Page 2</span>
        <Link to="/page1" style={{ color: "white" }}>
          to page1
        </Link>
      </header>
    </div>
  );
}
