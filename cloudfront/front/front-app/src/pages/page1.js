import React from "react";
import "../App.css";
import logo from "../logo.svg";
import { Link } from "react-router-dom";

export default function page1() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <span>Page 1</span>
        <Link to="/page2" style={{ color: "white" }}>
          to page2
        </Link>
      </header>
    </div>
  );
}
