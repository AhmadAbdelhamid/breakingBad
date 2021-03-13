# breakingBad

Show list of Breaking bad characters with infinite scroll with timer showing live age at current moment.

## Screenshots
![](BreakingBad.gif)

## Tech stack & Open-source libraries
- Minimum SDK level 21.
- 100% Kotlin based + Coroutines.
- JetPack:
    - LiveData
    - ViewModel
    - Paging 3
- Architecture
    - MVVM Architecture (View - DataBinding - ViewModel - Model)
    - Repository pattern
    - Hilt - dependency injection
    
- Retrofit2 & Gson - constructing the REST API
- OkHttp3 - implementing interceptor, logging and mocking web server
- Glide - loading images
- Timber - logging