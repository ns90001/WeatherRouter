import './App.css';
import Toggle from "./Toggle";
import {
    ArrowDropUp,
    Directions,
    Home,
    KeyboardArrowDown,
    KeyboardArrowUp,
    LocationOn,
    NearMe,
    WbSunny
} from "@material-ui/icons";
import {useRef, useState, useEffect} from 'react';
import LocationInput from "./LocationInput";
import DateTimePicker from 'react-datetime-picker/dist/entry.nostyle'
import {Redirect} from "react-router-dom";
import axios from "axios";

function Menu(props) {

    const [redirectToResultsPage, setRedirectToResultsPage] = useState(null);

    const stops = useRef([]);
    const [stopsList, setStopsList] = useState([]);

    const newStopDest = useRef({address: "", coords: []});
    const [newStopTime, setNewStopTime] = useState("");

    const [newStopText, setNewStopText] = useState("");
    const [startText, setStartText] = useState("");
    const [destText, setDestText] = useState("");

    // error messages
    const [newStopDestErrorText, setNewStopDestErrorText] = useState("");
    const [newStopTimeErrorText, setNewStopTimeErrorText] = useState("");
    const [startErrorText, setStartErrorText] = useState("");
    const [destErrorText, setDestErrorText] = useState("");
    const [timeErrorText, setTimeErrorText] = useState("");

    const start = useRef({address: "", coords: []});
    const dest = useRef({address: "", coords:[]});
    const [date, setDate] = useState(new Date());

    const [comfort, setComfort] = useState(3);
    const [reRoutes, setReRoutes] = useState(1);

    const [loadingText, setLoadingText] = useState("");

    const [route, setRoute] = useState({});

    const oneWeekFromNow = new Date();
    oneWeekFromNow.setDate(oneWeekFromNow.getDate() + 7);

    function addStop() {
        let errorPresent = false;
        if (newStopDest.current.coords.length == 0) {
            errorPresent = true;
            setNewStopDestErrorText("Must input a valid place")
        }
        if (newStopTime == "") {
            errorPresent = true;
            setNewStopTimeErrorText("Must input a valid time")
        }
        if (parseInt(newStopTime) < 0) {
            errorPresent = true;
            setNewStopTimeErrorText("Time must be positive")
        } else if (parseInt(newStopTime) > 168) {
            errorPresent = true;
            setNewStopTimeErrorText("Time is too large (must be under 168 hours)")
        }
        if (!errorPresent) {
            stops.current.push({
                lat: newStopDest.current.coords[0],
                lng: newStopDest.current.coords[1],
                time: newStopTime
            });
            setStopsList(stopsList.concat(<li className={"stopTile"}>{newStopDest.current.address}</li>))
            clearNewStopForm();
        }
    }

    function clearNewStopForm() {
        newStopDest.current = {address: "", coords: []};
        setNewStopText("");
        setNewStopTime("");
        setNewStopDestErrorText("")
        setNewStopTimeErrorText("")
    }

    function useCurrentLocation() {
        setStartText("Loading...");
        navigator.geolocation.getCurrentPosition(function(position) {
            console.log([position.coords.latitude, position.coords.longitude])
            start.current = {address: "current location", coords: [position.coords.latitude, position.coords.longitude]}
            setStartText("Your Location (" + position.coords.latitude + ", " + position.coords.longitude + ")");
        })
    }

    // calls routing algorithm and switches to next page
    function go() {

        //error handling
        let errorPresent  = false;
        if (date == null) {
            errorPresent = true;
            setTimeErrorText("Please enter a valid date and time");
        }
        if (start.current.coords.length === 0) {
            errorPresent = true;
            setStartErrorText("Please enter a valid address");
        }
        if (dest.current.coords.length === 0) {
            errorPresent = true;
            setDestErrorText("Please enter a valid address");
        }
        if(!errorPresent) {
            // run the algorithm

            setLoadingText("Loading...");
            console.log(stops.current);

            console.log("ReRoutes: " + reRoutes);

            const toSend = {
                startLat: start.current.coords[0],
                startLon: start.current.coords[1],
                destLat: dest.current.coords[0],
                destLon: dest.current.coords[1],
                time: date,
                stops: stops.current,
                reRoutes: reRoutes
            };

            let config = {
                headers: {
                    "Content-Type": "application/json",
                    'Access-Control-Allow-Origin': '*',
                }
            }

            axios.post(
                "http://localhost:4567/run",
                toSend,
                config
            )
                .then(response => {
                    console.log(response.data);
                    setRoute(response.data["route"]);
                })
                .catch(function (error) {
                    console.log(error);
                    setLoadingText("Something went wrong. Please try again");
                });

            console.log([dest.current.coords[0], dest.current.coords[1]]);
        }

    }

    useEffect(() => {
        console.log(Object.keys(route).length);
        if (Object.keys(route).length !== 0) {
            console.log("REDIRECTING")
            setRedirectToResultsPage(<Redirect to={{
                pathname: "/results",
                state: route,
                // state: {
                //     route: [
                //         {weather: {}, coords: [start.current.coords[0], start.current.coords[1]]},
                //         {weather: {}, coords: [dest.current.coords[0], dest.current.coords[1]]}
                //     ]
                // }
            }}/>)
            //to prevent reload from happening before redirect has finished
            setTimeout(() => {window.location.reload(false)}, 10);
        }
    }, [route])

    function clearForm() {
        setTimeErrorText("");
        setDate(null);
    }

    return (
        <div className="Menu">
            <div className={"weatherDisplay"}>
                <h1 className={"subtitle"}>Where will you travel today?</h1>
            </div>
            <h2 className={"loadingText"}>{loadingText}</h2>
            <div className={"container"}>
                <div className={"inputForm"}>
                    <label className={"inputLabel"}>Choose your starting point:</label>
                    <div className={"startingPoint"}>
                        <LocationInput val={startText} setVal={setStartText} toUpdate={start} className={"locationInput"} placeholder={'Search Places ...'}/>
                        <p className={"errorText2"}>{startErrorText}</p>
                        <button onClick={useCurrentLocation} className={"locationBtn"}><NearMe className={"locationIcon"} fontSize={"small"}/>Use current location</button>
                    </div>
                    <label className={"inputLabel"}>Choose your destination:</label>
                        <div>
                            <LocationInput val={destText} setVal={setDestText} toUpdate={dest} className={"locationInput"} placeholder={'Search Places ...'}/>
                            <p className={"errorText2"}>{destErrorText}</p>
                        </div>
                    <label className={"inputLabel"}>When is your trip: </label>
                    <div>
                        <DateTimePicker
                            className={"dateTimePicker"}
                            onChange={setDate}
                            value={date}
                            disableClock={true}
                            maxDate={oneWeekFromNow}
                            minDate={new Date()}/>
                        <p className={"errorText2"}>{timeErrorText}</p>
                    </div>
                    <div className={"reRoutes"}>
                        <div>
                            <label className={"inputLabel"}>How comfortable are you with driving in bad weather?</label>
                            <span style={{display: 'flex', justifyContent: 'space-evenly'}}><p className={"subText"}>1 (Highly uncomfortable)</p><p className={"subText"}>3 (Completely comfortable)</p></span>
                        </div>
                        <h2 className={"reRoutesNumber"}>{comfort}</h2>
                        <div className={"arrows"}>
                            <KeyboardArrowUp onClick={() => {
                                if (comfort < 3) {
                                    setReRoutes(reRoutes - 1)
                                    setComfort(comfort + 1);
                                    console.log(reRoutes);
                                }
                            }}></KeyboardArrowUp>
                            <KeyboardArrowDown onClick={() => {
                                if (comfort >= 2) {
                                    setReRoutes(reRoutes + 1)
                                    setComfort(comfort - 1);
                                    console.log(reRoutes);
                                }
                            }}></KeyboardArrowDown>
                        </div>
                    </div>
                </div>
                <hr className={"divider"}></hr>
                <div className={"stopsForm"}>
                    <label className={"generalLabel"}>Any stops along the way?</label>
                    <ul className={"stopsList"}>{stopsList}</ul>
                    <div className={"newStopForm"}>
                        <div className={"formContainer"}>
                            <LocationInput val={newStopText} setVal={setNewStopText} toUpdate={newStopDest} className={"locationInput"} placeholder={"Where is your stop?"}/>
                            <p className={"errorText"}>{newStopDestErrorText}</p>
                            <input type='number' min='1' max='10080' value={newStopTime} onChange={event => setNewStopTime(event.target.value)} placeholder={"For how long? (hours)"} />
                            <p className={"errorText"}>{newStopTimeErrorText}</p>
                            <span className={"twoBtns"}>
                                <button className={"clearBtn"} onClick={clearNewStopForm}>CLEAR</button>
                                <button className={"addBtn"} onClick={addStop}>ADD</button>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
            {redirectToResultsPage}
            <button className={"goBtn"} onClick={go}>GO</button>
        </div>
    );
}

export default Menu;
