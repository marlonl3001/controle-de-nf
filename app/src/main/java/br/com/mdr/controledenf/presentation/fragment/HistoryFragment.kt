package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.mdr.controledenf.databinding.FragmentHistoryBinding
import br.com.mdr.controledenf.presentation.adapter.HistoryPageAdapter
import com.google.android.material.tabs.TabLayoutMediator

class HistoryFragment : Fragment() {
    private var binding: FragmentHistoryBinding? = null

    private val tabTitles = arrayOf("Notas Fiscais", "Despesas")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupViewPager() {
        val adapter = HistoryPageAdapter(this)
        binding?.apply {
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }
    }

}