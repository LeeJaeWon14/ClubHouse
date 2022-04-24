package com.jeepchief.clubhouse.view.matchrecord.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.model.rest.FifaService
import com.jeepchief.clubhouse.model.rest.RetroClient
import com.jeepchief.clubhouse.model.rest.dto.MatchBean
import com.jeepchief.clubhouse.model.rest.dto.UserInfoDTO
import com.jeepchief.clubhouse.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchRecordAdapter(private val list: List<String>) : RecyclerView.Adapter<MatchRecordAdapter.MatchRecordViewHolder>() {
    private var userDTO : UserInfoDTO? = null

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
                                it.matchInfoBean[0].apply {
                                    tvFirstName.text = StringBuilder(nickname).append(" (${matchDetail.matchResult})")
                                    tvFirstLevel.text = getUserInfo(nickname).level.toString()
                                    tvFirstScore.text = shoot.goalTotal.toString()
                                }
                                it.matchInfoBean[1].apply {
                                    tvSecondName.text = StringBuilder(nickname).append(" (${matchDetail.matchResult})")
                                    tvSecondLevel.text = getUserInfo(nickname).level.toString()
                                    tvSecondScore.text = shoot.goalTotal.toString()
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
                Toast.makeText(itemView.context, itemView.context.getString(R.string.str_not_implemented_yet), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getUserInfo(nickname: String) : UserInfoDTO {
        Log.e("nickname is $nickname")
        var dto: UserInfoDTO? = null
        CoroutineScope(Dispatchers.IO).launch {
            val service = RetroClient.getInstance().create(FifaService::class.java).getUserInfo(nickname)
            service.enqueue(object : Callback<UserInfoDTO> {
                override fun onResponse(call: Call<UserInfoDTO>, response: Response<UserInfoDTO>) {
                    if(response.isSuccessful) {
                        response.body()?.let { dto = it.copy() }
                        Log.e("dto is ${dto.toString()}")
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
        return dto!!
    }
}