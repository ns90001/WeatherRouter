function changeNeighborsType() {
    let nCoords = document.querySelector("#n-target-coords");
    let nName = document.querySelector('#n-target-name');
    nName.style.display = nNameRadio.checked ? "block" : "none";
    nCoords.style.display = nCoordsRadio.checked ? "block" : "none";
}

function changeRadiusType() {
    let rCoords = document.querySelector("#r-target-coords");
    let rName = document.querySelector('#r-target-name');
    rName.style.display = rNameRadio.checked ? "block" : "none";
    rCoords.style.display = rCoordsRadio.checked ? "block" : "none";
}


const nNameRadio = document.querySelector("#neighbors-name");
const nCoordsRadio = document.querySelector("#neighbors-coords");
nNameRadio.addEventListener("click", changeNeighborsType);
nCoordsRadio.addEventListener("click", changeNeighborsType);
const rNameRadio = document.querySelector("#radius-name");
const rCoordsRadio = document.querySelector("#radius-coords");
rNameRadio.addEventListener("click", changeRadiusType);
rCoordsRadio.addEventListener("click", changeRadiusType);