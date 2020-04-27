package com.example.app_37_brilliantapp.application

import androidx.lifecycle.ViewModel
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.currentdiamond.CurrentDiamondViewModelFactory
import com.example.app_37_brilliantapp.earneddiamonds.EarnedDiamondsViewModelFactory
import com.example.app_37_brilliantapp.finddiamond.FindTheDiamondViewModelFactory
import com.example.app_37_brilliantapp.getdiamond.GetDiamondViewModelFactory
import com.example.app_37_brilliantapp.ideas.IdeasViewModelFactory
import com.example.app_37_brilliantapp.login.LoginViewModelFactory
import com.example.app_37_brilliantapp.signup.SignupViewModelFactory
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
abstract class ViewModelModule {

    @Binds
    @Named("SignupViewModelFactory")
    abstract fun bindSignupViewModelFactory(factory: SignupViewModelFactory): BaseViewModelFactory<out ViewModel>

    @Binds
    @Named("LoginViewModelFactory")
    abstract fun bindLoginViewModelFactory(factory: LoginViewModelFactory): BaseViewModelFactory<out ViewModel>

    @Binds
    @Named("IdeasViewModelFactory")
    abstract fun bindIdeasViewModelFactory(factory: IdeasViewModelFactory): BaseViewModelFactory<out ViewModel>

    @Binds
    @Named("GetDiamondViewModelFactory")
    abstract fun bindGetDiamondViewModelFactory(factory: GetDiamondViewModelFactory): BaseViewModelFactory<out ViewModel>

    @Binds
    @Named("FindTheDiamondViewModelFactory")
    abstract fun bindFindTheDiamondViewModelFactory(factory: FindTheDiamondViewModelFactory): BaseViewModelFactory<out ViewModel>

    @Binds
    @Named("EarnedDiamondsViewModelFactory")
    abstract fun bindEarnedDiamondsViewModelFactory(factory: EarnedDiamondsViewModelFactory): BaseViewModelFactory<out ViewModel>

    @Binds
    @Named("CurrentDiamondViewModelFactory")
    abstract fun bindCurrentDiamondViewModelFactory(factory: CurrentDiamondViewModelFactory): BaseViewModelFactory<out ViewModel>

}