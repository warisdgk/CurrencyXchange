# CurrencyXchange
Android app that allows converting currency to other available currencies using open apis from https://openexchangerates.org/

## Architecture
Architecture of the app is based on Clean Architecture practices. I have not used any domain layer with names like
'Interactors' or 'UseCases' because of the simple nature of application

## Application FLow
This Application uses work manager for fetching the data from network. Its a periodic job done after 15 mins to update local storage with the latest data.
Local DB(Room) is the single source of truth for data in the application. Data is kept unidirectional to avoid any inconsistent states.

## Testing

Most data layer components are defined as interfaces.
Then, concrete implementations (with various dependencies) are bound to provide those interfaces to
other components in the app.
In tests, **CurrencyXchange** does _not_ use any mocking libraries.
Instead, the production implementations can be replaced with test doubles using testing APIs
(or via manual constructor injection for `ViewModel` tests).

These test doubles implement the same interface as the production implementations and generally
provide a simplified (but still realistic) implementation with additional testing hooks.
This results in less brittle tests that may exercise more production code, instead of just verifying
specific calls against mocks.

## Libraries Used

* [Jetpack-Compose](https://developer.android.com/jetpack/compose) - Jetpack Compose is Android’s recommended modern toolkit for building native UI.
* [LeakCanary](https://square.github.io/leakcanary/) - LeakCanary is a memory leak detection library for Android.
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Newly introduced library on top of Dagger two for [Depencency Inversion](https://developer.android.com/training/dependency-injection)
* [Chucker](https://github.com/ChuckerTeam/chucker) - Chucker simplifies the inspection of HTTP(S) requests/responses fired by your Android App.
* [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android to perfrom Network Requests.
* [Kotlin-Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - Helps in writing the code to perform asynchronous operations.


## App Directory Structure

```
app/
|- data/
   |- local
   |- remote
   |- repositories
   |- sync
|- di/
|- ui/
   |- featureName
|- utils/
-MainApplication
```

#### - Folder Structure Explained

1. **Data** - This folder will hold all the data/Domain related classes in it. This data can either be from remote API or Local DB.
2. **di** - This folder will have Dependency Injection related Classes.
3. **ui** - UI will hold all the UI app components majorly composables, Further divided by feature name inside.
4. **utils** - A space for utilities that will be used by all over the application.

## Public APIs Used

Public Api used in the project can be found here : https://openexchangerates.org/

## Author
* Muhammad waris
* Staff Engineer
* Portfolio: [Muhammad waris](http://mwaris.dev/)
