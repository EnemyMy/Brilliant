package com.example.app_37_brilliantapp

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.app_37_brilliantapp.currentdiamond.CurrentDiamondViewModel
import com.example.app_37_brilliantapp.data.DefaultRepository
import com.example.app_37_brilliantapp.data.Repository
import com.example.app_37_brilliantapp.earneddiamonds.EarnedDiamondsViewModel
import com.example.app_37_brilliantapp.finddiamond.FindTheDiamondViewModel
import com.example.app_37_brilliantapp.getdiamond.GetDiamondViewModel
import com.example.app_37_brilliantapp.ideas.IdeasViewModel
import com.example.app_37_brilliantapp.login.LoginViewModel
import com.example.app_37_brilliantapp.signup.SignupViewModel
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class StateViewModelFactory<out V : ViewModel> (
        private val factory: BaseViewModelFactory<V>,
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) = factory.create(handle) as T

}