package com.jeepchief.clubhouse.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.databinding.ActivityMainBinding
import com.jeepchief.clubhouse.databinding.DialogInputNicknameBinding
import com.jeepchief.clubhouse.model.database.MyDatabase
import com.jeepchief.clubhouse.model.database.metadata.division.DivisionEntity
import com.jeepchief.clubhouse.model.database.metadata.matchtype.MatchTypeEntity
import com.jeepchief.clubhouse.model.database.metadata.player.PlayerEntity
import com.jeepchief.clubhouse.model.database.metadata.position.PositionEntity
import com.jeepchief.clubhouse.model.database.userinfo.UserInfoEntity
import com.jeepchief.clubhouse.model.rest.FifaService
import com.jeepchief.clubhouse.model.rest.RetroClient
import com.jeepchief.clubhouse.model.rest.dto.*
import com.jeepchief.clubhouse.util.Log
import com.jeepchief.clubhouse.util.Pref
import com.jeepchief.clubhouse.view.matchrecord.MatchRecordFragment
import com.jeepchief.clubhouse.view.traderecord.TradeRecordFragment
import com.jeepchief.clubhouse.view.user.UserInfoFragment
import com.jeepchief.clubhouse.viewmodel.FifaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: FifaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPref()
        initUi()
    }

    override fun onPause() {
        super.onPause()
        Log.e("onPause")
    }

    private fun checkPref() {
        Pref.getInstance(this)?.let {
            if(it.getString(Pref.USER_ID) == "") {
                showLoginDialog()
            } else viewModel.userId = Pref.getInstance(this)?.getString(Pref.USER_ID).toString()
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
        binding.apply {
            bnvBottoms.setOnItemSelectedListener { item ->
                var nowFragment: Fragment? = null
                supportFragmentManager.fragments.forEach { fragment ->
                    if(fragment.isVisible) nowFragment = fragment
                }
                val transaction = supportFragmentManager.beginTransaction()
                when (item.itemId) {
                    R.id.menu_user -> {
                        if(nowFragment is UserInfoFragment)
                            return@setOnItemSelectedListener true
                        else transaction.replace(R.id.fl_fragment, UserInfoFragment()).commit()
                    }
                    R.id.menu_trade -> {
                        if(nowFragment is TradeRecordFragment)
                            return@setOnItemSelectedListener true
                        else transaction.replace(R.id.fl_fragment, TradeRecordFragment()).commit()
                    }
                    R.id.menu_match -> {
                        if(nowFragment is MatchRecordFragment)
                            return@setOnItemSelectedListener true
                        else transaction.replace(R.id.fl_fragment, MatchRecordFragment()).commit()
                    }
                }

                return@setOnItemSelectedListener true
            }

            if(Pref.getInstance(this@MainActivity)?.getString(Pref.USER_ID) != "")
                binding.bnvBottoms.selectedItemId = R.id.menu_user
        }
    }

    private fun showLoginDialog() {
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
                                Pref.getInstance(this@MainActivity)
                                    ?.setValue(Pref.USER_ID, it.accessId)
                                viewModel.userId = it.accessId

//                                CoroutineScope(Dispatchers.IO).launch {
//                                    MyDatabase.getInstance(this@MainActivity).getUserInfoDAO().run {
//                                        val checkList = checkDistinctUserInfo(it.nickname)
//                                        if (checkList.isEmpty()) {
//                                            insertUserInfo(
//                                                UserInfoEntity(
//                                                    it.nickname,
//                                                    it.accessId,
//                                                    it.level
//                                                )
//                                            )
//                                        }
//                                    }
//                                }

                                viewModel.getUserInfo(this@MainActivity).run {
                                    val checkList = checkDistinctUserInfo(it.nickname)
                                    if (checkList.isEmpty()) {
                                        insertUserInfo(
                                            UserInfoEntity(
                                                it.nickname,
                                                it.accessId,
                                                it.level
                                            )
                                        )
                                    }
                                }

                                runOnUiThread {
                                    dlg.dismiss()
                                    binding.bnvBottoms.selectedItemId = R.id.menu_user
                                }
                            } ?: run {
                                Log.e("response body is null!!")
                                runOnUiThread {
                                    Toast.makeText(this@MainActivity, "닉네임을 다시 확인해주세요.", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<UserInfoDTO>, t: Throwable) =
                            restFailureMessage(t)
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
        Log.e("Meta data downloading..")
        binding.pbDownloading.isVisible = true

        val service = RetroClient.getInstance().create(FifaService::class.java)

        // Get players data from server
        CoroutineScope(Dispatchers.IO).launch {
            val call = service?.getPlayerData()
            call?.enqueue(object : Callback<List<PlayerDTO>> {
                override fun onResponse(
                    call: Call<List<PlayerDTO>>,
                    response: Response<List<PlayerDTO>>
                ) {
                    CoroutineScope(Dispatchers.Default).launch {
                        if(response.isSuccessful) {
                            response.body()?.let {
                                it.forEach { player ->
                                    withContext(Dispatchers.IO) {
                                        MyDatabase.getInstance(this@MainActivity).getPlayerDAO()
                                            .insertPlayer(PlayerEntity(player.spid, player.name))
                                    }
                                }
                            }
                            Log.e("Player data is Downloading done.")
                        }
                    }
                    Pref.getInstance(this@MainActivity)?.setValue(Pref.META_DATA_DOWNLOAD, true)
                }

                override fun onFailure(call: Call<List<PlayerDTO>>, t: Throwable) = restFailureMessage(t)
            })
        }

        // Get match type from server
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
                        Log.e("matchtype data is Downloading done.")
                    } ?: run {
                        Log.e("matchtype data is null")
                    }
                }

                override fun onFailure(call: Call<List<MatchTypeDTO>>, t: Throwable) = restFailureMessage(t)
            })
        }

        // Get division data from server
        CoroutineScope(Dispatchers.IO).launch {
            val call = service?.getDivisionData()
            call?.enqueue(object : Callback<List<DivisionDTO>> {
                override fun onResponse(
                    call: Call<List<DivisionDTO>>,
                    response: Response<List<DivisionDTO>>
                ) {
                    if(response.isSuccessful) {
                        response.body()?.let { list ->
                            CoroutineScope(Dispatchers.Default).launch {
                                list.forEach { dto ->
                                    withContext(Dispatchers.IO) {
                                        MyDatabase.getInstance(this@MainActivity).getDivisionDAO()
                                            .insertDivision(DivisionEntity(dto.divisionId, dto.divisionName))
                                    }
                                }
                            }
                            Log.e("Division data is Downloading done.")
                        }
                    }
                }

                override fun onFailure(call: Call<List<DivisionDTO>>, t: Throwable) = restFailureMessage(t)
            })
        }

        // Get position data from server
        CoroutineScope(Dispatchers.IO).launch {
            val call = service?.getPosition()
            call?.enqueue(object : Callback<List<PositionDTO>> {
                override fun onResponse(
                    call: Call<List<PositionDTO>>,
                    response: Response<List<PositionDTO>>
                ) {
                    if(response.isSuccessful) {
                        CoroutineScope(Dispatchers.Default).launch {
                            response.body()?.let {
                                it.forEach { dto ->
                                    withContext(Dispatchers.IO) {
                                        MyDatabase.getInstance(this@MainActivity).getPositionDAO()
                                            .insertPosition(PositionEntity(dto.spposition, dto.desc))
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<PositionDTO>>, t: Throwable) = restFailureMessage(t)
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