package com.jeepchief.clubhouse.view.traderecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jeepchief.clubhouse.databinding.FragmentTradeRecordBinding
import com.jeepchief.clubhouse.view.traderecord.adapter.TradePagerAdapter

class TradeRecordFragment : Fragment() {
    private var _binding: FragmentTradeRecordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTradeRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initUi() {
        binding.apply {
            vpTradePager.adapter = TradePagerAdapter(requireActivity())

            TabLayoutMediator(tlTradeLayout, vpTradePager) { tab, position ->
                tab.text = when(position) {
                    0 -> SellFragment().javaClass.simpleName
                    1 -> BuyFragment().javaClass.simpleName
                    else -> ""
                }
            }.attach()
        }
    }
}