package com.example.app_37_brilliantapp.finddiamond

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.custom.FontSpan
import com.example.app_37_brilliantapp.R
import com.example.app_37_brilliantapp.StateViewModelFactory
import com.example.app_37_brilliantapp.databinding.FragmentFindTheDiamondBinding
import com.example.app_37_brilliantapp.util.setupSnackbar
import javax.inject.Inject
import javax.inject.Named

class FindTheDiamondFragment @Inject constructor(@Named("FindTheDiamondViewModelFactory") private val factory: BaseViewModelFactory<out ViewModel>): Fragment() {

    private lateinit var binding: FragmentFindTheDiamondBinding
    private val viewModel by viewModels<FindTheDiamondViewModel> { StateViewModelFactory(factory, this) }
    private val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.fragmentFindTheDiamondInfoCard.visibility == View.VISIBLE) {
                binding.fragmentFindTheDiamondInfoCard.animate()
                    .setDuration(500)
                    .scaleX(0.01F)
                    .scaleY(0.01F)
                    .setInterpolator(AnticipateInterpolator())
                    .withEndAction {
                        binding.fragmentFindTheDiamondInfoCard.visibility = View.GONE
                    }
                    .start()
            }
            else
                requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFindTheDiamondBinding.inflate(inflater).apply {
            this.fragment = this@FindTheDiamondFragment
            this.viewModel = this@FindTheDiamondFragment.viewModel
            //this.infoCard = binding.fragmentFindTheDiamondInfoCard
            this.lifecycleOwner = this@FindTheDiamondFragment.viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = binding.fragmentFindTheDiamondInfoCardText.text
        SpannableString(text).apply {
            val startIndex = text.indexOf("Caution!")
            val endIndex = startIndex + 8
            this.setSpan(
                    FontSpan(
                            ResourcesCompat.getFont(
                                    requireContext(),
                                    R.font.montserrat_bold
                            )
                    ),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.fragmentFindTheDiamondInfoCardText.text = this
        }

        binding.fragmentFindTheDiamondInfoCard.post {
            binding.fragmentFindTheDiamondInfoCard.apply {
                pivotX = width.toFloat()
                pivotY = 0F
                scaleX = 0.01F
                scaleY = 0.01F
                visibility = View.GONE
            }
        }

        setupNavigation()
        setupSnackbar()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackBarEvent)
    }

    private fun setupNavigation() {
        viewModel.saveDiamondEvent.observe(this, Observer {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        })
    }

    fun onInfoButtonClick() {
        binding.fragmentFindTheDiamondInfoCard.visibility = View.VISIBLE
        binding.fragmentFindTheDiamondInfoCard.animate()
            .setDuration(500)
            .scaleX(1F)
            .scaleY(1F)
            .setInterpolator(OvershootInterpolator())
            .start()
    }

    fun onBackButtonClick() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}