package br.com.mdr.controledenf.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.mdr.controledenf.databinding.PreferencesCheckItemBinding
import br.com.mdr.controledenf.databinding.PreferencesTextItemBinding

private const val TEXT_ITEM = 0
private const val BOOLEAN_ITEM = 1

class PreferencesAdapter(
    private val onItemClick: ((position: Int) -> Unit),
    private val onCheckChange: ((checked: Boolean) -> Unit)
): ListAdapter<String, PreferencesAdapter.PreferencesViewHolder>(PreferencesCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreferencesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = if (viewType == TEXT_ITEM) {
            PreferencesTextItemBinding.inflate(inflater, parent, false)
        } else {
            PreferencesCheckItemBinding.inflate(inflater, parent, false)
        }
        return PreferencesViewHolder(binding, onItemClick, onCheckChange)
    }

    override fun onBindViewHolder(holder: PreferencesViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun getItemViewType(position: Int): Int =
        if (position == itemCount - 1) BOOLEAN_ITEM else TEXT_ITEM

    class PreferencesViewHolder(
        private val binding: ViewDataBinding,
        private val onItemClick: (position: Int) -> Unit,
        private val onCheckChange: (checked: Boolean) -> Unit): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, position: Int) {
            if (binding is PreferencesTextItemBinding) {
                binding.txtItem.text = item
                itemView.setOnClickListener { onItemClick(position) }
            } else if (binding is PreferencesCheckItemBinding) {
                binding.txtItem.text = item
                binding.checkAlert.setOnCheckedChangeListener { _, isChecked ->
                    onCheckChange(isChecked)
                }
            }
        }
    }

    private class PreferencesCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}