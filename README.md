# WeatherRouter

Weather Router is a Java and React web-app built to improve driving safety. We aim to increase drivers’ awareness of the weather while on the road and suggest safe driving routes to prevent driving in dangerous conditions. Our app is localized to Rhode Island -- but can be extended to entire US with a larger dataset.

## How to Build and Run

First, ```cd``` into the "frontend" directory of this project. From there the command ```npm start ``` should
launch the front-end. (Note: you may need to run ```npm install``` before this).

After this, you will need to download and move the "maps.sqlite3" file from this link:
https://drive.google.com/drive/folders/152pf-zIwtlT9aOAR22bECXPvtKSyjIOf?usp=sharing
into the directory data/maps

Next, open another terminal and execute the command ```.\run --gui``` into the main project directory.
After it runs, the program will start up a REPL. Enter ```map data/maps/maps.sqlite3``` and wait for the
REPL to print ```map set to data/maps/maps.sqlite3```.

Now you may interact with the program through the GUI!
