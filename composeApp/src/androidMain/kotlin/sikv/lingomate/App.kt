package sikv.lingomate

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import sikv.lingomate.analytics.AndroidAnalytics
import sikv.lingomate.di.appModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val analytics = AndroidAnalytics(this, postHogProjectToken, postHogHost, isDebug = BuildConfig.DEBUG)

        startKoin {
            androidContext(this@App)
            modules(appModule(analytics))
        }

        initSentry(isDebug = BuildConfig.DEBUG)
    }
}
