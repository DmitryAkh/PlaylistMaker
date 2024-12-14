interface SettingsRepository {
    fun provideIsNightMode(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
    fun setTheme(darkThemeEnabled: Boolean)
}