# cs0320 Term Project 2021

**Team Members:** Ian Gurland, Anoop Manjal, Joe Pate, Naveen Sharma

**Team Strengths and Weaknesses:**

Ian:
- Strength 1: Designing and implementing algorithms
- Strength 2: Thinking of ways to abstract classes to make them more extensible
- Strength 3: Planning deadlines to keep project on track
- Weakness 1: Debugging, finding small errors that corrupt project
- Weakness 2: Frontend design (not much of an artist)
- Weakness 3: Unwillingness to deviate from the plan at hand if things aren’t working out

Anoop:
- Strength 1: Good at debugging, testing, making sure edge cases are checked and making sure the program doesn't break on unexpected inputs
- Strength 2: Problem solving and coming up with algorithms to accomplish a certain task
- Strength 3: Using online resources to learn new concepts that can be used to implement an algorithm.
- Weakness 1: Frontend design and making the gui aesthetically pleasing
- Weakness 2: Making sure classes are fully extensible and reusable in a variety of cases
- Weakness 3: Design can sometimes be overly complicated and hard to understand

Joe:
- Strength 1: Good at communication and presentation of ideas/work and boiling down code so that it is explainable
- Strength 2: Implementing extensibility within projects and experience with using Java generics
- Strength 3: Good as a team player and willing to help out team members/explain concepts or talk things through. Willing to take extra time to do this when needed
- Weakness 1: Code can get overly complicated and convoluted 
- Weakness 2: Find it difficult to understand problems conceptually and wich can also lead to convoluted code
- Weakness 3: Creating comprehensive testing suites/expecting and testing for different edge cases

Naveen:
- Strength 1: Looking for new ways to solve / evade bugs
- Strength 2: Writing code which is extensible to new applications
- Strength 3: Writing efficient code
- Weakness 1: Testing for all edge cases
- Weakness 2: Writing readable code
- Weakness 3: I’m still learning how to use Java to its full potential


**Project Idea(s):** 

### Idea 1 (Laptop Finding App)

What problem are we attempting to solve?

  Oftentimes, when people are trying to buy new computer, they do not know where to look for new options, and usually revert to buying what they already “know”, even if there are certain features they don’t like about their current computer, or if it’s a bit higher cost than they’d like.

How will our project solve this problem?

  Our project will solve this problem by allowing the user to quickly search for their current computer, change certain characteristics from their current computer, and then search for new computers with those updated characteristics. The result will be a list of possible computers to choose from when buying, ordered by well of a match they are to your input characteristics.

Critical features we need to develop:

FEATURE 1: Quick search of current computer, allowing user to filter by computer brand and then input name of computer
- Why feature is being included: In order for the user to quickly find their current computer in our database, and retrieve their computer’s specifications.
- What will be most challenging about this feature: The hardest part about this feature will be somehow compiling all different computers that are available on the market along with their prices and specifications, and being able to access them through a database.
- Why feature is important from a user's perspective: The user will have a hard time using this feature if they cannot easily find their current computer on it, as the whole point is to edit specifications from your current computer to find new computers. that the user would like buying
	
FEATURE 2: Allow user to edit specifications of current computer to customize their search for a new computer, and let user rank the importance of each specification on a scale of 1-10 (using a slide bar)
- Why feature is being included: The point of the application is to find new computers that deviate from their current ones in the aspects the user desires, making the ability of the user to edit the specifications of the current computer vital.
- What will be most challenging about this feature: The hardest part about this feature will be the fact that some specifications for a computer are quantitative, while others are quantitative. Thus, there will have to be different mechanisms for adjusting different specifications. Also, we will have to deal with specifications not being found in the current computer.
- Why feature is important from a user’s perspective: Users will want to easily keep the parts of their computer that they did like, and also easily change what they didn’t like. Users will also have certain specifications of their computer they will want more than others, and they need to be able to note that easily.

FEATURE 3: Given user’s desired specifications and specification ranking, search database of computers and print computers that match user specifications most closely (the output computers’ specifications will be included in the list)
- Why feature is being included: The user needs to be able to see a list of the computers that most closely match their specifications, in order to decide which one they will buy.
- What will be most challenging about this feature: Figuring out how to store all the information about the computers in a database, and also how to scrape all of that information. Also, it will be hard to design an algorithm that takes into account both quantitative and qualitative factors when choosing matching computers.
- Why feature is important from a user’s perspective: Users don’t just want to see a list of computers that match their specifications best, they want to see what specifications are closest to their current computer, so all the data about the matching computers needs to be outputted.

