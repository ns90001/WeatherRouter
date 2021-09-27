import './App.css';
import Menu from "./Menu";
import React, {useEffect, useRef, useState} from "react";

import {
    BrowserRouter as Router,
    Switch,
    Route,
    Redirect,
} from "react-router-dom";

import Results from "./Results";
import {Home} from "@material-ui/icons";
import Toggle from "./Toggle";

function App() {

  const [redirectToHome, setRedirectToHome]= useState(null);

  const [units, setUnits] = useState("F");

  function redirect() {
      setRedirectToHome(<Redirect to="/home" />)
      //to prevent reload from happening before redirect has finished
      setTimeout(() => {window.location.reload(false)}, 10);
  }

  return (
    <div className="App">
        {redirectToHome}
        <nav className={"navBar"}>
            <Home cursor={"pointer"} onClick={redirect} fontSize={"large"} className={"homeBtn"}></Home>
            <h1 cursor={"pointer"} onClick={redirect} className={"title"}>WEATHER ROUTER</h1>
            <Toggle alignment={units} setAlignment={setUnits} className={"toggle"}></Toggle>
        </nav>
        <Router>
            <Switch>
                <Route exact path="/" >
                    <Redirect to="/home" />
                </Route>
                <Route
                    path='/home'
                    render={(props) => (
                        <Menu {...props} unit={units} />
                    )}
                />
                <Route
                    path='/results'
                    render={(props) => (
                        <Results {...props} unit={units} setUnits={setUnits} />
                        )}
                />
            </Switch>
        </Router>
    </div>
  );
}

export default App;
