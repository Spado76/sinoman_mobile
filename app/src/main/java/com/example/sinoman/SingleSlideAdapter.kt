package com.example.sinoman

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SingleSlideAdapter(private val context: Context) : RecyclerView.Adapter<SingleSlideAdapter.SlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.slide_intro_single, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        // Nothing to bind as we have only one static slide
    }

    override fun getItemCount(): Int = 1 // Only one slide

    inner class SlideViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
