package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mdr.base.domain.ExpenseCategory
import br.com.mdr.controledenf.databinding.FragmentExpenseCategoriesBinding
import br.com.mdr.controledenf.presentation.adapter.ExpenseCategoriesAdapter
import br.com.mdr.controledenf.presentation.viewmodel.PreferencesViewModel
import br.com.mdr.controledenf.utils.SpacesItemDecoration
import br.com.mdr.controledenf.utils.extension.showBottomSheet
import br.com.mdr.controledenf.utils.extension.successSnack
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ExpenseCategoriesFragment : Fragment() {
    private var binding: FragmentExpenseCategoriesBinding? = null

    private val preferencesViewModel: PreferencesViewModel by sharedViewModel()
    private val adapter = ExpenseCategoriesAdapter(onItemClick = onExpenseClick())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseCategoriesBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupObservers() {
        with(preferencesViewModel) {
            successMessage.observe(viewLifecycleOwner) {
                it?.apply {
                    view?.successSnack(getString(this))
                }
            }
            categories.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

            getExpenseCategories()
        }
    }

    private fun setupListeners() {
        binding?.fabAdd?.setOnClickListener {
            onExpenseClick().invoke(null)
        }
    }

    private fun setupView() {
        binding?.apply {
            recyclerCategories.addItemDecoration(
                SpacesItemDecoration(orientation = LinearLayoutManager.VERTICAL)
            )
            recyclerCategories.adapter = adapter
        }
    }

    private fun onExpenseClick(): (category: ExpenseCategory?) -> Unit = {
        val bottomSheet = RegisterExpenseCategoryFragment.newInstance(it).apply {
            registerClick = { e ->
                preferencesViewModel.saveCategory(e)
            }
        }
        showBottomSheet(bottomSheet)
    }
}