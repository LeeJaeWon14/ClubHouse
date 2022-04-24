package com.jeepchief.clubhouse.view.traderecord.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.model.database.MyDatabase
import com.jeepchief.clubhouse.model.rest.NetworkConstants
import com.jeepchief.clubhouse.model.rest.dto.TradeRecordDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class TradeListAdapter(private val list: List<TradeRecordDTO>) : RecyclerView.Adapter<TradeListAdapter.TradeListViewHolder>() {
    private var NOT_FIND_ACTION_SHOT = false
    class TradeListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPlayer: ImageView = view.findViewById(R.id.iv_player)
        val tvPlayerName: TextView = view.findViewById(R.id.tv_goal_player)
        val tvPlayerPrice: TextView = view.findViewById(R.id.tv_player_price)
        val tvTradeDate: TextView = view.findViewById(R.id.tv_trade_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trade_list, parent, false)
        return TradeListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TradeListViewHolder, position: Int) {
        holder.apply {
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(itemView.context)
                    .load(String.format(NetworkConstants.PLAYER_ACTION_SHOT_URL, list[position].spid))
                    .error(R.drawable.ic_launcher_foreground)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            NOT_FIND_ACTION_SHOT = true

                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {

                            return false
                        }
                    })
                    .thumbnail(0.2f)
                    .centerCrop()
                    .into(ivPlayer)

//                if(NOT_FIND_ACTION_SHOT) {
//                    Glide.with(itemView.context)
//                        .load(String.format(NetworkConstants.PLAYER_IMAGE_URL, makePid(list[position].spid.toString())))
//                        .centerCrop()
//                        .into(ivPlayer)
//                    NOT_FIND_ACTION_SHOT = false
//                }

                tvPlayerName.text = getPlayerName(list[position].spid.toString(), itemView.context).plus("\t +${list[position].grade}")
                tvPlayerPrice.text = makeComma(list[position].value.toString()).plus("BP")
                tvTradeDate.text = list[position].tradeDate.replace("T", " / ")
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private suspend fun getPlayerName(spid: String, context: Context) : String {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            MyDatabase.getInstance(context).getPlayerDAO()
                .selectPlayer(spid)
        }
        return deferred.await()
    }

    private fun makeComma(price : String) : String {
        //소숫점이 존재하거나 천 단위 이하일 경우 생략
        if(price.contains(".") || price.length < 4) {
            return price
        }
        val formatter = DecimalFormat("###,###")
        return formatter.format(price.toLong())
    }

    private fun makePid(spid: String) : String {
        val uid = spid.substring(3)
        var count = 0
        for(char in uid.toCharArray()) {
            if(char != '0') break
            else count ++
        }
        return uid.substring(count)
    }
}