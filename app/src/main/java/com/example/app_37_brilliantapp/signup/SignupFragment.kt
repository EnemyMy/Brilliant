package com.example.app_37_brilliantapp.signup

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.EventObserver
import com.example.app_37_brilliantapp.R
import com.example.app_37_brilliantapp.StateViewModelFactory
import com.example.app_37_brilliantapp.databinding.FragmentSignupBinding
import com.example.app_37_brilliantapp.login.LoginFragment
import com.example.app_37_brilliantapp.main.MainActivity
import com.example.app_37_brilliantapp.main.MainMenuFragment
import com.example.app_37_brilliantapp.util.*
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

import javax.inject.Inject
import javax.inject.Named

private const val RC_SIGN_IN = 9001

class SignupFragment @Inject constructor(@Named("SignupViewModelFactory") private val factory: BaseViewModelFactory<out ViewModel>): Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val parentActivity by lazy { activity as MainActivity }
    private val viewModel by viewModels<SignupViewModel> { StateViewModelFactory(factory, this) }

    //for Google Sign In
    private lateinit var googleSignInClient: GoogleSignInClient

    //for Facebook Sign In
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginManager: LoginManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignupBinding.inflate(inflater).apply {
            this.fragment = this@SignupFragment
            this.viewModel = this@SignupFragment.viewModel
            this.lifecycleOwner = this@SignupFragment.viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLogInText()
        setupNavigation()
        setupSnackbar()
        userLogInCheck()
        configureProviders()
    }

    private fun configureProviders() {
        //configure Google provider
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        //configure Facebook provider
        callbackManager = CallbackManager.Factory.create()
        loginManager = LoginManager.getInstance()
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("FirebaseAuth", "facebook:onSuccess:$loginResult")
                viewModel.firebaseAuthWithFacebook(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.e("FirebaseAuth", "Facebook sign in canceled")
            }

            override fun onError(error: FacebookException) {
                Log.e("FirebaseAuth", "Facebook sign in failed ${error.cause}")
                viewModel.showSnackbar(SnackbarEvent("Facebook sign in failed"))
            }
        })
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackBarEvent)
    }

    private fun userLogInCheck() {
        if (viewModel.isUserLoggedIn) {
            viewModel.startMainMenuEvent()
        }
    }

    private fun prepareLogInText() {
        val text = "Already have account? Log in"
        val ss = SpannableString(text)
        val span = object : ClickableSpan() {
            override fun onClick(widget: View) {
                viewModel.startLoginEvent()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = resources.getColor(R.color.loginLinkTextColor)
            }
        }
        ss.setSpan(span, 22, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.fragmentSignUpLoginText?.text = ss
        binding.fragmentSignUpLoginText?.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupNavigation() {
        viewModel.loginEvent.observe(this, EventObserver {
            navigateToLogin()
        })
        viewModel.mainMenuEvent.observe(this, EventObserver {
            navigateToMainMenu()
        })
    }

    private fun navigateToLogin() {
        parentActivity.supportFragmentManager.navigateToFragment(LoginFragment::class.java, R.id.main_activity_fragment_container, true, "login_fragment_tag")
    }

    private fun navigateToMainMenu() {
        parentActivity.supportFragmentManager.navigateToFragment(MainMenuFragment::class.java, R.id.main_activity_fragment_container, true)
    }

    fun onGoogleButtonClick(view: View) {
        animateImageButton(view, Runnable {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        })
    }

    fun onFacebookButtonClick(view: View) {
        animateImageButton(view, Runnable {
            loginManager.logInWithReadPermissions(this, listOf("email", "public_profile"))
        })

    }

    private fun animateImageButton(view: View, action: Runnable) {
        val onEndAction = Runnable {
            view.makeAnimationScaleBy(200, -0.3, -0.3, DecelerateInterpolator(), action)
        }
        view.makeAnimationScaleBy(200, 0.3, 0.3, AccelerateInterpolator(), onEndAction)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    viewModel.firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                Log.e("FirebaseAuth", "Google sign in failed", e)
                viewModel.showSnackbar(SnackbarEvent("Google sign in failed"))
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}