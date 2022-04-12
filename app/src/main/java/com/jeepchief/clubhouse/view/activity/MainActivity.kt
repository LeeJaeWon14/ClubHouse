package com.jeepchief.clubhouse.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.databinding.ActivityMainBinding
import com.jeepchief.clubhouse.databinding.DialogInputNicknameBinding
import com.jeepchief.clubhouse.model.database.MyDatabase
import com.jeepchief.clubhouse.model.database.matchtype.MatchTypeEntity
import com.jeepchief.clubhouse.model.database.metadata.player.PlayerEntity
import com.jeepchief.clubhouse.model.database.userinfo.UserInfoEntity
import com.jeepchief.clubhouse.model.rest.FifaService
import com.jeepchief.clubhouse.model.rest.RetroClient
import com.jeepchief.clubhouse.model.rest.dto.MatchTypeDTO
import com.jeepchief.clubhouse.model.rest.dto.PlayerDTO
import com.jeepchief.clubhouse.model.rest.dto.UserInfoDTO
import com.jeepchief.clubhouse.util.Log
import com.jeepchief.clubhouse.util.Pref
import com.jeepchief.clubhouse.view.matchrecord.MatchRecordFragment
import com.jeepchief.clubhouse.view.traderecord.TradeRecordFragment
import com.jeepchief.clubhouse.view.user.UserInfoFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    companion object {
        const val TEST = "101001075"
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPref()
        initUi()
    }

    private fun checkPref() {
        Pref.getInstance(this)?.let {
            if(!it.getBoolean(Pref.FIRST_LOGIN))
                showInputDialog()
            if(!it.getBoolean(Pref.META_DATA_DOWNLOAD))
                downloadMetadata()
        }
    }

    private var time : Long = 0
    override fun onBackPressed() { //뒤로가기 클릭 시 종료 메소드
        if(System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis()
            Toast.makeText(this@MainActivity, "한번 더 누르면 종료합니다", Toast.LENGTH_SHORT).show()
        }
        else if(System.currentTimeMillis() - time < 2000) {
            this.finishAffinity()
        }
    }

    private fun initUi() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = MyDatabase.getInstance(this@MainActivity).getPlayerDAO()
                .selectPlayer(TEST)
            if(list.isEmpty())
                Log.e("not find player")
            else
                Log.e("player is ${list[0].name}")
        }
        binding.apply {
            bnvBottoms.setOnItemSelectedListener { item ->
                val transaction = supportFragmentManager.beginTransaction()
                when (item.itemId) {
                    R.id.menu_user -> {
                        transaction.replace(R.id.fl_fragment, UserInfoFragment()).commit()
                    }
                    R.id.menu_trade -> {
                        transaction.replace(R.id.fl_fragment, TradeRecordFragment()).commit()
                    }
                    R.id.menu_match -> {
                        transaction.replace(R.id.fl_fragment, MatchRecordFragment()).commit()
                    }
                }

                return@setOnItemSelectedListener true
            }
            bnvBottoms.selectedItemId = R.id.menu_user
        }
    }

    private fun showInputDialog() {
        val dlgBinding = DialogInputNicknameBinding.inflate(layoutInflater)
        val dlg = AlertDialog.Builder(this).create().apply {
            setView(dlgBinding.root)
            setCancelable(false)
        }

        dlgBinding.apply {
            tvOk.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val service = RetroClient.getInstance().create(FifaService::class.java)
                    val call = service.getUserInfo(edtNickname.text.toString())
                    call.enqueue(object : Callback<UserInfoDTO> {
                            override fun onResponse(
                                call: Call<UserInfoDTO>,
                                response: Response<UserInfoDTO>
                            ) {

                                response.body()?.let {
                                    Log.e("id=${it.accessId}, nickname=${it.nickname}, level=${it.level}")
                                    Pref.getInstance(this@MainActivity)?.setValue(Pref.FIRST_LOGIN, true)

                                    CoroutineScope(Dispatchers.IO).launch {
                                        MyDatabase.getInstance(this@MainActivity).getUserInfoDAO().run {
                                            val checkList = checkDistinctUserInfo(it.nickname)
                                            if(checkList.isEmpty()) {
                                                insertUserInfo(UserInfoEntity(it.nickname, it.accessId, it.level))
                                            }
                                        }
                                    }
                                } ?: run {
                                    Log.e("response body is null!!")
                                }

                                runOnUiThread {
                                    Toast.makeText(this@MainActivity, "성공", Toast.LENGTH_SHORT)
                                        .show()
                                    dlg.dismiss()
                                }
                            }

                            override fun onFailure(call: Call<UserInfoDTO>, t: Throwable) = restFailureMessage(t)
                        })
                }
            }
            tvExit.setOnClickListener {
                finishAffinity()
            }
        }
        dlg.show()
    }

    private fun downloadMetadata() {
        binding.pbDownloading.isVisible = true

        val service = RetroClient.getInstance().create(FifaService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val call = service?.getPlayerData()
            call?.enqueue(object : Callback<List<PlayerDTO>> {
                override fun onResponse(
                    call: Call<List<PlayerDTO>>,
                    response: Response<List<PlayerDTO>>
                ) {
                    Log.e("Meta data downloading..")
                    CoroutineScope(Dispatchers.Default).launch {
                        response.body()?.let {
                            it.forEach { player ->
                                withContext(Dispatchers.IO) {
                                    MyDatabase.getInstance(this@MainActivity).getPlayerDAO()
                                        .insertPlayer(PlayerEntity(player.spid, player.name))
                                }
                            }
                        }
                    }
                    Pref.getInstance(this@MainActivity)?.setValue(Pref.META_DATA_DOWNLOAD, true)
                }

                override fun onFailure(call: Call<List<PlayerDTO>>, t: Throwable) = restFailureMessage(t)
            })
        }
        CoroutineScope(Dispatchers.IO).launch {
            val call = service?.getMatchType()
            call?.enqueue(object : Callback<List<MatchTypeDTO>> {
                override fun onResponse(
                    call: Call<List<MatchTypeDTO>>,
                    response: Response<List<MatchTypeDTO>>
                ) {
                    response.body()?.let { list ->
                        CoroutineScope(Dispatchers.Default).launch {
                            list.forEach { dto ->
                                withContext(Dispatchers.IO) {
                                    MyDatabase.getInstance(this@MainActivity).getMatchTypeDAO()
                                        .insertMatchType(MatchTypeEntity(dto.matchtype, dto.desc))
                                }
                            }
                        }
                    } ?: run {
                        Log.e("matchtype data is null")
                    }
                }

                override fun onFailure(call: Call<List<MatchTypeDTO>>, t: Throwable) = restFailureMessage(t)
            })
        }
        binding.pbDownloading.isVisible = false
    }

    private fun restFailureMessage(t: Throwable) {
        runOnUiThread {
            Toast.makeText(this@MainActivity, "실패", Toast.LENGTH_SHORT)
                .show()
        }
        Log.e("${t.message}")
    }
}