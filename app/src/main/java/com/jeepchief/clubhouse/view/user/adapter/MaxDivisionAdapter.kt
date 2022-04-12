package com.jeepchief.clubhouse.view.user.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.clubhouse.model.rest.dto.MaxDivisionDTO

class MaxDivisionAdapter(private val list: List<MaxDivisionDTO>) : RecyclerView.Adapter<MaxDivisionAdapter.MaxDivisionViewHolder>() {
    class MaxDivisionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaxDivisionViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MaxDivisionViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return list.size
    }
}