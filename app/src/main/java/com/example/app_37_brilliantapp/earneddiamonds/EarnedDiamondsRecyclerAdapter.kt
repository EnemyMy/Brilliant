package com.example.app_37_brilliantapp.earneddiamonds

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_37_brilliantapp.data.EarnedDiamond
import com.example.app_37_brilliantapp.databinding.EarnedDiamondsRecyclerItemBinding
import kotlin.math.roundToInt

class EarnedDiamondsRecyclerAdapter(private val itemDimension: EarnedDiamondsRecyclerItemDimension): RecyclerView.Adapter<EarnedDiamondsRecyclerAdapter.RecyclerHolder>() {

    var list = listOf<EarnedDiamond>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val binding = EarnedDiamondsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        setRecyclerItemSize(binding, parent.context)
        return RecyclerHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.binding.diamond = list[position]
    }

    override fun getItemCount(): Int = list.size

    private fun setRecyclerItemSize(binding: EarnedDiamondsRecyclerItemBinding, context: Context) {
        val layoutParams = binding.root.layoutParams
        layoutParams.width = itemDimension.itemWidth.roundToInt()
        layoutParams.height = layoutParams.width
        binding.root.layoutParams = layoutParams
    }

    inner class RecyclerHolder(val binding: EarnedDiamondsRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)

}