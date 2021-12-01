# VenU

## Table of Contents
1. [Overview](#Overview)
2. [Product Spec](#Product-Spec)
3. [Wireframes](#Wireframes)
4. [Schema](#Schema)
5. [Sprint Demos](#Sprint Demos)

## Overview
### Description
**VenU** is a location-based event finding android app that connects you to live events in your area. 

### App Evaluation
- **Category:** Entertainment
- **Mobile:** This app would be primarily developed for Android mobile devices.
- **Story:** Pulls a list of events within the user-provided location parameters and displays them to users sorted by Sports, Music, Theater. Users can then make mock purchases of tickets to the events and mark themselves as attending.
- **Market:** Any user is welcome to use this app as long as they meet with an event's age requirements.
- **Habit:** This app could be used as often as users would like, depending on their social life and interests.
- **Scope:** This app includes features for browsing events, using profiles to personalize user experience, and making mock purchases for events. It has the option to expand the account aspect for socialization with other users and other account features such as tracking history and using external OAUTH for registration. Another possible expansion includes making actual ticket purchases directly in the app through an external vendor API such as Eventbrite, but the app will not include any direct interaction with event hosts or venues, only mediated through existing APIs.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- [x] Retrieve a list of events from the Ticketmaster API.
- [ ] Filter events by radius
- [ ] Filter events by genre.
- [ ] Filter by user preferences.
- [x] Create a login and registration screen
- [x] Create a bottom navigation bar that allows log out.
- [x] Allow users to see their profile including a profile picture and biography.
- [x] Display achieveable badges
- [ ] Display followers.
- [ ] Purchase tickets directly in the app.

**Optional Nice-to-have Stories**

- Login via Google OAUTH
- Ability to add other uses as friends
- Connect with and message other users attending the same events as you
- Track confirmed event attendance and save to user's profile
- Work towards in-app achievements/rewards program based on event attendence

### 2. Screen Archetypes

* Login 
* Register - User signs up or logs into their account
   * Upon Download/Reopening of the application, the user is prompted to log in to gain access to the main feed.
* Main Feed Screen - Event listings
   * After login, the main feed defaults to list view and a list of local events is shown. At the top of the feed are events categories.
   * Tapping on a category will list events filtered by that category. 
   * Tapping on an event will lead to a detailed view of the event.
   * Going into map view will show local events highlighted on the map. with the ability to tap on the highlights to learn more and enter a detailed view.
* Maps Screen
   * Shows the events in the area on a map of the user's local area. Events are hightlighted by pins and clickable to get more details.
* Detailed View Screen
   * Provides users with detailed information about the event, including photos, location, etc. A link to buy tickets will also be on this screen.
* Search Screen.
   * Allows user to be able to enter their zip code to see events near their location.
* Profile Screen
   * Shows the user's profile, as well as past events, badges, and friend connections.
* Past Events Screen
   * Shows the user's past ticket purchases.
* Friend Screen
   * Shows the user's friend connections.
   * Tapping on a profile will direct the user to the full profile.
* Badges Screen
   * Shows the user's special cosmetic awards gained from attending events.


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home
* Search
* Profile

**Flow Navigation** (Screen to Screen)

* Forced Log-in -> Account creation if no log in is available
* Main feed -> Detailed view
* Main feed -> Map view
* Map view -> detailed View
* Search (empty if no zip code is given) -> Detailed View
* Profile (text edit fields) -> Past events
* Profile (text edit fields) -> Friends list 
* Profile (text edit fields) -> Badges
* Past events -> Detailed view(if available)
* Friend list -> Friend's Profile

## Wireframes

### Digital Wireframes & Mockups
<img src="https://i.imgur.com/NsV7rx5.png" width=800><br>

### Interactive Prototype
<img src="https://i.imgur.com/tgXJh69.gif">

## Schema 
### Models
#### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user (default field) |
   | username        | String| unique username for the user |
   | password         | String     | key for authenticating user |
   | email       | String   | contact information for user |
   | friends | Array   | array of objectIds that acts as a friends list |
   | pastevents    | Array  | array of objectIds that stores old ticket purchases |
   | createdAt     | DateTime | date when post is created (default field) |
   | badges     | Array | array of badge objectIds of badges the user has earned |

#### Event

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the event (default field) |
   | eventname        | String| unique name for the event |
   | photo        | String     | url of event images and photos |
   | eventID     | String   | Ticketmaster has unique eventIDs for every event |
   | description | String   | event description |
   | time    | Datetime  | datetime object of the event |
   | location     | String | Location of the event |

#### Badge

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the badge (default field) |
   | badgename        | String| unique name for the badge |
   | photo        | String     | url of the badge image |
   | description | String   | badge description |

### Networking

- Login Screen
	- (Read/GET) Query database to verify inputted credentials 
- Register Screen
	- (Create/POST) Create a new profile and add it to database
- Main Feed Screen
	- (Read/GET) Query for events in area based on location alone or selected category
- Maps Screen
	- (Read/GET) Get map info of local area using external API
	- (Read/GET) Query for events in local area to place onto map using TicketMaster API
- Detailed View Screen
	- (Read/GET) Query for detailed info on clicked event
	- (Update/PUT) Update user with event data if choosing to attend
- Search Screen
	- (Read/GET) Get list of events based on search input
	```java
	// Get User's past events
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include(Event.location);
	query.whereEqualTo(Post.location, searchedLocation);
        query.addDescendingOrder(Event.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error getting events", e);
                }
                // TODO: something
            }
        });
	```
	- (Read/GET) Get list of users based on search input
	```java
	// Get searched users
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.include(User.KEY_USER);
	query.whereEqualTo(User.KEY_USER, searchedUser);
        query.addDescendingOrder(User.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> users, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error getting users", e);
                }
                // TODO: something
            }
        });
	```
	- (Update/PUT) Update user's friends array if choosing to follow searched user
- Profile Screen
	- (Read/GET) Retrieve info from user's object (followers, friends, past events, badges)
- Past Events Screen
	- (Read/GET) Get more detailed info on user's past events
	
- Friend Screen
	- (Read/GET) Get full list of user's follows
	- (Update/PUT) Unfollow a user and remove them from their follows list
- Badges Screen
	- (Read/GET) Get list of available badges, including info on how to earn them
	```java
	// Get badges
        ParseQuery<Badge> query = ParseQuery.getQuery(Badge.class);
        query.include(Badge.KEY_ID);
        query.addDescendingOrder(Badge.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Badge>() {
            @Override
            public void done(List<Badge> badges, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error getting badges", e);
                }
                // TODO: something
            }
        });
	```
	- (Read/GET) Get list of badges the user has earned

## Sprint Demos 

Sprint 1 Progress Demo:

<img src='VenU_sprint_1.gif' title='Sprint 1 Video Walkthrough' width='' alt='Sprint 1 Video Walkthrough' />

Sprint 2 Progress Demo:

<img src='VenU_sprint_2.gif' title='Sprint 2 Video Walkthrough' width='' alt='Sprint 2 Video Walkthrough' />
