package com.jeepchief.clubhouse.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlayerDTO(
    @Expose
    @SerializedName("id")
    var spid: String,

    @Expose
    @SerializedName("name")
    var name: String
)
