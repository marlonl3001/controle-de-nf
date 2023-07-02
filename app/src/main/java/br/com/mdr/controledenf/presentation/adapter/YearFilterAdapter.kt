package br.com.mdr.controledenf.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.mdr.controledenf.databinding.TextItemBinding

class YearFilterAdapter(
    private val onItemClick: ((item: String) -> Unit)
): ListAdapter<String, YearFilterAdapter.YearFilterViewHolder>(YearFilterCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearFilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = TextItemBinding.inflate(inflater, parent, false)

        return YearFilterViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: YearFilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class YearFilterViewHolder(
        private val binding: TextItemBinding,
        private val onItemClick: (item: String) -> Unit): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.txtItem.text = item
            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    private class YearFilterCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}