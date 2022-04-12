package com.jeepchief.clubhouse.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DivisionDTO(
    @Expose
    @SerializedName("divisionId")
    var divisionId: String,

    @Expose
    @SerializedName("divisionName")
    var divisionName: String
)