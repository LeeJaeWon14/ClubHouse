package com.jeepchief.clubhouse.view.user.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.model.database.metadata.division.DivisionEntity

class GradeGuideAdapter(private val list: List<DivisionEntity>) : RecyclerView.Adapter<GradeGuideAdapter.GradeGuideViewHolder>() {
    class GradeGuideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvGrade: TextView = view.findViewById(R.id.tv_grade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeGuideViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grade_guide, parent, false)
        return GradeGuideViewHolder(view)
    }

    override fun onBindViewHolder(holder: GradeGuideViewHolder, position: Int) {
        holder.apply {
            tvGrade.text = list[position].divisionName
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}