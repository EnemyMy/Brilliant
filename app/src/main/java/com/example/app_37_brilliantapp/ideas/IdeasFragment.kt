package com.example.app_37_brilliantapp.ideas

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.StateViewModelFactory
import com.example.app_37_brilliantapp.data.Idea
import com.example.app_37_brilliantapp.databinding.FragmentIdeasBinding
import com.example.app_37_brilliantapp.util.setupSnackbar
import javax.inject.Inject
import javax.inject.Named

class IdeasFragment @Inject constructor(@Named("IdeasViewModelFactory") private val factory: BaseViewModelFactory<out ViewModel>): Fragment() {

    private lateinit var binding: FragmentIdeasBinding
    private val viewModel by viewModels<IdeasViewModel> { StateViewModelFactory(factory, this) }
    private val backCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            if (binding.ideasFragmentAddIdeaCard.visibility == View.VISIBLE)
                binding.ideasFragmentAddIdeaCard.animate()
                    .setDuration(500)
                    .scaleX(0.01F)
                    .scaleY(0.01F)
                    .setInterpolator(AnticipateInterpolator())
                    .withEndAction {
                        binding.ideasFragmentAddIdeaCard.visibility = View.GONE
                        isEnabled = false
                    }
                    .start()
            else
                requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentIdeasBinding.inflate(inflater).apply {
            this.fragment = this@IdeasFragment
            this.viewModel = this@IdeasFragment.viewModel
            this.lifecycleOwner = this@IdeasFragment.viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.ideasFragmentRecycler) {
            this.adapter =
                IdeasRecyclerAdapter(viewModel)
        }
        Log.e("OnViewCreated", "List: ${(binding.ideasFragmentRecycler.adapter as IdeasRecyclerAdapter).currentList}")
        setupList()
        setupSnackbar()
    }

    fun onCreateIdeaButtonCLick() {
        binding.ideasFragmentAddIdeaCard.visibility = View.VISIBLE
        binding.ideasFragmentAddIdeaCard.animate()
            .setDuration(500)
            .scaleX(1F)
            .scaleY(1F)
            .setInterpolator(OvershootInterpolator())
            .start()
        backCallback.isEnabled = true
    }

    fun onCreateIdeaCardButtonCLick() {
        if (viewModel.createIdea()) {
            binding.ideasFragmentAddIdeaCardButton.animate()
                    .setDuration(200)
                    .scaleXBy(0.3F)
                    .scaleYBy(0.3F)
                    .setInterpolator(AccelerateInterpolator())
                    .withEndAction {
                        binding.ideasFragmentAddIdeaCardButton.animate()
                                .setDuration(200)
                                .scaleXBy(-0.3F)
                                .scaleYBy(-0.3F)
                                .setInterpolator(DecelerateInterpolator())
                                .withEndAction {
                                    binding.ideasFragmentAddIdeaCard.animate()
                                            .setDuration(500)
                                            .scaleX(0.01F)
                                            .scaleY(0.01F)
                                            .setInterpolator(AnticipateInterpolator())
                                            .withEndAction { binding.ideasFragmentAddIdeaCard.visibility = View.GONE }
                                            .start()
                                }
                                .start()
                    }
                    .start()
        }
    }

    private fun setupList() {
        viewModel.ideas.observe(this, Observer {list ->
            Log.e("SetupList", "List: $list")
            Log.e("SetupListBefore", "List: ${(binding.ideasFragmentRecycler.adapter as IdeasRecyclerAdapter).currentList}")
            if (!viewModel.clearOldIdeas())
                (binding.ideasFragmentRecycler.adapter as IdeasRecyclerAdapter).submitList(list)
            Log.e("SetupListAfter", "List: ${(binding.ideasFragmentRecycler.adapter as IdeasRecyclerAdapter).currentList}")
        })
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackBarEvent)
    }
}