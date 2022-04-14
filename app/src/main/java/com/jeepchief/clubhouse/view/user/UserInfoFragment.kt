package com.jeepchief.clubhouse.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.clubhouse.databinding.FragmentUserInfoBinding
import com.jeepchief.clubhouse.model.database.MyDatabase
import com.jeepchief.clubhouse.model.database.userinfo.UserInfoEntity
import com.jeepchief.clubhouse.model.rest.FifaService
import com.jeepchief.clubhouse.model.rest.RetroClient
import com.jeepchief.clubhouse.model.rest.dto.MaxDivisionDTO
import com.jeepchief.clubhouse.util.Log
import com.jeepchief.clubhouse.view.user.adapter.MaxDivisionAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IndexOutOfBoundsException

class UserInfoFragment : Fragment() {
    private var _binding : FragmentUserInfoBinding? = null
    private val binding get() = _binding!!
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

        binding.apply {

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    getUserInfo().also { user ->
                        tvNickname.text = user.nickname
                        tvLevel.text = user.level.toString()
                        selectMaxDivision(user.uid)
                    }
                } catch(e: IndexOutOfBoundsException) {
                    Toast.makeText(requireContext(), "유저정보 없음", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

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