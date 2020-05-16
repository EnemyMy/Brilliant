package com.example.app_37_brilliantapp.application

import android.app.Application
import com.example.app_37_brilliantapp.main.MainActivity
import com.example.app_37_brilliantapp.main.MainMenuFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RepositoryModule::class, FragmentModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: MainMenuFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: BrilliantApplication): AppComponent
    }

}