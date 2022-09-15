package com.jeepchief.clubhouse.view.matchrecord.adapter

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
import com.jeepchief.clubhouse.model.rest.dto.PlayerBean
import com.jeepchief.clubhouse.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SquadAdapter(_squadList: List<PlayerBean>) : RecyclerView.Adapter<SquadAdapter.SquadViewHolder>() {
    private var squadList: List<PlayerBean> = _squadList.sortedBy { it.spPosition }

    class SquadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPlayerImage: ImageView = view.findViewById(R.id.iv_squad_player)
        val tvPlayerName: TextView = view.findViewById(R.id.tv_squad_player_name)
        val tvPlayerPosition: TextView = view.findViewById(R.id.tv_squad_player_position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_squad, parent, false)
        return SquadViewHolder(view)
    }

    override fun onBindViewHolder(holder: SquadViewHolder, position: Int) {
        holder.apply {
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(itemView.context)
                    .load(String.format(NetworkConstants.PLAYER_ACTION_SHOT_URL, squadList[position].spId))
                    .error(R.drawable.ic_launcher_foreground)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            CoroutineScope(Dispatchers.Main).launch {
                                Glide.with(itemView.context)
                                    .load(String.format(NetworkConstants.PLAYER_IMAGE_URL, makePid(squadList[position].spId.toString())))
                                    .centerCrop()
                                    .into(ivPlayerImage)
                            }

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
                    .into(ivPlayerImage)

                tvPlayerName.text = getPlayerName(squadList[position].spId.toString(), itemView.context).plus("\t +${squadList[position].spGrade}")
                tvPlayerPosition.text = getPlayerPosition(squadList[position].spPosition, itemView.context)
            }
        }
    }

    override fun getItemCount(): Int {
        return squadList.size
    }

    private suspend fun getPlayerName(spid: String, context: Context) : String {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            MyDatabase.getInstance(context).getPlayerDAO()
                .selectPlayer(spid)
        }
        return deferred.await()
    }

    private suspend fun getPlayerPosition(spid: Int, context: Context) : String {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            MyDatabase.getInstance(context).getPositionDAO()
                .selectPosition(spid)
        }
        return deferred.await().desc
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