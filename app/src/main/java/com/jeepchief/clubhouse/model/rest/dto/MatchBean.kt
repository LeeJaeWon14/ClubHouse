package com.jeepchief.clubhouse.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MatchBean(

    @Expose
    @SerializedName("matchId")
    val matchId: String,
    @Expose
    @SerializedName("matchDate")
    val matchDate: String,
    @Expose
    @SerializedName("matchType")
    var matchType: Int,
    @Expose
    @SerializedName("matchInfo")
    var matchInfoBean: List<MatchInfoBean>
)

data class MatchInfoBean(
    @Expose
    @SerializedName("accessId")
    val accessId: String,
    @Expose
    @SerializedName("nickname")
    val nickname: String,
    @Expose
    @SerializedName("matchDetail")
    var matchDetail: MatchDetailBean,
    @Expose
    @SerializedName("shoot")
    var shoot: ShootBean,
    @Expose
    @SerializedName("shootDetail")
    var shootDetailBean: List<ShootDetailBean>,
    @Expose
    @SerializedName("pass")
    var pass: PassBean,
    @Expose
    @SerializedName("defence")
    var defence: DefenceBean,
    @Expose
    @SerializedName("player")
    var playerBean: List<PlayerBean>
)

data class PlayerBean(
    @Expose
    @SerializedName("spId")
    var spId: Int,
    @Expose
    @SerializedName("spPosition")
    var spPosition: Int,
    @Expose
    @SerializedName("spGrade")
    var spGrade: Int,
    @Expose
    @SerializedName("status")
    var status: StatusBean
)

data class StatusBean(
    @Expose
    @SerializedName("shoot")
    var shoot: Int,
    @Expose
    @SerializedName("effectiveShoot")
    var effectiveShoot: Int,
    @Expose
    @SerializedName("assist")
    var assist: Int,
    @Expose
    @SerializedName("goal")
    var goal: Int,
    @Expose
    @SerializedName("dribble")
    var dribble: Int,
    @Expose
    @SerializedName("intercept")
    var intercept: Int,
    @Expose
    @SerializedName("defending")
    var defending: Int,
    @Expose
    @SerializedName("passTry")
    var passTry: Int,
    @Expose
    @SerializedName("passSuccess")
    var passSuccess: Int,
    @Expose
    @SerializedName("dribbleTry")
    var dribbleTry: Int,
    @Expose
    @SerializedName("dribbleSuccess")
    var dribbleSuccess: Int,
    @Expose
    @SerializedName("ballPossesionTry")
    var ballPossesionTry: Int,
    @Expose
    @SerializedName("ballPossesionSuccess")
    var ballPossesionSuccess: Int,
    @Expose
    @SerializedName("aerialTry")
    var aerialTry: Int,
    @Expose
    @SerializedName("aerialSuccess")
    var aerialSuccess: Int,
    @Expose
    @SerializedName("blockTry")
    var blockTry: Int,
    @Expose
    @SerializedName("block")
    var block: Int,
    @Expose
    @SerializedName("tackleTry")
    var tackleTry: Int,
    @Expose
    @SerializedName("tackle")
    var tackle: Int,
    @Expose
    @SerializedName("yellowCards")
    var yellowCards: Int,
    @Expose
    @SerializedName("redCards")
    var redCards: Int,
    @Expose
    @SerializedName("spRating")
    val spRating: Double
)

data class DefenceBean(
    @Expose
    @SerializedName("blockTry")
    var blockTry: Int,
    @Expose
    @SerializedName("blockSuccess")
    var blockSuccess: Int,
    @Expose
    @SerializedName("tackleTry")
    var tackleTry: Int,
    @Expose
    @SerializedName("tackleSuccess")
    var tackleSuccess: Int
)

data class PassBean(
    @Expose
    @SerializedName("passTry")
    var passTry: Int,
    @Expose
    @SerializedName("passSuccess")
    var passSuccess: Int,
    @Expose
    @SerializedName("shortPassTry")
    var shortPassTry: Int,
    @Expose
    @SerializedName("shortPassSuccess")
    var shortPassSuccess: Int,
    @Expose
    @SerializedName("longPassTry")
    var longPassTry: Int,
    @Expose
    @SerializedName("longPassSuccess")
    var longPassSuccess: Int,
    @Expose
    @SerializedName("bouncingLobPassTry")
    var bouncingLobPassTry: Int,
    @Expose
    @SerializedName("bouncingLobPassSuccess")
    var bouncingLobPassSuccess: Int,
    @Expose
    @SerializedName("drivenGroundPassTry")
    var drivenGroundPassTry: Int,
    @Expose
    @SerializedName("drivenGroundPassSuccess")
    var drivenGroundPassSuccess: Int,
    @Expose
    @SerializedName("throughPassTry")
    var throughPassTry: Int,
    @Expose
    @SerializedName("throughPassSuccess")
    var throughPassSuccess: Int,
    @Expose
    @SerializedName("lobbedThroughPassTry")
    var lobbedThroughPassTry: Int,
    @Expose
    @SerializedName("lobbedThroughPassSuccess")
    var lobbedThroughPassSuccess: Int
)

