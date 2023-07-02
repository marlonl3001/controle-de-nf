package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import br.com.mdr.base.domain.Invoice
import br.com.mdr.base.domain.PartnerCompany
import br.com.mdr.controledenf.R
import br.com.mdr.controledenf.databinding.FragmentRegisterInvoiceBinding
import br.com.mdr.controledenf.presentation.viewmodel.HistoryViewModel
import br.com.mdr.controledenf.utils.MaskUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val INVOICE_PARAM = "invoice"

class RegisterInvoiceFragment : BottomSheetDialogFragment() {

    private var invoice: Invoice? = null

    companion object {
        fun newInstance(invoice: Invoice? = null) =
            RegisterInvoiceFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(INVOICE_PARAM, invoice)
                }
            }
    }

    private var binding: FragmentRegisterInvoiceBinding? = null

    private val viewModel: HistoryViewModel by sharedViewModel()

    var registerClick: ((Invoice) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterInvoiceBinding.inflate(inflater)
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
        viewModel.partnerCompanies.observe(viewLifecycleOwner) {
            setupSpinner(companies = it)
        }
        viewModel.getPartnerCompanies()
    }

    private fun setupSpinner(companies: List<PartnerCompany>) {
        val companiesAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item,
            getCompanies(companies)
        )

        binding?.companySpinner?.adapter = companiesAdapter

        val monthsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.months)
        )
        binding?.monthSpinner?.adapter = monthsAdapter
    }

    private fun setupListeners() {
        binding?.apply {
            edtReceiveDate.addTextChangedListener(
                MaskUtil.insertMask(edtReceiveDate, MaskUtil.MaskType.DATE_BR)
            )
            btnRegister.setOnClickListener {
                viewModel.saveInvoice(getInvoice(this))
                dismiss()
            }
        }
    }

    private fun getCompanies(companies: List<PartnerCompany>): List<String> {
        val items = arrayListOf<String>()
        for (company in companies) {
            items.add(company.companyName)
        }
        return items
    }

    private fun getInvoice(binding: FragmentRegisterInvoiceBinding) =
        with(binding) {
            val number = edtNumber.text.toString().toInt()
            val value = MaskUtil.removeBrCurrencyMask(edtValue.text.toString()).toDouble()
            val receiveDate = edtReceiveDate.text.toString()
            val competencyMonth = monthSpinner.selectedItem.toString()
            val company = companySpinner.selectedItem.toString()
            val description = edtDescription.text.toString()
            invoice?.apply {
                this.number = number
                this.value = value
                this.description = description
                this.month = competencyMonth
                this.receiveDate = receiveDate
                this.company = company
            } ?: Invoice(
                number = number,
                value = value,
                description = description,
                month = competencyMonth,
                receiveDate = receiveDate,
                company = company
            )
        }

}