package com.jeepchief.clubhouse.view.matchrecord.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.model.rest.dto.MatchBean

class MatchRecordAdapter(private val list: List<MatchBean>) : RecyclerView.Adapter<MatchRecordAdapter.MatchRecordViewHolder>() {
    class MatchRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match_list, parent, false)
        return MatchRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchRecordViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return list.size
    }
}