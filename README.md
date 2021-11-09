# VenU

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
**VenU** is a location-based event finding android app that connects you to live events in your area. 

### App Evaluation
- **Category:** Entertainment
- **Mobile:** This app would be primarily developed for Android mobile devices.
- **Story:** Pulls a list of events within the user-provided location parameters and displays them to users sorted by Sports, Music, Theater. Users can then purchase tickets to the events through a Ticketmaster redirect.
- **Market:** Any user is welcome to use this app as long as they meet with an event's age requirements.
- **Habit:** This app could be used as often as users would like, depending on their social life and interests.
- **Scope:**

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- See a list of live events within a given radius of the user's location
- Filter results by categories like sports, music, comedy shows, etc.
- Prioritize events based on user's activity and interests
- Set preferences to only display events of interest (i.e. favorite sports teams or bands)
- Create an account on the platform to save preferences
- Purchase tickets via Eventbrite API

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
   * After login, the main feed of local events is shown. At the top of the feed are events categories.
   * Tapping on a category will list events filtered by that category. 
   * Tapping on an event will lead to a detailed view of the event.
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
<img src="https://i.imgur.com/tgXJh69.gif" width=800><br>