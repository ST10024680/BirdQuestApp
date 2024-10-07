package com.example.birdwatchapp_p2.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.birdwatchapp_p2.R
import com.example.birdwatchapp_p2.ui.captures.BirdSighting

class CapturesAdapter(private val capturesList: List<BirdSighting>) :
    RecyclerView.Adapter<CapturesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val birdName: TextView = view.findViewById(R.id.birdName)
        val location: TextView = view.findViewById(R.id.location)
        val dateTime: TextView = view.findViewById(R.id.dateTime)
        val notes: TextView = view.findViewById(R.id.notes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_capture, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val capture = capturesList[position]
        holder.birdName.text = capture.birdName
        holder.location.text = capture.location
        holder.dateTime.text = capture.dateTime
        holder.notes.text = capture.notes
    }

    override fun getItemCount(): Int {
        return capturesList.size
    }
}
