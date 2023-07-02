package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.mdr.base.domain.ExpenseCategory
import br.com.mdr.controledenf.databinding.FragmentRegisterExpenseCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val EXPENSE_PARAM = "expense"

    class RegisterExpenseCategoryFragment : BottomSheetDialogFragment() {
    private var expense: ExpenseCategory? = null

    companion object {
        fun newInstance(expense: ExpenseCategory? = null) =
            RegisterExpenseCategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXPENSE_PARAM, expense)
                }
            }
    }

    var registerClick: ((ExpenseCategory) -> Unit)? = null
    var binding: FragmentRegisterExpenseCategoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            expense = it.getParcelable(EXPENSE_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterExpenseCategoryBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupListeners() {
        binding?.apply {
            btnRegister.setOnClickListener {
                registerClick?.invoke(getExpenseCategory(this))
                dismiss()
            }
            edtName.addTextChangedListener(textChangeFocusListener())
            edtDescription.addTextChangedListener(textChangeFocusListener())
        }
    }

    private fun setupView() {
        expense?.let {
            binding?.apply {
                edtName.setText(it.name)
                edtDescription.setText(it.description)
            }
        }
    }

    private fun getExpenseCategory(binding: FragmentRegisterExpenseCategoryBinding) =
        with(binding) {
            val name = edtName.text.toString()
            val description = edtDescription.text.toString()
            expense?.apply {
                this.name = name
                this.description = description
            } ?: ExpenseCategory(
                name = name,
                description = description
            )
        }

    private fun textChangeFocusListener() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding?.apply {
                btnRegister.isEnabled =
                    edtName.text.toString().isNotEmpty() &&
                            edtDescription.text.toString().isNotEmpty()
            }
        }

        override fun afterTextChanged(s: Editable?) = Unit
    }
}