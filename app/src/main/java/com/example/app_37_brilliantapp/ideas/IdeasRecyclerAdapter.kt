package com.example.app_37_brilliantapp.ideas

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app_37_brilliantapp.data.Idea
import com.example.app_37_brilliantapp.databinding.IdeasRecyclerItemBinding

class IdeasRecyclerAdapter(private val viewModel: IdeasViewModel): ListAdapter<Idea, IdeasRecyclerAdapter.RecyclerHolder>(IdeasDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val binding = IdeasRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.binding.viewHolder = holder
        holder.binding.idea = currentList[position]
        if (currentList[position].done) holder.binding.ideasRecyclerItemText.setTextStrikeThrough()
        Log.e("onBindViewHolder", "IdeaState: ${currentList[position].done}")
        Log.e("onBindViewHolder", "CheckboxVisibility: ${holder.binding.ideasRecyclerItemCheckbox.visibility}")
        Log.e("onBindViewHolder", "CheckboxDoneVisibility: ${holder.binding.ideasRecyclerItemCheckboxDone.visibility}")
    }

    inner class RecyclerHolder(val binding: IdeasRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onCheckboxClick() {
            Log.e("CheckboxClick", "onCheckboxClick")
            if (binding.ideasRecyclerItemCheckboxDone.visibility == View.INVISIBLE) {
                binding.ideasRecyclerItemCheckboxDone.visibility = View.VISIBLE
                binding.ideasRecyclerItemCheckboxDone.animate()
                    .setDuration(300)
                    .scaleX(1F)
                    .scaleY(1F)
                    .setInterpolator(OvershootInterpolator())
                        .withEndAction {
                            val currentIdea = currentList[layoutPosition].apply { done = true }
                            binding.idea = currentIdea
                            viewModel.changeIdeaState(currentIdea)
                        }
                    .start()
                binding.ideasRecyclerItemText.startStrikeThroughAnimationForward()
            } else {
                binding.ideasRecyclerItemCheckboxDone.animate()
                    .setDuration(300)
                    .scaleX(0.01F)
                    .scaleY(0.01F)
                    .setInterpolator(AnticipateInterpolator())
                    .withEndAction {
                        val currentIdea = currentList[layoutPosition].apply { done = false }
                        binding.idea = currentIdea
                        viewModel.changeIdeaState(currentIdea)
                        binding.ideasRecyclerItemCheckboxDone.visibility = View.INVISIBLE
                    }
                    .start()
                binding.ideasRecyclerItemText.startStrikeThroughAnimationBackward()
            }

        }

        fun onCheckboxLongClick(view: View?): Boolean {
            if (binding.ideasRecyclerItemCheckboxDelete.visibility == View.INVISIBLE) {
                binding.ideasRecyclerItemCheckboxDelete.visibility = View.VISIBLE
                binding.ideasRecyclerItemCheckboxDelete.animate()
                    .setDuration(300)
                    .scaleX(1F)
                    .scaleY(1F)
                    .setInterpolator(OvershootInterpolator())
                    .start()
            } else {
                binding.ideasRecyclerItemCheckboxDelete.animate()
                    .setDuration(300)
                    .scaleX(0.01F)
                    .scaleY(0.01F)
                    .setInterpolator(AnticipateInterpolator())
                    .withEndAction { binding.ideasRecyclerItemCheckboxDelete.visibility = View.INVISIBLE }
                    .start()
            }
            return true
        }

        fun onDeleteIdeaClick() {
            val currentIdea = currentList[layoutPosition]
            binding.ideasRecyclerItemCheckboxDelete.visibility = View.INVISIBLE
            binding.ideasRecyclerItemText.setTextCommon()
            viewModel.deleteIdea(currentIdea)
        }
    }

}

class IdeasDiffCallback: DiffUtil.ItemCallback<Idea>() {
    override fun areItemsTheSame(oldItem: Idea, newItem: Idea): Boolean = oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: Idea, newItem: Idea): Boolean = oldItem == newItem
}