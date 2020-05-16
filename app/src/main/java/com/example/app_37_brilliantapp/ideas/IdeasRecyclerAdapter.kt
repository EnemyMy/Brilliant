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
import com.example.app_37_brilliantapp.util.makeAnimationScale

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
                val action = Runnable {
                    val currentIdea = currentList[layoutPosition].apply { done = true }
                    binding.idea = currentIdea
                    viewModel.changeIdeaState(currentIdea)
                }
                animateCheckboxDoneAppear(action)
                binding.ideasRecyclerItemText.startStrikeThroughAnimationForward()
            } else {
                val action = Runnable {
                    val currentIdea = currentList[layoutPosition].apply { done = false }
                    binding.idea = currentIdea
                    viewModel.changeIdeaState(currentIdea)
                    binding.ideasRecyclerItemCheckboxDone.visibility = View.INVISIBLE
                }
                animateCheckboxDoneDisappear(action)
                binding.ideasRecyclerItemText.startStrikeThroughAnimationBackward()
            }

        }

        fun onCheckboxLongClick(view: View?): Boolean {
            if (binding.ideasRecyclerItemCheckboxDelete.visibility == View.INVISIBLE) {
                binding.ideasRecyclerItemCheckboxDelete.visibility = View.VISIBLE
                animateCheckboxDeleteAppear()
            } else {
                val action = Runnable {
                    binding.ideasRecyclerItemCheckboxDelete.visibility = View.INVISIBLE
                }
                animateCheckboxDeleteDisappear(action)
            }
            return true
        }

        fun onDeleteIdeaClick() {
            val currentIdea = currentList[layoutPosition]
            binding.ideasRecyclerItemCheckboxDelete.visibility = View.INVISIBLE
            binding.ideasRecyclerItemText.setTextCommon()
            viewModel.deleteIdea(currentIdea)
        }

        private fun animateCheckboxDoneAppear(action: Runnable) {
            binding.ideasRecyclerItemCheckboxDone.makeAnimationScale(300, 1.0, 1.0, OvershootInterpolator(), action)
        }

        private fun animateCheckboxDoneDisappear(action: Runnable) {
            binding.ideasRecyclerItemCheckboxDone.makeAnimationScale(300, 0.01, 0.01, AnticipateInterpolator(), action)
        }

        private fun animateCheckboxDeleteAppear() {
            binding.ideasRecyclerItemCheckboxDelete.makeAnimationScale(300, 1.0, 1.0, OvershootInterpolator())
        }

        private fun animateCheckboxDeleteDisappear(action: Runnable) {
            binding.ideasRecyclerItemCheckboxDelete.makeAnimationScale(300, 0.01, 0.01, AnticipateInterpolator(), action)
        }
    }

}

class IdeasDiffCallback: DiffUtil.ItemCallback<Idea>() {
    override fun areItemsTheSame(oldItem: Idea, newItem: Idea): Boolean = oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: Idea, newItem: Idea): Boolean = oldItem == newItem
}