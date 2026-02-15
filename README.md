# Foody

Foody is an Android app for discovering meals, viewing recipes, and planning your week. It combines a remote meal API with local favorites and optional cloud sync for authenticated users.

## Features

- Onboarding flow with first-launch tracking.
- Authentication: email/password, Google, Facebook, and guest mode.
- Home feed with a random meal card and popular meals carousel.
- Search by category or country, plus advanced filtering for category/country/ingredient.
- Meal details: ingredients, step-by-step instructions, share link, and YouTube video player.
- Favorites: local storage with optional Firestore sync (auth required).
- Meal plan: breakfast/lunch/dinner by date with Firestore storage (auth required).
- Offline detection with a dedicated no-internet dialog.

## Architecture

The app follows a layered, MVP-style structure with clear separation of concerns:

- Presentation layer (`app/src/main/java/org/wahid/foody/presentation`)
  - `Fragment` + `Presenter` pairs (e.g., `HomeFragment` + `HomePresenterImpl`) manage UI and user actions.
  - UI contracts are defined through `*View` and `*Presenter` interfaces.
  - Navigation is defined in `app/src/main/res/navigation/app_navigation_graph.xml`.
- Data layer (`app/src/main/java/org/wahid/foody/data`)
  - Remote data sources use Retrofit (`RemoteMealDatasource`) and map DTOs to domain models.
  - Local data uses Room (`MealRoomDb`, `MealDao`) with RxJava streams.
  - Firestore handles meal plans and favorites for authenticated users (`FirestoreRepositoryImpl`).
- Domain models (`app/src/main/java/org/wahid/foody/presentation/model`)
  - Simple, presentation-ready models such as `MealDomainModel`, `CategoryDomainModel`.
- Utilities (`app/src/main/java/org/wahid/foody/utils`)
  - App preferences, connectivity monitoring, image loading, and simple dependency wiring.

### Data flow (high level)

1. UI event in a Fragment calls a Presenter.
2. Presenter requests data from a repository interface.
3. Repository implementation retrieves data (remote API, Room DB, or Firestore).
4. Presenter maps results to view models and updates the View.

### Dependency wiring

Dependencies are centralized in `ApplicationDependencyRepository` using a simple static service locator. This includes:

- `MealRepository` for remote API access.
- `MealLocalRepository` for local favorites.
- `FirestoreRepository` for cloud sync and meal planning.

## Main Screens

- Onboarding: first-time intro screens.
- Auth: login, register, and guest entry.
- Home: random meal, popular meals carousel, profile menu.
- Search: categories and countries lists, plus a detailed search with filters.
- Details: ingredients, instructions, share, and video playback.
- Favorites: local favorites with optional cloud sync (auth required).
- Plan: daily meal planning with breakfast/lunch/dinner (auth required).

## Tech Stack

- Language: Java (Android, Java 17)
- UI: Fragments, ViewBinding, Material Components
- Navigation: Jetpack Navigation + Safe Args
- Networking: Retrofit + OkHttp + Gson
- Async: RxJava3 + RxAndroid
- Persistence: Room
- Auth & Cloud: Firebase Auth + Firestore + Firebase UI Auth
- Media: Glide, Android YouTube Player
- Social auth: Google Identity, Facebook Login

## Project Structure

```
app/src/main/java/org/wahid/foody/
  data/           // Remote, local, and Firestore implementations
  presentation/   // Fragments, presenters, adapters, view contracts
  utils/          // Helpers, preferences, connectivity, DI
app/src/main/res/
  navigation/     // Navigation graph
  layout/         // UI layouts
```

## Setup Notes

- Firebase is configured via `app/google-services.json` and `libs.versions.toml`.
- Facebook login uses values in `strings.xml` (`facebook_app_id`, `facebook_client_token`).
- Meal data is retrieved from a remote API via Retrofit; check `RetrofitClient` for base URL.

## Build

Open the project in Android Studio and sync Gradle. Use the IDE Run button to build and install.

## License

See `LICENSE`.