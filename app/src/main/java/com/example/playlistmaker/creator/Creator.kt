package com.example.playlistmaker.creator


//const val SHARED_PREFERENCES = "shared_preferences"
//
//object Creator {
//
//    private lateinit var application: Application
//
//
//    fun initApplication(application: Application) {
//        Creator.application = application
//    }
//
//    fun provideSharedPreferences(): SharedPreferences =
//        application.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
//
//
//    private fun provideSettingsRepository(): SettingsRepository =
//        SettingsRepositoryImpl(provideSharedPreferences())
//
//
//    private fun providePlayerRepository(): PlayerRepository =
//        PlayerRepositoryImpl(mediaPlayer = MediaPlayer())
//
//    fun providePlayerInteractor(): PlayerInteractor =
//        PlayerInteractorImpl(providePlayerRepository())
//
//
//    fun provideSettingsInteractor(): SettingsInteractor =
//        SettingsInteractorImpl(provideSettingsRepository())
//
//    private fun provideSearchRepository(context: Context): SearchRepository =
//        SearchRepositoryImpl(RetrofitClient(context), provideSharedPreferences())
//
//    fun provideSearchInteractor(context: Context): SearchInteractor =
//        SearchInteractorImpl(provideSearchRepository(context))
//}