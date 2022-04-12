package com.jeepchief.clubhouse.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MaxDivisionDTO(
    @Expose
    @SerializedName("matchType")
    var matchType: Int,
    @Expose
    @SerializedName("division")
    var division: Int,
    @Expose
    @SerializedName("achievementDate")
    val achievementDate: String
)