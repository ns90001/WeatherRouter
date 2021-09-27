import React, {useRef} from 'react'
import './map.css'
import {withScriptjs, withGoogleMap, GoogleMap, Polyline} from "react-google-maps"

const Map = withScriptjs(withGoogleMap((props) =>
        <GoogleMap
            defaultZoom={7}
            defaultCenter={{ lat: 0, lng: 0}}
            ref={map => {
                const bounds = new window.google.maps.LatLngBounds();

                console.log(props);
                console.log(props.weatherRoute);

                for (let i = 0; i < props.route.length; i++) {
                    bounds.extend(props.route[i])
                }
                map && map.fitBounds(bounds)
            }}>
            <Polyline
                path={props.weatherRoute}
                options={{
                    geodesic: true,
                    strokeColor: '#FF0000'
                }}
            />
            <Polyline
                path={props.route}
                options={{
                    geodesic: true,
                    strokeColor: '#445DFA'
                }}
            />
        </GoogleMap>
    ))

export default Map;
