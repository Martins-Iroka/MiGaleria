<p align="center">
   <a href="https://github.com/Martins-Iroka/MiGaleria/actions/workflows/AndroidBuild.yml"><img alt="Build Status" src="https://github.com/Martins-Iroka/MiGaleria/actions/workflows/AndroidBuild.yml/badge.svg"/></a>
</p>

# MiGaleria
MiGaleria is a gallery application that showcases curated photos and videos from the Pexels API. It's built with modern Android development in mind, following Clean Architecture principles.

## Backend
The backend for this project is powered by [MyGallery-Backend](https://github.com/Martins-Iroka/MyGallery-Backend), a Golang server that handles API requests to Pexels.

## Features
* Discover curated and popular photos and videos.
* In-app video playback powered by ExoPlayer.
* Offline caching of content using Room.

## Tech Stack & Libraries
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for modern, declarative UI development.
* **Architecture:** [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html), MVVM
* **Asynchronous Programming:** [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) for managing background threads.
* **Dependency Injection:** [Koin](https://insert-koin.io/) for managing dependencies.
* **Networking:** [Ktor Client](https://ktor.io/docs/client-overview.html) for making network requests.
* **Data Serialization:** [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) for parsing JSON.
* **Database:** [Room](https://developer.android.com/training/data-storage/room) for local data storage.
* **Image Loading:** [Coil](https://coil-kt.github.io/coil/) for loading images.
* **Navigation:** [Jetpack Navigation](https://developer.android.com/guide/navigation) for navigating between screens.
* **Video Playback:** [ExoPlayer](https://github.com/google/ExoPlayer) for video streaming.
* **Pagination:** [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) for paginating content.
* **Logging:** [Timber](https://github.com/JakeWharton/timber) for logging.


## License
[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