FEATURE 4: Allow users to save computers they like once they have found them by searching
- Why feature is being included: Users  need to be able to access the information of computers they liked when searching, so that they can look for them on other websites while referencing the computer they are actually trying to buy.
- What will be most challenging about this feature: Creating a database that matches users to a list of computers that they are interested in; this will involve a complicated relationship between the large computer database and the user database.
- Why feature is important from a user’s perspective: If users can’t figure out what computers they were looking at earlier, they will have a bad user experience since searching for a computer will have led to nothing.


TA Approval (dlichen): Approved, contingent on the algorithm for searching for best matches being more developed. Be careful that the focus of this doesn't become scraping for information and instead is the matching algorithm.

### Idea 2 (Route Weather App)

What problem are we attempting to solve?

  Oftentimes, when people are taking a trip (whether it be by foot, by bicycle, by car, or by plane), the outdoor conditions can change along the way and negatively impact an individual’s schedule or ease of transportation. Over 21% of all vehicle crashes are weather-related. Imagine you are taking a road trip and are suddenly hit with a snow storm mid-way through your drive. It would be much easier if people had a way to look at the weather on a given route for a duration of time to make better judgement about their journey.

How will our project solve this problem?

  Our project will use weather data from the Internet to predict the conditions along a route over a period of time. It will allow users to choose from a variety of transportation types and create custom routes. The app will be able to suggest changes to the route to optimize time and safety from weather conditions. It can also suggest places along the route to stop if the conditions are too poor to continue, or suggest different days to travel when the conditions will be safer. One very useful feature of this app will be the ability to plan routes in advance. For example, if a user was planning a trip for next week, the app would be able to find the weather conditions along the route during this future timeframe. The goal of this web application would be to offer a tool for users to be better informed about a trip they are making so they can prioritize their safety.
	
Critical features we need to develop:

FEATURE 1: Route Interface
- Why feature is being included: Users should be able to map their route on the web-app so the app can analyse the weather along this route. The interface will also allow users to choose from a number of transportation options (car, walk, public transportation)
- What will be most challenging about this feature: The greatest challenge with this feature will be to integrate an existing map service (ex. Google Maps)
- Why feature is important from a user’s perspective: This interface will be extremely important as it allows the user to create a custom route that they will be travelling upon so the app can suggest potential changes and inform the user about the conditions along the way.

FEATURE 2: Gathering weather data along route
- Why feature is being included: This feature is benign included to provide users information about the route they will be travelling on. They will be provided with a map of the route with weather data plotted along the way.
- What will be most challenging about this feature: We will have to figure out how frequently to gather weather data. This frequency will change with the transportation method. Also, we will need to find a way to scrape weather data from an existing online source.
- Why feature is important from a user’s perspective: This feature is important because users should be able to receive reliable data for the route they entered into the app. The main functionality of this app will be this data gathering system.

FEATURE 3: Route change suggestions
- Why feature is being included: In case the user is trying to follow a route that is potentially dangerous, our app will be able to provide potential changes to increase the safety of their route.
- What will be most challenging about this feature: The most challenging thing about this feature will be to figure which weather conditions are dangerous and which are not. Also we will have to figure out how much to prioritize safety from weather, and overall travel times, among other factors.
- Why feature is important from a user’s perspective: This feature is very important, since it can possibly prevent users from taking a dangerous route to their destination. We want our app to be able to warn and help users make safer travel choices.

TA Approval (dlichen): Approved. Be careful that you don't focus too much on just gathering weather data and you shouldn't offset the routing to the google maps api -- you guys should have the algorithm that makes the routing and changes based on weather. 

### Idea 3 (Investing App)

What problem are we attempting to solve?

  In recent times the popularity of retail investing has boomed. Yet, the financial world is often very complicated and hard to get into. People are often overwhelmed by all the info, and either abandon retail investing all together, or end up making uninformed financial decisions. Simply put, it is hard to find clear, understandable information with which one can make sound informed decisions.

