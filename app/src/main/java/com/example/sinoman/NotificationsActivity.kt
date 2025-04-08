package com.example.sinoman

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sinoman.model.Notification
import com.example.sinoman.utils.NotificationManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class NotificationsActivity : AppCompatActivity() {

    private lateinit var notificationsRecyclerView: RecyclerView
    private lateinit var emptyNotificationsTextView: TextView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var notificationsAdapter: NotificationsAdapter
    private var notifications: MutableList<Notification> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        // Initialize views
        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView)
        emptyNotificationsTextView = findViewById(R.id.emptyNotificationsTextView)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up recycler view
        setupRecyclerView()

        // Set up bottom navigation
        bottomNavigation.selectedItemId = R.id.nav_notifications
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_notifications -> true
                R.id.nav_form -> {
                    if (FormData.isPersonalDataCompleted(this)) {
                        startActivity(Intent(this, FormPage2Activity::class.java))
                    } else {
                        startActivity(Intent(this, FormActivity::class.java))
                    }
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        // Check for notifications
        checkForNotifications()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.notifications_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_mark_all_read -> {
                markAllAsRead()
                true
            }
            R.id.action_clear_all -> {
                showClearAllConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        notificationsAdapter = NotificationsAdapter(
            notifications,
            onNotificationClick = { notification ->
                markAsRead(notification.id)
            }
        )
        
        notificationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@NotificationsActivity)
            adapter = notificationsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun loadNotifications() {
        notifications.clear()
        notifications.addAll(Notification.loadNotifications(this))
        notificationsAdapter.notifyDataSetChanged()
        
        if (notifications.isEmpty()) {
            emptyNotificationsTextView.visibility = View.VISIBLE
            notificationsRecyclerView.visibility = View.GONE
        } else {
            emptyNotificationsTextView.visibility = View.GONE
            notificationsRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun markAsRead(notificationId: String) {
        Notification.markAsRead(this, notificationId)
        loadNotifications()
    }

    private fun markAllAsRead() {
        Notification.markAllAsRead(this)
        loadNotifications()
    }

    private fun clearAllNotifications() {
        Notification.clearAllNotifications(this)
        loadNotifications()
    }

    private fun showClearAllConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Hapus Semua Notifikasi")
            .setMessage("Apakah Anda yakin ingin menghapus semua notifikasi?")
            .setPositiveButton("Ya") { _, _ ->
                clearAllNotifications()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun checkForNotifications() {
        // Check for data completion
        NotificationManager.checkDataCompletion(this)
        
        // Check for registration submissions
        NotificationManager.checkRegistrationSubmissions(this)
        
        // Load notifications
        loadNotifications()
    }

    override fun onResume() {
        super.onResume()
        loadNotifications()
    }

    inner class NotificationsAdapter(
        private val notifications: List<Notification>,
        private val onNotificationClick: (Notification) -> Unit
    ) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val unreadIndicator: View = view.findViewById(R.id.unreadIndicator)
            val titleTextView: TextView = view.findViewById(R.id.notificationTitleTextView)
            val messageTextView: TextView = view.findViewById(R.id.notificationMessageTextView)
            val timeTextView: TextView = view.findViewById(R.id.notificationTimeTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val notification = notifications[position]
            
            // Set notification data
            holder.titleTextView.text = notification.title
            holder.messageTextView.text = notification.message
            holder.timeTextView.text = getTimeAgo(notification.timestamp)
            
            // Set unread indicator visibility
            holder.unreadIndicator.visibility = if (notification.isRead) View.INVISIBLE else View.VISIBLE
            
            // Set item click listener
            holder.itemView.setOnClickListener {
                onNotificationClick(notification)
            }
        }

        override fun getItemCount() = notifications.size

        private fun getTimeAgo(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            
            return when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "Baru saja"
                diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)} menit yang lalu"
                diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)} jam yang lalu"
                diff < TimeUnit.DAYS.toMillis(7) -> "${TimeUnit.MILLISECONDS.toDays(diff)} hari yang lalu"
                else -> {
                    val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
                    sdf.format(Date(timestamp))
                }
            }
        }
    }
}

