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
import kotlin.math.pow

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
//            goalInfoList.sortedByDescending { it.goalTime }
            CoroutineScope(Dispatchers.Main).launch {
                goalInfoList[position].run {
                    tvPlayerName.text = StringBuilder("득점 : ").append(getGoalPlayer(spId.toString()))
                    tvGoalTime.text = convertGoalTime(goalTime).toString().plus("분")
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

    private fun convertGoalTime(time: Int) : Int {
        val rangeBase = 2.0.pow(24.0).toInt()
        return when(time) {
            in rangeBase * 0 until rangeBase * 1 -> { // 전반전
                time / 60
            }
            in rangeBase * 1 until rangeBase * 2 -> { // 후반전, rangeBase * 1 차감 후 45 * 60s 더하기
                (time - (rangeBase * 1) + (45 * 60)) / 60
            }
            in rangeBase * 2 until rangeBase * 3 -> { // 연장 전반전, rangeBase * 2 차감 후 90 * 60s 더하기
                (time - (rangeBase * 2) + (90 * 60)) / 60
            }
            in rangeBase * 3 until rangeBase * 4 -> { // 연장 후반전, rangeBase * 3 차감 후 105 * 60s 더하기
                (time - (rangeBase * 3) + (105 * 60)) / 60
            }
            in rangeBase * 4 until rangeBase * 5 -> { // 승부차기, rangeBase * 4 차감 후 120 * 60s 더하기
                (time - (rangeBase * 4) + (120 * 60)) / 60
            }
            else -> { 0 }
        }
    }
}