data class ShootDetailBean(
    @Expose
    @SerializedName("goalTime")
    var goalTime: Int,
    @Expose
    @SerializedName("x")
    val x: Double,
    @Expose
    @SerializedName("y")
    val y: Double,
    @Expose
    @SerializedName("type")
    var type: Int,
    @Expose
    @SerializedName("result")
    var result: Int,
    @Expose
    @SerializedName("spId")
    var spId: Int,
    @Expose
    @SerializedName("spGrade")
    var spGrade: Int,
    @Expose
    @SerializedName("spLevel")
    var spLevel: Int,
    @Expose
    @SerializedName("spIdType")
    var spIdType: Boolean,
    @Expose
    @SerializedName("assist")
    var assist: Boolean,
    @Expose
    @SerializedName("assistSpId")
    var assistSpId: Int,
    @Expose
    @SerializedName("assistX")
    val assistX: Double,
    @Expose
    @SerializedName("assistY")
    val assistY: Double,
    @Expose
    @SerializedName("hitPost")
    var hitPost: Boolean,
    @Expose
    @SerializedName("inPenalty")
    var inPenalty: Boolean
)

data class ShootBean(
    @Expose
    @SerializedName("shootTotal")
    var shootTotal: Int,
    @Expose
    @SerializedName("effectiveShootTotal")
    var effectiveShootTotal: Int,
    @Expose
    @SerializedName("shootOutScore")
    var shootOutScore: Int,
    @Expose
    @SerializedName("goalTotal")
    var goalTotal: Int,
    @Expose
    @SerializedName("goalTotalDisplay")
    var goalTotalDisplay: Int,
    @Expose
    @SerializedName("ownGoal")
    var ownGoal: Int,
    @Expose
    @SerializedName("shootHeading")
    var shootHeading: Int,
    @Expose
    @SerializedName("goalHeading")
    var goalHeading: Int,
    @Expose
    @SerializedName("shootFreekick")
    var shootFreekick: Int,
    @Expose
    @SerializedName("goalFreekick")
    var goalFreekick: Int,
    @Expose
    @SerializedName("shootInPenalty")
    var shootInPenalty: Int,
    @Expose
    @SerializedName("goalInPenalty")
    var goalInPenalty: Int,
    @Expose
    @SerializedName("shootOutPenalty")
    var shootOutPenalty: Int,
    @Expose
    @SerializedName("goalOutPenalty")
    var goalOutPenalty: Int,
    @Expose
    @SerializedName("shootPenaltyKick")
    var shootPenaltyKick: Int,
    @Expose
    @SerializedName("goalPenaltyKick")
    var goalPenaltyKick: Int
)

data class MatchDetailBean(
    @Expose
    @SerializedName("seasonId")
    var seasonId: Int,
    @Expose
    @SerializedName("matchResult")
    val matchResult: String,
    @Expose
    @SerializedName("matchEndType")
    var matchEndType: Int,
    @Expose
    @SerializedName("systemPause")
    var systemPause: Int,
    @Expose
    @SerializedName("foul")
    var foul: Int,
    @Expose
    @SerializedName("injury")
    var injury: Int,
    @Expose
    @SerializedName("redCards")
    var redCards: Int,
    @Expose
    @SerializedName("yellowCards")
    var yellowCards: Int,
    @Expose
    @SerializedName("dribble")
    var dribble: Int,
    @Expose
    @SerializedName("cornerKick")
    var cornerKick: Int,
    @Expose
    @SerializedName("possession")
    var possession: Int,
    @Expose
    @SerializedName("offsideCount")
    var offsideCount: Int,
    @Expose
    @SerializedName("averageRating")
    val averageRating: Double,
    @Expose
    @SerializedName("controller")
    val controller: String
)
