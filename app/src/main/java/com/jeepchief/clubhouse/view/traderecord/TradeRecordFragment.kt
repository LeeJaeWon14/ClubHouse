package com.jeepchief.clubhouse.view.traderecord

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jeepchief.clubhouse.R
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
            vpTradePager.adapter = TradePagerAdapter(requireActivity()).apply {
                addFragment(BuyFragment(), BuyFragment())
            }

            TabLayoutMediator(tlTradeLayout, vpTradePager) { tab, position ->
                tab.text = when(position) {
                    0 -> getString(R.string.str_trade_buy)
                    1 -> getString(R.string.str_trade_sell)
                    else -> ""
                }
            }.attach()
            tlTradeLayout.setSelectedTabIndicatorColor(requireContext().getColor(R.color.primary_green))
        }
    }
}