package com.example.app_37_brilliantapp.application

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.app_37_brilliantapp.FragmentFactoryImpl
import com.example.app_37_brilliantapp.currentdiamond.CurrentDiamondFragment
import com.example.app_37_brilliantapp.earneddiamonds.EarnedDiamondsFragment
import com.example.app_37_brilliantapp.finddiamond.FindTheDiamondFragment
import com.example.app_37_brilliantapp.getdiamond.GetDiamondFragment
import com.example.app_37_brilliantapp.ideas.IdeasFragment
import com.example.app_37_brilliantapp.login.LoginFragment
import com.example.app_37_brilliantapp.main.MainMenuFragment
import com.example.app_37_brilliantapp.signup.SignupFragment
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class FragmentModule {

    @Binds
    abstract fun bindFragmentFactory(factory: FragmentFactoryImpl): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(SignupFragment::class)
    abstract fun bindSignupFragment(fragment: SignupFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(MainMenuFragment::class)
    abstract fun bindMainMenuFragment(fragment: MainMenuFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginFragment::class)
    abstract fun bindLoginFragment(fragment: LoginFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(IdeasFragment::class)
    abstract fun bindIdeasFragment(fragment: IdeasFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(GetDiamondFragment::class)
    abstract fun bindGetDiamondFragment(fragment: GetDiamondFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(FindTheDiamondFragment::class)
    abstract fun bindFindTheDiamondFragment(fragment: FindTheDiamondFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(EarnedDiamondsFragment::class)
    abstract fun bindEarnedDiamondsFragment(fragment: EarnedDiamondsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CurrentDiamondFragment::class)
    abstract fun bindCurrentDiamondFragment(fragment: CurrentDiamondFragment): Fragment

}

@MapKey
annotation class FragmentKey(val clazz: KClass<out Fragment>)