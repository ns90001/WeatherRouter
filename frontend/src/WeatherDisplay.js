import React, {useEffect} from "react";
import { geolocated } from "react-geolocated";

function WeatherDisplay(props) {

    useEffect(() => {
        props.userCoords.current = props.coords
    }, [props.coords])

    return !props.isGeolocationAvailable ? (
        <div>Your browser does not support Geolocation</div>
    ) : !props.isGeolocationEnabled ? (
        <div>Geolocation is not enabled</div>
    ) : props.coords ? (
        <div>
            <p>{props.coords.latitude}</p>
            <p>{props.coords.longitude}</p>
        </div>
    ) : (
        <div>Getting the location data&hellip; </div>
    );
}

export default geolocated({
    positionOptions: {
        enableHighAccuracy: false,
    },
    userDecisionTimeout: 5000,
})(WeatherDisplay);