import android.content.Context


object AppContextProvider {

    private lateinit var applicationContext: Context

    // Инициализация контекста приложения
    fun initialize(context: Context) {
        if (!::applicationContext.isInitialized) {
            applicationContext = context.applicationContext
        }
    }

    // Получение контекста приложения
    fun getContext(): Context {
        if (!::applicationContext.isInitialized) {
            throw IllegalStateException("AppContextProvider not initialized. Call initialize() first.")
        }
        return applicationContext
    }
}