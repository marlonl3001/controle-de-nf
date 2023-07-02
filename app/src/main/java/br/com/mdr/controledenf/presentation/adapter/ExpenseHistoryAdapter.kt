package br.com.mdr.controledenf.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.mdr.base.domain.Expense
import br.com.mdr.controledenf.databinding.HistoryEditItemBinding
import br.com.mdr.controledenf.utils.extension.toCurrencyString

class ExpenseHistoryAdapter(
    private val onEditClick: ((expense: Expense) -> Unit),
    private val onDeleteClick: ((expense: Expense) -> Unit)
): ListAdapter<Expense, ExpenseHistoryAdapter.ExpenseViewHolder>(ExpenseCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HistoryEditItemBinding.inflate(inflater, parent, false)
        return ExpenseViewHolder(binding, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExpenseViewHolder(
        private val binding: HistoryEditItemBinding,
        private val onEditClick: (expense: Expense) -> Unit,
        private val onDeleteClick: (expense: Expense) -> Unit): RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: Expense) {
            binding.apply {
                txtItem.text = expense.name
                txtValue.text = expense.value.toCurrencyString()
                txtCompany.text = expense.company
                btnEdit.setOnClickListener {
                    onEditClick.invoke(expense)
                }
                btnDelete.setOnClickListener {
                    onDeleteClick.invoke(expense)
                }
            }
        }
    }

    private class ExpenseCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) =
            oldItem.name == newItem.name &&
                    oldItem.category == newItem.category &&
                    oldItem.company == newItem.company &&
                    oldItem.value == newItem.value &&
                    oldItem.paymentDate == newItem.paymentDate &&
                    oldItem.competencyDate == newItem.competencyDate

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) =
            oldItem == newItem
    }
}