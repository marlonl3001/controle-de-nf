package br.com.mdr.controledenf.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.mdr.base.domain.PartnerCompany
import br.com.mdr.controledenf.databinding.TextEditItemBinding

class PartnerCompaniesAdapter(
    private val onItemClick: ((company: PartnerCompany) -> Unit)
): ListAdapter<PartnerCompany, PartnerCompaniesAdapter.PartnerCompaniesViewHolder>(PartnerCompaniesCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerCompaniesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TextEditItemBinding.inflate(inflater, parent, false)
        return PartnerCompaniesViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: PartnerCompaniesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PartnerCompaniesViewHolder(
        private val binding: TextEditItemBinding,
        private val onItemClick: (company: PartnerCompany) -> Unit): RecyclerView.ViewHolder(binding.root) {

        fun bind(company: PartnerCompany) {
            binding.txtItem.text = company.companyName
            binding.btnEdit.setOnClickListener {
                onItemClick.invoke(company)
            }
        }
    }

    private class PartnerCompaniesCallback : DiffUtil.ItemCallback<PartnerCompany>() {
        override fun areItemsTheSame(oldItem: PartnerCompany, newItem: PartnerCompany) =
            oldItem.companyName == newItem.companyName &&
                    oldItem.document == newItem.document &&
                    oldItem.fantasyName == newItem.fantasyName

        override fun areContentsTheSame(oldItem: PartnerCompany, newItem: PartnerCompany) =
            oldItem == newItem
    }
}