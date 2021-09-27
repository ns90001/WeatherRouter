import './App.css';
import React, {useEffect, useRef} from "react";
import Map from "./Map";
import {Image, WbCloudy, WbSunny} from "@material-ui/icons";
import { mdiWeatherPouring, mdiWeatherSnowy, mdiWeatherLightning, mdiWeatherFog} from '@mdi/js';
import {Icon} from "@mdi/react";
import {colors, rgbToHex} from "@material-ui/core";


function Results(props) {

    const formattedRoute = useRef([]);
    const formattedWeatherRoute = useRef([]);
    const weatherData = useRef([]);
    const newWeatherData = useRef([]);

    const newWeatherText = useRef("");
    const weatherText = useRef("");

    const displayNewWeather = useRef(null);

    const newWeatherBtn = useRef(null);

    const weatherTiles = useRef([]);
    const newWeatherTiles = useRef([]);

    let route;
    let weatherRoute;

    function convertToCelsius(temp) {
        return Math.round((temp - 32) * (5/9))
    }

    useEffect(() => {
        setTimeout(() => {
            props.setUnits("C")
            props.setUnits("F");
        } , 10);
    }, [])

    function openInGMaps(route) {
        let request_url = "https://www.google.com/maps/dir/?api=1&travelmode=driving&origin="
            + route.current[0].lat + "%2C" + route.current[0].lng
            + "&waypoints=";

        console.log(request_url);

        // generate url with directions param

        for (let i = 1; i < route.current.length - 1; i++) {
            request_url += route.current[i].lat + "%2C" + route.current[i].lng + "%7C"
        }
        request_url += "&destination="
            + route.current[route.current.length - 1].lat + "%2C" + route.current[route.current.length - 1].lng

        window.open(request_url);
    }

    useEffect(() => {

        console.log(props.location.state);

        route = props.location.state.nameValuePairs.normal_route;
        weatherRoute = props.location.state.nameValuePairs.weather_route;
        weatherData.current = [];
        weatherTiles.current = [];
        newWeatherTiles.current = [];
        formattedRoute.current = [];
        formattedWeatherRoute.current = [];

        for (let i = 0; i < route.length; i++) {
            const newNode = {
                lat: parseFloat(route[i].nameValuePairs.coords[0]),
                lng: parseFloat(route[i].nameValuePairs.coords[1])
            }
            formattedRoute.current.push(newNode);

            weatherData.current.push(
                {
                    temp: route[i].nameValuePairs.weather.nameValuePairs["temp"],
                    forecast: route[i].nameValuePairs.weather.nameValuePairs["forecast"],
                    wind: route[i].nameValuePairs.weather.nameValuePairs["wind"],
                    icon: route[i].nameValuePairs.weather.nameValuePairs["icon"]
                }
            )

            let temperature = parseFloat(route[i].nameValuePairs.weather.nameValuePairs["temp"]);

            if (props.unit == "F") {
                temperature = convertToCelsius(temperature);
            }

            let unitText = "ºF";

            if (props.unit == "F") {
                unitText = "ºC";
            }

            let forecast = route[i].nameValuePairs.weather.nameValuePairs["forecast"];
            let icon = <WbCloudy></WbCloudy>;

            console.log(forecast)

            if (forecast != undefined) {
                if (forecast.toString().toLowerCase().includes("snow")) {
                    icon = <Icon className="weatherIcon" path={mdiWeatherSnowy} size={1}/>
                } else if (forecast.toString().toLowerCase().includes("storm")) {
                    icon = <Icon className="weatherIcon" path={mdiWeatherLightning} size={1}/>
                } else if (forecast.toString().toLowerCase().includes("fog") || forecast.toString().includes("haze")) {
                    icon = <Icon className="weatherIcon" path={mdiWeatherFog} size={1}/>
                } else if (forecast.toString().toLowerCase().includes("rain")) {
                    icon = <Icon className="weatherIcon" path={mdiWeatherPouring} size={1}/>
                } else if (forecast.toString().toLowerCase().includes("sun")) {
                    icon = <WbSunny></WbSunny>
                } else if (forecast.toString().toLowerCase().includes("cloud")) {
                    icon = <WbCloudy></WbCloudy>
                }
                if (i > 1 && i < route.length - 1) {
                    if (weatherData[i] != weatherData[i - 1]) {
                        weatherTiles.current.push(
                            <li className={"weatherTile"}>
                                {route[i].nameValuePairs.coords}
                                <p></p>
                                {temperature + " " + unitText}
                                {icon}
                                {forecast}
                            </li>
                        )
                    }
                } else {
                    weatherTiles.current.push(
                        <li className={"weatherTile"}>
                            {route[i].nameValuePairs.coords}
                            <p></p>
                            {temperature + " " + unitText}
                            {icon}
                            {forecast}
                        </li>)
                }
            }
        }

        if (weatherRoute != undefined) {
            for (let i = 0; i < weatherRoute.length; i++) {
                const newNode = {
                    lat: parseFloat(weatherRoute[i].nameValuePairs.coords[0]),
                    lng: parseFloat(weatherRoute[i].nameValuePairs.coords[1])
                }
                formattedWeatherRoute.current.push(newNode);
                    newWeatherData.current.push(
                        {
                            temp: weatherRoute[i].nameValuePairs.weather.nameValuePairs["temp"],
                            forecast: weatherRoute[i].nameValuePairs.weather.nameValuePairs["forecast"],
                            wind: weatherRoute[i].nameValuePairs.weather.nameValuePairs["wind"]
                        }
                    )

                let temperature = parseFloat(weatherRoute[i].nameValuePairs.weather.nameValuePairs["temp"]);

                if (props.unit == "F") {
                    temperature = convertToCelsius(temperature);
                }

                let unitText = "ºF";

                if (props.unit == "F") {
                    unitText = "ºC";
                }

                let forecast = weatherRoute[i].nameValuePairs.weather.nameValuePairs["forecast"];
                let icon = <WbCloudy></WbCloudy>;

                if(forecast != undefined) {
                    if (forecast.toString().toLowerCase().includes("snow")) {
                        icon = <Icon className="weatherIcon" path={mdiWeatherSnowy} size={1}/>
                    } else if (forecast.toString().toLowerCase().includes("storm")) {
                        icon = <Icon className="weatherIcon" path={mdiWeatherLightning} size={1}/>
                    } else if (forecast.toString().toLowerCase().includes("fog") || forecast.toString().includes("haze")) {
                        icon = <Icon className="weatherIcon" path={mdiWeatherFog} size={1}/>
                    } else if (forecast.toString().toLowerCase().includes("rain")) {
                        icon = <Icon className="weatherIcon" path={mdiWeatherPouring} size={1}/>
                    } else if (forecast.toString().toLowerCase().includes("sun")) {
                        icon = <WbSunny></WbSunny>
                    } else if (forecast.toString().toLowerCase().includes("cloud")) {
                        icon = <WbCloudy></WbCloudy>
                    }

                    if (i > 1 && i < weatherRoute.length - 1) {
                        if (newWeatherData[i] !== newWeatherData[i - 1]) {
                            newWeatherTiles.current.push(
                                <li className={"weatherTile"}>
                                    {weatherRoute[i].nameValuePairs.coords}
                                    <p></p>
                                    {temperature + " " + unitText}
                                    {icon}
                                    {forecast}
                                </li>
                            )
                        }
                    } else {
                        newWeatherTiles.current.push(
                            <li className={"weatherTile"}>
                                {weatherRoute[i].nameValuePairs.coords}
                                <p></p>
                                {temperature + " " + unitText}
                                {icon}
                                {forecast}
                            </li>
                        )
                    }
                }
            }

            console.log(formattedRoute);
            console.log(formattedWeatherRoute);

            newWeatherText.current = "Here's the weather along your adjusted (red) route:"
            weatherText.current = "Here's the weather along your unadjusted (blue) route:"
            displayNewWeather.current = newWeatherTiles.current;
            newWeatherBtn.current = <button className={"goBtn2"} onClick={() => {openInGMaps(formattedWeatherRoute)}}>Open Weather Adjusted (Red) Route in Google Maps</button>
        } else {
            newWeatherText.current = "Here's the weather along your route:"
        }

    }, [props.unit]);

    return (
        <div className="Results">
            <div className={"container"}>
                <div>
                    <Map
                        zoomLevel={10}
                        loadingElement={<div style={{ height: `100%` }} />}
                        containerElement={<div style={{ height: `400px` }} />}
                        mapElement={<div style={{ height: `100%` }} />}
                        googleMapURL='AIzaSyAGXweTXgAymoZ3eU43wy1YdKkiWeW6qpM'
                        route={formattedRoute.current}
                        weatherRoute={formattedWeatherRoute.current}>
                    </Map>
                    <div className={"googleMapsBtnContainer"}>
                        <button className={"goBtn2"} onClick={() => {openInGMaps(formattedRoute)}}>Open in Google Maps</button>
                        {newWeatherBtn.current}
                    </div>
                </div>
                <hr className={"divider"}></hr>
                <div>
                    <label className={"generalLabel"}>{newWeatherText.current}</label>
                    {displayNewWeather.current}
                    <hr className={"divider2"}></hr>
                    <label className={"generalLabel"}>{weatherText.current}</label>
                    <ul className={"stopsList"}>{weatherTiles.current}</ul>
                </div>
            </div>
        </div>
    );
}

export default Results;
