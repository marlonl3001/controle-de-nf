package br.com.mdr.controledenf.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.mdr.base.domain.Invoice
import br.com.mdr.controledenf.databinding.HistoryEditItemBinding
import br.com.mdr.controledenf.utils.extension.toCurrencyString

class InvoiceHistoryAdapter(
    private val onItemClick: ((invoice: Invoice) -> Unit),
    private val onDeleteClick: ((invoice: Invoice) -> Unit)
): ListAdapter<Invoice, InvoiceHistoryAdapter.InvoiceViewHolder>(InvoiceCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HistoryEditItemBinding.inflate(inflater, parent, false)
        return InvoiceViewHolder(binding, onItemClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class InvoiceViewHolder(
        private val binding: HistoryEditItemBinding,
        private val onItemClick: (invoice: Invoice) -> Unit,
        private val onDeleteClick: (invoice: Invoice) -> Unit): RecyclerView.ViewHolder(binding.root) {

        fun bind(invoice: Invoice) {
            binding.apply {
                txtItem.text = invoice.description
                txtValue.text = invoice.value.toCurrencyString()
                txtCompany.text = invoice.company
                btnEdit.setOnClickListener {
                    onItemClick.invoke(invoice)
                }
                btnDelete.setOnClickListener {
                    onDeleteClick.invoke(invoice)
                }
            }
        }
    }

    private class InvoiceCallback : DiffUtil.ItemCallback<Invoice>() {
        override fun areItemsTheSame(oldItem: Invoice, newItem: Invoice) =
            oldItem.company == newItem.company &&
                    oldItem.value == newItem.value &&
                    oldItem.description == newItem.description &&
                    oldItem.month == newItem.month &&
                    oldItem.number == newItem.number &&
                    oldItem.receiveDate == newItem.receiveDate

        override fun areContentsTheSame(oldItem: Invoice, newItem: Invoice) =
            oldItem == newItem
    }
}