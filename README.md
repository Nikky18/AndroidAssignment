# Event Explorer

## Features
- Event listing
- View list of events
- See event time and details
- Bookmark / unbookmark events
- View all bookmarked events
- Distance calculation based on user location
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

## Decisions
- Local DB as source of truth
- Fake API for simplicity

## Improvements
- Real API
- Improve UI/UX with animations
- Add search & filters
- Add better error handling UI