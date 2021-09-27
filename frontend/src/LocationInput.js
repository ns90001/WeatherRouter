import React, {useState} from 'react';
import './App.css';
import PlacesAutocomplete, {
    geocodeByAddress,
    getLatLng,
} from 'react-places-autocomplete';

function LocationInput(props) {

    function handleChange(newAddress) {
        props.setVal(newAddress);

    };

    function handleSelect(newAddress) {
        props.setVal(newAddress);
        geocodeByAddress(newAddress)
            .then(results => getLatLng(results[0]))
            .then(latLng => {
                props.toUpdate.current = {
                    address: newAddress,
                    coords: [latLng.lat, latLng.lng]
                }
                console.log('Success', props.toUpdate.current)
            })
            .catch(error => {
                props.setVal("");
                console.error('Error', error)
            });
    };

    const searchOptions = {
        location: new window.google.maps.LatLng(41.731744, -71.564708),
        radius: 50000,
        types: ['address']
    }

    return (
        <PlacesAutocomplete
            value={props.val}
            onChange={handleChange}
            onSelect={handleSelect}
            className={"locationInput"}
            searchOptions={searchOptions}
        >
            {({ getInputProps, suggestions, getSuggestionItemProps, loading }) => (
                <div>
                    <input
                        {...getInputProps({
                            placeholder: props.placeholder,
                            className: 'location-search-input',
                        })}
                    />
                    <div className="autocomplete-dropdown-container">
                        {loading && <div>Loading...</div>}
                        {suggestions.map(suggestion => {
                            const className = suggestion.active
                                ? 'suggestion-item--active'
                                : 'suggestion-item';
                            const style = suggestion.active
                                ? { backgroundColor: 'lightblue', cursor: 'pointer' }
                                : { backgroundColor: '#ffffff', cursor: 'pointer' };
                            return (
                                <div
                                    {...getSuggestionItemProps(suggestion, {
                                        className,
                                        style,
                                    })}
                                >
                                    <span>{suggestion.description}</span>
                                </div>
                            );
                        })}
                    </div>
                </div>
            )}
        </PlacesAutocomplete>
    );
}

export default LocationInput;