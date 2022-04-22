package com.jeepchief.clubhouse.view.matchrecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.jeepchief.clubhouse.R
import com.jeepchief.clubhouse.databinding.FragmentMatchRecordBinding
import com.jeepchief.clubhouse.model.database.MyDatabase
import com.jeepchief.clubhouse.model.rest.FifaService
import com.jeepchief.clubhouse.model.rest.RetroClient
import com.jeepchief.clubhouse.util.Log
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
                rvMatchRecord.apply {
                    layoutManager = null
                    adapter = null
                }
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
            val matchId = MyDatabase.getInstance(requireContext()).getMatchTypeDAO()
                .selectMatchTypeId(binding.spMatchType.selectedItem?.toString()!!)

            val service = RetroClient.getInstance().create(FifaService::class.java)
            val call = service?.getMatchId(
                "474b77ce34d7d22cf449d09c",
                matchId
            )
            call?.enqueue(object : Callback<List<String>> {
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                    if(response.isSuccessful) {
                        response.body()?.let {
                            it.forEach { id ->
                                Log.e("matchId is ${id}")
                            }
                        } ?: run {
                            Log.e("matchId response is null")
                        }
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Log.e("rest fail")
                }
            })
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // no-op
    }

    private suspend fun getMatchTypes() : Deferred<List<String>> {
        return CoroutineScope(Dispatchers.IO).async {
            MyDatabase.getInstance(requireContext()).getMatchTypeDAO()
                .selectAllMatchType()
        }
    }
}