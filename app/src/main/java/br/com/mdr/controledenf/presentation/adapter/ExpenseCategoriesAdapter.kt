package br.com.mdr.controledenf.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.mdr.base.domain.ExpenseCategory
import br.com.mdr.controledenf.databinding.TextEditItemBinding

class ExpenseCategoriesAdapter(
    private val onItemClick: ((category: ExpenseCategory) -> Unit)
): ListAdapter<ExpenseCategory, ExpenseCategoriesAdapter.ExpenseCategoriesViewHolder>(ExpenseCategoriesCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseCategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TextEditItemBinding.inflate(inflater, parent, false)
        return ExpenseCategoriesViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ExpenseCategoriesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExpenseCategoriesViewHolder(
        private val binding: TextEditItemBinding,
        private val onItemClick: (category: ExpenseCategory) -> Unit): RecyclerView.ViewHolder(binding.root) {

        fun bind(category: ExpenseCategory) {
            binding.txtItem.text = category.name
            binding.btnEdit.setOnClickListener {
                onItemClick.invoke(category)
            }
        }
    }

    private class ExpenseCategoriesCallback : DiffUtil.ItemCallback<ExpenseCategory>() {
        override fun areItemsTheSame(oldItem: ExpenseCategory, newItem: ExpenseCategory) =
            oldItem.name == newItem.name &&
                    oldItem.description == newItem.description

        override fun areContentsTheSame(oldItem: ExpenseCategory, newItem: ExpenseCategory) =
            oldItem == newItem
    }
}