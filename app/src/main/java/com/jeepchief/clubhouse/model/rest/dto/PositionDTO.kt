package com.jeepchief.clubhouse.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PositionDTO(

    @Expose
    @SerializedName("spposition")
    var spposition: Int,
    @Expose
    @SerializedName("desc")
    val desc: String
)
