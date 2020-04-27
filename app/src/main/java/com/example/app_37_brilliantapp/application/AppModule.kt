package com.example.app_37_brilliantapp.application

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import com.example.app_37_brilliantapp.data.DefaultRepository
import com.example.app_37_brilliantapp.data.RemoteDataSource
import com.example.app_37_brilliantapp.data.Repository
import com.example.app_37_brilliantapp.data.remote.FirebaseDataSource
import com.example.app_37_brilliantapp.login.LoginFragment
import com.example.app_37_brilliantapp.login.LoginViewModelFactory
import com.example.app_37_brilliantapp.signup.SignupFragment
import com.example.app_37_brilliantapp.signup.SignupViewModel
import com.example.app_37_brilliantapp.signup.SignupViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun bindRepository(repository: DefaultRepository): Repository

    @Singleton
    @Binds
    abstract fun bindRemoteDataSource(dataSource: FirebaseDataSource): RemoteDataSource

}