How will our project solve this problem?

  This project will attempt to provide users with critical financial information in several ways. Through a clean interface, and understandable easy to read graphs the app will present the user with a stock exhibiting unusual activity, while also allowing the user to search any stock and see some level of technical analysis. Moreover, it will provide up to date information on the benefits and disadvantages of different brokerage firms, as well as up to date news on restrictions or new rules that are oftentimes hard to find. Hopefully, with this information a user can find investing less daunting, and come back to the app when they want to do some more research on a certain stock as well. (Note: this app is not meant to provide financial advice, it is simply meant to provide information upon which the user can make a decision).

Critical features we need to develop:

FEATURE 1: Highlight stocks with unusual trading activity and pull up articles discussing the possible causes of such activity
- Why feature is being included: This feature is being included so the burden of research can be taken off the user and put onto the app. If the user has a 5 minute break, they can pull up the app and see what is going on in certain stocks, read articles regarding the behaviour, and then proceed to make an informed decision on what to do next.
- What will be most challenging about this feature: There would be two challenges to this feature. One is deciding how to measure “unusual activity” so only a few stocks fit into the category but not that it is so limiting that no stocks fit. The second more difficult part would be searching the internet for relevant articles and providing the links to the user. There would have to be a way to make sure only relevant articles enter the feed, because providing the user with irrelevant information would be useless.
- Why feature is important from a user’s perspective: This feature would allow the user to catch up to look at interesting moves in the market at that time. Moreover it would provide a nice interface to find articles, rather than having to search for the articles themselves. Ultimately, this takes the burden off the user, and thus makes investing a little easier.
	
FEATURE 2: Stock database with technical analysis
- Why feature is being included: This feature is being included so the user can search any stock they so wish, and see some baseline technical analysis (support and resistance lines). By displaying an easy to read graph with this analysis and other information, the user can make a decision to further pursue researching this stock. 
- What will be most challenging about this feature: The most challenging part of this feature would be gui and making sure the graph can be updated in near real time while also remaining aesthetically pleasing. Another difficult component would be implementing the technical analysis, and deciding how and what analysis to do.
- Why feature is important from a user’s perspective: This feature is useful for the user, because it provides a database for them to see movement and information on a stock they have particular interest in. The technical analysis makes this feature go one step further than most sites (yahoo finance) and provides the user with some additional info that they can use to make an informed financial decision.

FEATURE 3: A mechanic where a user can create a portfolio to record performance
- Why feature is being included: This feature is being implemented for users thinking of investing in a variety of stocks. Essentially, a user could make a basket of stock, with certain proportions and see what the average returns have been over some past time period. Additionally, the user can save the basket he created, and then come back at a later date, and see the performance of his selection since the date he created it. This provides a simulation of investing, and takes off the mathematical calculation one would have to do trying to add up the performances of several individual stocks.
- What will be most challenging about this feature: The most challenging part of this feature would be somehow storing user data so they can view their bin at any time, even if they haven’t gone on the app for a month. While the math could be challenging for the calculation it is definitely doable.
- Why feature is important from a user’s perspective: This feature is important because it allows the user to build and see how his ideas have performed, or would perform, without the financial implications. Thus this could be a learning experience for the user before he/she decides to invest with real money.

TA Approval (dlichen): Potentially approved if the focus of this application is algorithmic analysis of finding those unusual trading stocks and analysis of the stocks (not using an outside library for analysis) rather than just displaying data to the user.

Do not need to resubmit.

**Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 15)_

**4-Way Checkpoint:** _(Schedule for on or before April 5)_

**Adversary Checkpoint:** _(Schedule for on or before April 12 once you are assigned an adversary TA)_

## How to Build and Run

First, ```cd``` into the "frontend" directory of this project. From there the command ```npm start ``` should
launch the front-end. (Note: you may need to run ```npm install``` before this).

After this, you will need to download and move the "maps.sqlite3" file from this link:
https://drive.google.com/drive/folders/152pf-zIwtlT9aOAR22bECXPvtKSyjIOf?usp=sharing
into the directory data/maps

Next, open another terminal and execute the command ```.run --gui``` into the main project directory.
After it runs, the program will start up a REPL. Enter ```map data/maps/maps.sqlite3``` and wait for the
REPL to print ```map set to data/maps/maps.sqlite3```.

Now you may interact with the program through the GUI!
