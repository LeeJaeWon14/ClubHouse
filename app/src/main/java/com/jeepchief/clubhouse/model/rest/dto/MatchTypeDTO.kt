package com.jeepchief.clubhouse.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MatchTypeDTO(
    @Expose
    @SerializedName("matchtype")
    var matchtype: Int,
    @Expose
    @SerializedName("desc")
    val desc: String
)