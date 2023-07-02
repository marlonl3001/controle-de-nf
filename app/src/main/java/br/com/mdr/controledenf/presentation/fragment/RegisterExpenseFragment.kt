package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import br.com.mdr.base.domain.Expense
import br.com.mdr.base.domain.ExpenseCategory
import br.com.mdr.base.domain.PartnerCompany
import br.com.mdr.controledenf.databinding.FragmentRegisterExpenseBinding
import br.com.mdr.controledenf.presentation.viewmodel.HistoryViewModel
import br.com.mdr.controledenf.utils.MaskUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val EXPENSE_PARAM = "expense"

class RegisterExpenseFragment : BottomSheetDialogFragment() {

    private var expense: Expense? = null

    companion object {
        fun newInstance(expense: Expense? = null) =
            RegisterExpenseFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXPENSE_PARAM, expense)
                }
            }
    }

    private var binding: FragmentRegisterExpenseBinding? = null

    private val viewModel: HistoryViewModel by sharedViewModel()

    var registerClick: ((Expense) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterExpenseBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupObservers() {
        viewModel.expenseCategories.observe(viewLifecycleOwner) {
            setupSpinner(categories = it)
        }
        viewModel.partnerCompanies.observe(viewLifecycleOwner) {
            setupSpinner(companies = it)
        }
        viewModel.getExpenseCategories()
        viewModel.getPartnerCompanies()
    }

    private fun setupSpinner(categories: List<ExpenseCategory>? = null,
                             companies: List<PartnerCompany>? = null) {
        categories?.let {
            val categoriesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
                getCategories(it))
            binding?.categorySpinner?.adapter = categoriesAdapter
        }

        companies?.let {
            val companiesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
                getCompanies(it))

            binding?.companySpinner?.adapter = companiesAdapter
        }
    }

    private fun setupListeners() {
        binding?.apply {
            edtCompetencyDate.addTextChangedListener(
                MaskUtil.insertMask(edtCompetencyDate, MaskUtil.MaskType.DATE_BR)
            )
            edtPaymentDate.addTextChangedListener(
                MaskUtil.insertMask(edtPaymentDate, MaskUtil.MaskType.DATE_BR)
            )
            btnRegister.setOnClickListener {
                viewModel.saveExpense(getExpense(this))
                dismiss()
            }
        }
    }

    private fun getCategories(categories: List<ExpenseCategory>): List<String> {
        val items = arrayListOf<String>()
        for (category in categories) {
            items.add(category.name)
        }
        return items
    }

    private fun getCompanies(companies: List<PartnerCompany>): List<String> {
        val items = arrayListOf<String>()
        for (company in companies) {
            items.add(company.companyName)
        }
        return items
    }

    private fun getExpense(binding: FragmentRegisterExpenseBinding) =
        with(binding) {
            val name = edtName.text.toString()
            val category = categorySpinner.selectedItem.toString()
            val value = MaskUtil.removeBrCurrencyMask(edtValue.text.toString()).toDouble()
            val paymentDate = edtPaymentDate.text.toString()
            val competencyDate = edtCompetencyDate.text.toString()
            val company = companySpinner.selectedItem.toString()
            expense?.apply {
                this.name = name
                this.value = value
                this.category = category
                this.paymentDate = paymentDate
                this.competencyDate = competencyDate
                this.company = company
            } ?: Expense(
                name = name,
                value = value,
                category = category,
                paymentDate = paymentDate,
                competencyDate = competencyDate,
                company = company
            )
        }

}