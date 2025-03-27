package com.example.sinoman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class IntroSlideAdapter(
    private val layouts: List<Int>,
    private val onNextButtonClick: (position: Int) -> Unit
) : RecyclerView.Adapter<IntroSlideAdapter.SlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val layoutId = layouts[viewType]
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        // Use final position to avoid issues with RecyclerView position changes
        val finalPosition = holder.adapterPosition
        holder.nextButton.setOnClickListener {
            onNextButtonClick(finalPosition)
        }
    }

    override fun getItemCount(): Int = layouts.size

    override fun getItemViewType(position: Int): Int = position

    inner class SlideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nextButton: Button = view.findViewById(R.id.nextButton)
    }
}

