package com.jeepchief.clubhouse.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TradeRecordDTO(
    @Expose
    @SerializedName("tradeDate")
    val tradeDate: String,
    @Expose
    @SerializedName("saleSn")
    val saleSn: String,
    @Expose
    @SerializedName("spid")
    var spid: Int,
    @Expose
    @SerializedName("grade")
    var grade: Int,
    @Expose
    @SerializedName("value")
    var value: Long
)