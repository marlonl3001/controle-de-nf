package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mdr.base.domain.Expense
import br.com.mdr.controledenf.databinding.FragmentExpenseHistoryBinding
import br.com.mdr.controledenf.presentation.adapter.ExpenseHistoryAdapter
import br.com.mdr.controledenf.presentation.viewmodel.HistoryViewModel
import br.com.mdr.controledenf.utils.SpacesItemDecoration
import br.com.mdr.controledenf.utils.extension.showBottomSheet
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ExpenseHistoryFragment : Fragment() {
    private var binding: FragmentExpenseHistoryBinding? = null

    private val viewModel: HistoryViewModel by sharedViewModel()
    private val adapter = ExpenseHistoryAdapter(onExpenseClick(), onDeleteClick())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseHistoryBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupObservers() {
        viewModel.expenses.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.getExpenses()
    }

    private fun setupRecyclerView() {
        binding?.apply {
            recyclerExpenses.addItemDecoration(
                SpacesItemDecoration(orientation = LinearLayoutManager.VERTICAL)
            )
            recyclerExpenses.adapter = adapter
        }
    }

    private fun onExpenseClick(): (expense: Expense?) -> Unit = {
        val bottomSheet = RegisterExpenseFragment.newInstance(it).apply {
            registerClick = { e ->
                viewModel.saveExpense(e)
            }
        }
        showBottomSheet(bottomSheet)
    }

    private fun onDeleteClick(): (expense: Expense) -> Unit = {
        viewModel.deleteExpense(it)
    }

}