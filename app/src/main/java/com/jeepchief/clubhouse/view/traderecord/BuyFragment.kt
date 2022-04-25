package com.jeepchief.clubhouse.view.traderecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.clubhouse.databinding.FragmentBuyBinding
import com.jeepchief.clubhouse.model.database.MyDatabase
import com.jeepchief.clubhouse.model.rest.FifaService
import com.jeepchief.clubhouse.model.rest.NetworkConstants
import com.jeepchief.clubhouse.model.rest.RetroClient
import com.jeepchief.clubhouse.model.rest.dto.TradeRecordDTO
import com.jeepchief.clubhouse.util.Log
import com.jeepchief.clubhouse.view.traderecord.adapter.TradeListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuyFragment : Fragment() {
    private var _binding: FragmentBuyBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Toast.makeText(requireContext(), arguments?.getInt("page").toString(), Toast.LENGTH_SHORT).show()

        CoroutineScope(Dispatchers.IO).launch {
            val service = RetroClient.getInstance().create(FifaService::class.java)
            service?.getTradeRecord(
                getUserId(),
                checkTradeType(arguments?.getInt("page"))
            )?.enqueue(object : Callback<List<TradeRecordDTO>> {
                override fun onResponse(
                    call: Call<List<TradeRecordDTO>>,
                    response: Response<List<TradeRecordDTO>>
                ) {
                    if(response.isSuccessful) {
                        response.body()?.let {
                            requireActivity().runOnUiThread {
                                binding.rvTradeList.apply {
                                    layoutManager = LinearLayoutManager(requireContext())
                                    adapter = TradeListAdapter(it)
                                }
                            }
                        } ?: run {
                            Log.e("response is null")
                        }
                    }
                }

                override fun onFailure(call: Call<List<TradeRecordDTO>>, t: Throwable) {
                    Log.e("rest fail, message is ${t.message}")
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkTradeType(page: Int?) : String {
        return when(page) {
            0 -> NetworkConstants.TRADE_BUY
            1 -> NetworkConstants.TRADE_SELL
            else -> ""
        }
    }

    private suspend fun getUserId() : String {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            MyDatabase.getInstance(requireContext()).getUserInfoDAO()
                .selectUserInfo().get(0).uid
        }
        return deferred.await()
    }
}