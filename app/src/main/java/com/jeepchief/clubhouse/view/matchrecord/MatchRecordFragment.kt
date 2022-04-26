package com.jeepchief.clubhouse.view.matchrecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.databinding.FragmentMatchRecordBinding
import com.jeepchief.clubhouse.model.database.MyDatabase
import com.jeepchief.clubhouse.model.rest.FifaService
import com.jeepchief.clubhouse.model.rest.RetroClient
import com.jeepchief.clubhouse.util.Log
import com.jeepchief.clubhouse.util.Pref
import com.jeepchief.clubhouse.view.matchrecord.adapter.MatchRecordAdapter
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchRecordFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentMatchRecordBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMatchRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {

            binding.apply {
//                if(Pref.getInstance(requireContext())?.getBoolean(Pref.SHOWED_VOLTA_MESSAGE) == false) {
//                    Toast.makeText(requireContext(), getString(R.string.str_volta_is_not_stable), Toast.LENGTH_SHORT).show()
//                    Pref.getInstance(requireContext())?.setValue(Pref.SHOWED_VOLTA_MESSAGE, true)
//                }
                spMatchType.apply {
                    val spAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, getMatchTypes().await())
                    adapter = spAdapter
                    setSelection(0, false)
                    onItemSelectedListener = this@MatchRecordFragment
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val matchTypeId = MyDatabase.getInstance(requireContext()).getMatchTypeDAO()
                .selectMatchTypeId(binding.spMatchType.selectedItem?.toString()!!)

            val service = RetroClient.getInstance().create(FifaService::class.java)
            val call = service?.getMatchId(
                getUserId(),
                matchTypeId
            )
            call?.enqueue(object : Callback<List<String>> {
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                    if(response.isSuccessful) {
                        response.body()?.let {
                            binding.apply {
                                rvMatchRecord.apply {
                                    layoutManager = LinearLayoutManager(requireContext())
                                    adapter = MatchRecordAdapter(it)
                                }
                                tvSelectGuide.isVisible = false
                            }
                        } ?: run {
                            Log.e("matchId response is null")
                            Toast.makeText(requireContext(), "response body is null!!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("matchId response is null")
                        Toast.makeText(requireContext(), "response is fail", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Log.e("rest fail")
                }
            })
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        /* no-op */
    }

    private suspend fun getMatchTypes() : Deferred<List<String>> {
        return CoroutineScope(Dispatchers.IO).async {
            MyDatabase.getInstance(requireContext()).getMatchTypeDAO()
                .selectAllMatchType()
        }
    }

    private suspend fun getUserId() : String {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            MyDatabase.getInstance(requireContext()).getUserInfoDAO()
                .selectUserInfo().get(0).uid
        }
        return deferred.await()
    }

    fun showEmptyText() {
        binding.tvSelectGuide.apply {
            isVisible = true
            text = getString(R.string.str_not_found_record)
        }
    }
}