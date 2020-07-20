import React from "react";
import logo from "./logo.svg";
import "./App.css";
import pages from "./pages";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

function App() {
  return (
    <Router>
      <Switch>
        <Route path="/page1" component={pages.page1} />
        <Route path="/page2" component={pages.page2} />
      </Switch>
    </Router>
  );
}

export default App;
