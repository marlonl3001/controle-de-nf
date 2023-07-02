package br.com.mdr.controledenf.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.mdr.controledenf.presentation.fragment.ExpenseHistoryFragment
import br.com.mdr.controledenf.presentation.fragment.InvoiceHistoryFragment

class HistoryPageAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                InvoiceHistoryFragment()
            }
            else -> {
                ExpenseHistoryFragment()
            }
        }
    }
}