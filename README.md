# Event Explorer APP here you can see event list, bookmark your favourite event and see it's detail.


## Features
- View list of events
- See event time and details
- Bookmark / unbookmark events
- View all bookmarked events
- Distance calculation based on user's current  to event location
- Open event location in maps
- Offline support (cached events & images)
- Background sync using WorkManager

## Tech Stack
- Kotlin, Compose, MVVM, Room, Hilt, Coil, WorkManager

## Architecture
MVVM + Repository
UI (Compose)
↓
ViewModel
↓
Repository
↓
Room Database ←→ API (Mock JSON)

## Application Flow
- App Launch 
- ViewModel calls Repository.refresh()
- Repository fetches data from API (JSON)
- Data saved into Room Database 
- Room emits Flow updates 
- ViewModel collects Flow 
- UI recomposes and shows list

## Demo
The `demo/` folder contains three screen recordings showcasing the application's functionality and user flow.
- Demo 1: Main features walkthrough
- Demo 2: Showing events with image while app is not connected to internet.
- Demo 3: Application features in light theme.

## Decisions
- Local DB as source of truth
- Fake API for simplicity

## Improvements
- Real API
- Improve UI/UX with animations
- Add search & filters
- Add better error handling UI