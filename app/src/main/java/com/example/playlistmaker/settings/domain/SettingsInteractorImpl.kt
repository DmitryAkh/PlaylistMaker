package com.example.playlistmaker.settings.domain


class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    override fun provideIsNightMode(): Boolean = repository.provideIsNightMode()

    override fun switchTheme(darkThemeEnabled: Boolean) = repository.switchTheme(darkThemeEnabled)


    override fun setTheme(darkThemeEnabled: Boolean) = repository.setTheme(darkThemeEnabled)

}