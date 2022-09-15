package com.jeepchief.clubhouse.view.user

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.databinding.FragmentUserInfoBinding
import com.jeepchief.clubhouse.databinding.LayoutGradeGuideBinding
import com.jeepchief.clubhouse.model.database.MyDatabase
import com.jeepchief.clubhouse.model.database.metadata.division.DivisionEntity
import com.jeepchief.clubhouse.model.database.userinfo.UserInfoEntity
import com.jeepchief.clubhouse.model.rest.FifaService
import com.jeepchief.clubhouse.model.rest.RetroClient
import com.jeepchief.clubhouse.model.rest.dto.MaxDivisionDTO
import com.jeepchief.clubhouse.util.Log
import com.jeepchief.clubhouse.util.Pref
import com.jeepchief.clubhouse.view.activity.MainActivity
import com.jeepchief.clubhouse.view.user.adapter.GradeGuideAdapter
import com.jeepchief.clubhouse.view.user.adapter.MaxDivisionAdapter
import com.jeepchief.clubhouse.viewmodel.FifaViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IndexOutOfBoundsException

class UserInfoFragment : Fragment() {
    private var _binding : FragmentUserInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FifaViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        divisionList = CoroutineScope(Dispatchers.IO).async {
            MyDatabase.getInstance(requireContext()).getDivisionDAO()
                .selectAllDivisionName()
        }

        binding.apply {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    MyDatabase.getInstance(requireContext()).getUserInfoDAO().selectUserInfo()[0].also { user ->
                        withContext(Dispatchers.Main) {
                            tvNickname.text = StringBuilder("Name : ").append(user.nickname).toString()
                            tvLevel.text = StringBuilder("Lv. ").append(user.level.toString()).toString()
                            selectMaxDivision(user.uid)
                        }
                    }
                } catch(e: IndexOutOfBoundsException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "유저정보 없음", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            btnGradeGuide.setOnClickListener {
               CoroutineScope(Dispatchers.Main).launch {
                   val dlgBinding = LayoutGradeGuideBinding.inflate(layoutInflater)
                   val dlg = AlertDialog.Builder(requireContext()).create().apply {
                       setView(dlgBinding.root)
                       setCancelable(false)
                   }


                   dlgBinding.apply {
                       rvGradeGuide.apply {
                           layoutManager = LinearLayoutManager(requireContext())
                           adapter = GradeGuideAdapter(divisionList.await())
                       }
                       btnCloseGuide.setOnClickListener {
                           dlg.dismiss()
                       }
                   }
                   dlg.show()
               }
            }

            btnLogout.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.str_confirmed_logout))
                    .setPositiveButton(R.string.str_dialog_ok) { dialogInterface: DialogInterface, i: Int ->
                        CoroutineScope(Dispatchers.IO).launch {
                            MyDatabase.getInstance(requireContext()).getUserInfoDAO().run {
                                val userEntity = selectUserInfo().get(0)
                                deleteUserInfo(userEntity)
                            }
                            Pref.getInstance(requireContext())?.removeValue(Pref.USER_ID)
                            withContext(Dispatchers.Main) {
                                requireActivity().finishAffinity()
                                startActivity(Intent(requireContext(), MainActivity::class.java))
                            }
                        }
                    }
                    .setNegativeButton(R.string.str_dialog_cancel, null)
                    .setCancelable(false)
                    .show()
            }
        }
    }

    private lateinit var divisionList: Deferred<List<DivisionEntity>>

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun selectMaxDivision(uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val service = RetroClient.getInstance().create(FifaService::class.java)
            val call = service?.getMaxDivision(uid)
            call?.enqueue(object : Callback<List<MaxDivisionDTO>> {
                override fun onResponse(
                    call: Call<List<MaxDivisionDTO>>,
                    response: Response<List<MaxDivisionDTO>>
                ) {
                    response.body()?.let {
                        requireActivity().runOnUiThread {
                            binding.rvMaxDivision.apply {
                                layoutManager = LinearLayoutManager(requireContext())
                                adapter = MaxDivisionAdapter(it)
                            }
                        }

                    } ?: run {
                        Log.e("response is null!!")
                    }
                }

                override fun onFailure(call: Call<List<MaxDivisionDTO>>, t: Throwable) {
                    Log.e("Failed getting max division, message is ${t.message}")
                }
            })
        }
    }

    private suspend fun getUserInfo() : UserInfoEntity {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            MyDatabase.getInstance(requireContext()).getUserInfoDAO()
                .selectUserInfo().get(0)
        }
        return deferred.await()
    }
}