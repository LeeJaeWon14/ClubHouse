package com.jeepchief.clubhouse.view.matchrecord.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.databinding.DialogMatchDetailBinding
import com.jeepchief.clubhouse.databinding.DialogSquadBinding
import com.jeepchief.clubhouse.model.rest.FifaService
import com.jeepchief.clubhouse.model.rest.RetroClient
import com.jeepchief.clubhouse.model.rest.dto.MatchBean
import com.jeepchief.clubhouse.model.rest.dto.PlayerBean
import com.jeepchief.clubhouse.model.rest.dto.ShootDetailBean
import com.jeepchief.clubhouse.model.rest.dto.UserInfoDTO
import com.jeepchief.clubhouse.util.Log
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchRecordAdapter(private val list: List<MatchBean>) : RecyclerView.Adapter<MatchRecordAdapter.MatchRecordViewHolder>() {
    private var userDTO : UserInfoDTO? = null
    private val firstBeanMap = hashMapOf<Int, List<ShootDetailBean>>()
    private val secondBeanMap = hashMapOf<Int, List<ShootDetailBean>>()
    private val firstSquadMap = hashMapOf<Int, List<PlayerBean>>()
    private val secondSquadMap = hashMapOf<Int, List<PlayerBean>>()


    class MatchRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFirstName: TextView = view.findViewById(R.id.tv_first_name)
        val tvFirstLevel: TextView = view.findViewById(R.id.tv_first_level)
        val tvFirstScore: TextView = view.findViewById(R.id.tv_first_score)
        val tvSecondName: TextView = view.findViewById(R.id.tv_second_name)
        val tvSecondLevel: TextView = view. findViewById(R.id.tv_second_level)
        val tvSecondScore: TextView = view.findViewById(R.id.tv_second_score)
        val tvPlayDate: TextView = view.findViewById(R.id.tv_play_date)
        val llMatchRecord: LinearLayout = view.findViewById(R.id.ll_match_record)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match_list, parent, false)
        return MatchRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchRecordViewHolder, position: Int) {
        holder.apply {
            list[position].also {
                tvPlayDate.text = it.matchDate.replace("T", " / ")
                firstBeanMap.put(
                    position,
                    getGoalInfo(it.matchInfoBean[0].shootDetailBean)
                )
                secondBeanMap.put(
                    position,
                    getGoalInfo(it.matchInfoBean[1].shootDetailBean)
                )
                firstSquadMap.put(position, it.matchInfoBean[0].playerBean)
                secondSquadMap.put(position, it.matchInfoBean[1].playerBean)
                it.matchInfoBean[0].run {
                    tvFirstName.text =
                        StringBuilder(nickname).append(" (${matchDetail.matchResult})")
                    tvFirstScore.text = shoot.goalTotal.toString()
                    getUserInfo(nickname, tvFirstLevel, itemView.context)
                }
                it.matchInfoBean[1].run {
                    tvSecondName.text =
                        StringBuilder(nickname).append(" (${matchDetail.matchResult})")
                    tvSecondScore.text = shoot.goalTotal.toString()
                    getUserInfo(nickname, tvSecondLevel, itemView.context)
                }
            }

            llMatchRecord.setOnClickListener {
                val dlgBinding = DialogMatchDetailBinding.inflate((itemView.context as Activity).layoutInflater)
                val dlg = AlertDialog.Builder(itemView.context).create().apply {
                    setView(dlgBinding.root)
                    setCancelable(false)
                }

                dlgBinding.apply {
                    rvFirstMatchDetail.apply {
                        layoutManager = LinearLayoutManager(itemView.context)
                        firstBeanMap[position]?.let { adapter = MatchDetailAdapter(it) }
                    }

                    rvSecondMatchDetail.apply {
                        layoutManager = LinearLayoutManager(itemView.context)
                        secondBeanMap[position]?.let { adapter = MatchDetailAdapter(it) }
                    }

                    btnCloseMatch.setOnClickListener {
                        dlg.dismiss()
                    }

                    btnMatchSquad.setOnClickListener {
                        showSquadDialog(itemView.context, position)
                    }
                }
                dlg.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getUserInfo(nickname: String, textView: TextView, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val service = RetroClient.getInstance().create(FifaService::class.java).getUserInfo(nickname)
            service.enqueue(object : Callback<UserInfoDTO> {
                override fun onResponse(call: Call<UserInfoDTO>, response: Response<UserInfoDTO>) {
                    if(response.isSuccessful) {
                        response.body()?.let {
                            (context as Activity).runOnUiThread {
                                textView.text = it.level.toString()
                            }
                        }
                    }
                    else {
                        Log.e("response fail")
                    }
                }

                override fun onFailure(call: Call<UserInfoDTO>, t: Throwable) {
                    Log.e("rest fail")
                }
            })
        }
    }

    private fun getGoalInfo(shootDetailBean: List<ShootDetailBean>) : List<ShootDetailBean> {
        val goalInfoList: MutableList<ShootDetailBean> = mutableListOf()
        shootDetailBean.forEach { bean ->
            // if result value is 3, it is goal.
            if(bean.result == 3)
                goalInfoList.add(bean)
        }

        return goalInfoList
    }

    private fun showSquadDialog(context: Context, position: Int) {
        val dlgBinding = DialogSquadBinding.inflate((context as Activity).layoutInflater)
        val dlg = AlertDialog.Builder(context).create().apply {
            setView(dlgBinding.root)
            setCancelable(false)
        }

        dlgBinding.apply {
            rvFirstSquad.apply {
                layoutManager = LinearLayoutManager(context)
                firstSquadMap[position]?.let { adapter = SquadAdapter(it) }
            }

            rvSecondSquad.apply {
                layoutManager = LinearLayoutManager(context)
                secondSquadMap[position]?.let { adapter = SquadAdapter(it) }
            }

            btnSquadClose.setOnClickListener { dlg.dismiss() }
        }
        dlg.show()
    }
}