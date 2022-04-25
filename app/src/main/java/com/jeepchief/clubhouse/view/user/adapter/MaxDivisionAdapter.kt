package com.jeepchief.clubhouse.view.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.model.database.MyDatabase
import com.jeepchief.clubhouse.model.rest.dto.MaxDivisionDTO
import com.jeepchief.clubhouse.util.Log
import kotlinx.coroutines.*

class MaxDivisionAdapter(private val list: List<MaxDivisionDTO>) : RecyclerView.Adapter<MaxDivisionAdapter.MaxDivisionViewHolder>() {
    private lateinit var context: Context

    class MaxDivisionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMatchType: TextView = view.findViewById(R.id.tv_division_type)
        val tvDivisionGrade: TextView = view.findViewById(R.id.tv_division_grade)
        val tvAchievementDate: TextView = view.findViewById(R.id.tv_division_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaxDivisionViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_max_division, parent, false)
        return MaxDivisionViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaxDivisionViewHolder, position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            holder.apply {
                tvMatchType.text = getMatchType(list[position].matchType).await()
                tvDivisionGrade.text = getGrade(list[position].division)
                tvAchievementDate.text = list[position].achievementDate.replace("T", "\n")
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private suspend fun getGrade(divisionId: Int) : String {
        return withContext(Dispatchers.IO) {
            MyDatabase.getInstance(context).getDivisionDAO()
                .selectDivisionName(divisionId).divisionName
        }
    }

    private fun getMatchType(matchId: Int) : Deferred<String> {
        return CoroutineScope(Dispatchers.IO).async {
            MyDatabase.getInstance(context).getMatchTypeDAO()
                .selectMatchType(matchId).desc
        }
    }
}