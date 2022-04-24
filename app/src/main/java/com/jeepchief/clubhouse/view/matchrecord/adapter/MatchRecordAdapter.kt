package com.jeepchief.clubhouse.view.matchrecord.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.databinding.DialogMatchDetailBinding
import com.jeepchief.clubhouse.model.rest.FifaService
import com.jeepchief.clubhouse.model.rest.RetroClient
import com.jeepchief.clubhouse.model.rest.dto.MatchBean
import com.jeepchief.clubhouse.model.rest.dto.ShootDetailBean
import com.jeepchief.clubhouse.model.rest.dto.UserInfoDTO
import com.jeepchief.clubhouse.util.Log
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchRecordAdapter(private val list: List<String>) : RecyclerView.Adapter<MatchRecordAdapter.MatchRecordViewHolder>() {
    private var userDTO : UserInfoDTO? = null
    private val firstBeanMap = hashMapOf<Int, List<ShootDetailBean>>()
    private val secondBeanMap = hashMapOf<Int, List<ShootDetailBean>>()

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
            CoroutineScope(Dispatchers.IO).launch {
                val service = RetroClient.getInstance().create(FifaService::class.java)
                service.getMatchRecord(list[position]).enqueue(object : Callback<MatchBean> {
                    override fun onResponse(
                        call: Call<MatchBean>,
                        response: Response<MatchBean>
                    ) {
                        if(response.isSuccessful) {
                            response.body()?.let {
                                tvPlayDate.text = it.matchDate.replace("T", " / ")
                                firstBeanMap.put(position, getGoalInfo(it.matchInfoBean[0].shootDetailBean))
                                secondBeanMap.put(position, getGoalInfo(it.matchInfoBean[1].shootDetailBean))
                                it.matchInfoBean[0].run {
                                    tvFirstName.text = StringBuilder(nickname).append(" (${matchDetail.matchResult})")
                                    tvFirstScore.text = shoot.goalTotal.toString()
                                    getUserInfo(nickname, tvFirstLevel, itemView.context)
                                }
                                it.matchInfoBean[1].run {
                                    tvSecondName.text = StringBuilder(nickname).append(" (${matchDetail.matchResult})")
                                    tvSecondScore.text = shoot.goalTotal.toString()
                                    getUserInfo(nickname, tvSecondLevel, itemView.context)
                                }
                            } ?: run {
                                Log.e("response body is null!!")
                            }
                        } else {
                            Log.e("response is fail")
                        }
                    }

                    override fun onFailure(call: Call<MatchBean>, t: Throwable) {
                        Log.e("match record is fail, message is ${t.message}")
                    }
                })
            }
            llMatchRecord.setOnClickListener {
//                Toast.makeText(itemView.context, itemView.context.getString(R.string.str_not_implemented_yet), Toast.LENGTH_SHORT).show()

                val dlg = AlertDialog.Builder(itemView.context).create()
                val dlgBinding = DialogMatchDetailBinding.inflate((itemView.context as Activity).layoutInflater)
                dlg.setView(dlgBinding.root)

                dlgBinding.apply {
                    rvFirstMatchDetail.apply {
                        layoutManager = LinearLayoutManager(itemView.context)
                        firstBeanMap[position]?.let { adapter = MatchDetailAdapter(it) }
                    }

                    rvSecondMatchDetail.apply {
                        layoutManager = LinearLayoutManager(itemView.context)
                        secondBeanMap[position]?.let { adapter = MatchDetailAdapter(it) }
                    }
                }

//                dlg.setCancelable(false)
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
            if(bean.result == 3)
                goalInfoList.add(bean)
        }

        return goalInfoList
    }
}