import React from 'react';
import ToggleButton from '@material-ui/lab/ToggleButton';
import ToggleButtonGroup from '@material-ui/lab/ToggleButtonGroup';

function Toggle(props) {
    //const [alignment, setAlignment] = React.useState("left");

    const handleAlignment = (event, newAlignment) => {
        props.setAlignment(newAlignment);
    };

    return (
        <ToggleButtonGroup
            value={props.alignment}
            exclusive
            onChange={handleAlignment}
            className={"toggle"}
        >
            <ToggleButton value="F">
                <h2 className={"toggleText"}>Fº</h2>
            </ToggleButton>
            <ToggleButton value="C">
                <h2 className={"toggleText"}>Cº</h2>
            </ToggleButton>
        </ToggleButtonGroup>
    );
}

export default Toggle;