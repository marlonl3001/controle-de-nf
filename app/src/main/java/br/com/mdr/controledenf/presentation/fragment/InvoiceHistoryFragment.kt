package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mdr.base.domain.Invoice
import br.com.mdr.controledenf.databinding.FragmentInvoiceHistoryBinding
import br.com.mdr.controledenf.presentation.adapter.InvoiceHistoryAdapter
import br.com.mdr.controledenf.presentation.viewmodel.HistoryViewModel
import br.com.mdr.controledenf.utils.SpacesItemDecoration
import br.com.mdr.controledenf.utils.extension.showBottomSheet
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class InvoiceHistoryFragment : Fragment() {
    private var binding: FragmentInvoiceHistoryBinding? = null

    private val viewModel: HistoryViewModel by sharedViewModel()
    private val adapter = InvoiceHistoryAdapter(onInvoiceClick(), onDeleteClick())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvoiceHistoryBinding.inflate(inflater)
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
        viewModel.invoices.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.getInvoices()
    }

    private fun setupRecyclerView() {
        binding?.apply {
            recyclerInvoices.addItemDecoration(
                SpacesItemDecoration(orientation = LinearLayoutManager.VERTICAL)
            )
            recyclerInvoices.adapter = adapter
        }
    }

    private fun onInvoiceClick(): (invoice: Invoice?) -> Unit = {
        val bottomSheet = RegisterInvoiceFragment.newInstance(it).apply {
            registerClick = { i ->
                viewModel.saveInvoice(i)
            }
        }
        showBottomSheet(bottomSheet)
    }

    private fun onDeleteClick(): (invoice: Invoice) -> Unit = {
        viewModel.deleteInvoice(it)
    }
}