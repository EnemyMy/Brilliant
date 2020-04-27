package com.example.app_37_brilliantapp.earneddiamonds

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.StateViewModelFactory
import com.example.app_37_brilliantapp.data.Difficulty
import com.example.app_37_brilliantapp.data.EarnedDiamond
import com.example.app_37_brilliantapp.databinding.FragmentEarnedDiamondsBinding
import com.example.app_37_brilliantapp.util.setupSnackbar
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.roundToInt

class EarnedDiamondsFragment @Inject constructor(@Named("EarnedDiamondsViewModelFactory") private val factory: BaseViewModelFactory<out ViewModel>): Fragment() {

    private lateinit var binding: FragmentEarnedDiamondsBinding
    private val viewModel by viewModels<EarnedDiamondsViewModel> { StateViewModelFactory(factory, this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEarnedDiamondsBinding.inflate(inflater).apply {
            this.lifecycleOwner = this@EarnedDiamondsFragment.viewLifecycleOwner
            this.viewModel = this@EarnedDiamondsFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.earnedDiamondsFragmentRecycler) {
            val itemDimension =
                EarnedDiamondsRecyclerItemDimension(
                    context
                )
            this.adapter =
                EarnedDiamondsRecyclerAdapter(
                    itemDimension
                )
            this.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            this.addItemDecoration(
                SpacesItemDecoration(
                    itemDimension.itemSpacing.roundToInt(),
                    spanCount = 2
                )
            )
        }
        setupSnackbar()
        setupList()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarEvent)
    }

    private fun setupList() {
        viewModel.earnedDiamonds.observe(this, Observer { list ->
            if (list != null) {
                val adapter = binding.earnedDiamondsFragmentRecycler.adapter as EarnedDiamondsRecyclerAdapter
                adapter.list = list
                adapter.notifyDataSetChanged()
            }
        })
    }

    class SpacesItemDecoration @JvmOverloads constructor(private val horizontalSpacing: Int, private val verticalSpacing: Int = horizontalSpacing, private val spanCount: Int = 1): RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.top = verticalSpacing
            outRect.bottom = verticalSpacing
            if (parent.getChildAdapterPosition(view) / spanCount == 0) {
                outRect.left = horizontalSpacing
            } else {
                outRect.left = 0
            }
            outRect.right = horizontalSpacing
        }
    }
}