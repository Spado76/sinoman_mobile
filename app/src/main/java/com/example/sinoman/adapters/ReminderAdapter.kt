package com.example.sinoman.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sinoman.R
import com.example.sinoman.model.ReminderItem

class ReminderAdapter(
    private val reminders: List<ReminderItem>,
    private val onActionClick: (ReminderItem) -> Unit,
    private val onCloseClick: (ReminderItem) -> Unit
) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    class ReminderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.reminderTitle)
        val messageTextView: TextView = view.findViewById(R.id.reminderMessage)
        val actionButton: Button = view.findViewById(R.id.actionButton)
        val closeButton: ImageButton = view.findViewById(R.id.closeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder_card, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminders[position]
        
        holder.titleTextView.text = reminder.title
        holder.messageTextView.text = reminder.message
        holder.actionButton.text = reminder.actionText
        
        holder.actionButton.setOnClickListener {
            onActionClick(reminder)
        }
        
        holder.closeButton.setOnClickListener {
            onCloseClick(reminder)
        }
    }

    override fun getItemCount() = reminders.size
}
