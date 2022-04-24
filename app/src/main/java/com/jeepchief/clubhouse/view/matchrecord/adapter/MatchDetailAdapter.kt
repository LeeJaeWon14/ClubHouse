package com.jeepchief.clubhouse.view.matchrecord.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.model.database.MyDatabase
import com.jeepchief.clubhouse.model.rest.dto.ShootDetailBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class MatchDetailAdapter(private val goalInfoList: List<ShootDetailBean>) : RecyclerView.Adapter<MatchDetailAdapter.MatchDetailViewHolder>() {
    private lateinit var context: Context
    class MatchDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPlayerName: TextView = view.findViewById(R.id.tv_goal_player)
        val tvGoalTime: TextView = view.findViewById(R.id.tv_goal_time)
        val tvAssistNam: TextView = view.findViewById(R.id.tv_assist_player)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match_detail, parent, false)
        context = parent.context
        return MatchDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchDetailViewHolder, position: Int) {
        holder.apply {
//            getGoalInfo(shootDetailBean).also { Log.e("list size is $it") }
            CoroutineScope(Dispatchers.Main).launch {
                goalInfoList[position].run {
                    tvPlayerName.text = StringBuilder("득점 : ").append(getGoalPlayer(spId.toString()))
                    tvGoalTime.text = (goalTime).toString().plus("분")
                    tvAssistNam.text = if(assistSpId == -1) "No Assist" else StringBuilder("도움 : ").append(getAssistPlayer(assistSpId.toString()))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return goalInfoList.size
    }

    private fun getGoalInfo(shootDetailBean: List<ShootDetailBean>) : List<ShootDetailBean> {
        val goalInfoList: MutableList<ShootDetailBean> = mutableListOf()
        shootDetailBean.forEach { bean ->
            if(bean.result == 3)
                goalInfoList.add(bean)
        }

        return goalInfoList
    }

    private suspend fun getGoalPlayer(spid: String) : String {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            MyDatabase.getInstance(context).getPlayerDAO()
                .selectPlayer(spid)
        }
        return deferred.await()
    }

    private suspend fun getAssistPlayer(spid: String) : String {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            MyDatabase.getInstance(context).getPlayerDAO()
                .selectPlayer(spid)
        }
        return deferred.await()
    }

    private fun convertSecond(time: Int) : Int = time / 60
}