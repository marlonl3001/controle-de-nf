package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mdr.base.domain.PartnerCompany
import br.com.mdr.controledenf.databinding.FragmentPartnerCompaniesBinding
import br.com.mdr.controledenf.presentation.adapter.PartnerCompaniesAdapter
import br.com.mdr.controledenf.presentation.viewmodel.PreferencesViewModel
import br.com.mdr.controledenf.utils.SpacesItemDecoration
import br.com.mdr.controledenf.utils.extension.showBottomSheet
import br.com.mdr.controledenf.utils.extension.successSnack
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PartnerCompaniesFragment : Fragment() {
    private var binding: FragmentPartnerCompaniesBinding? = null

    private val preferencesViewModel: PreferencesViewModel by sharedViewModel()
    private val adapter = PartnerCompaniesAdapter(onItemClick = onCompanyClick())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPartnerCompaniesBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupObservers() {
        with(preferencesViewModel) {
            successMessage.observe(viewLifecycleOwner) {
                it?.apply {
                    view?.successSnack(getString(this))
                }
            }
            companies.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

            getPartnerCompanies()
        }
    }

    private fun setupListeners() {
        binding?.fabAdd?.setOnClickListener {
            onCompanyClick().invoke(null)
        }
    }

    private fun setupView() {
        binding?.apply {
            recyclerCompanies.addItemDecoration(
                SpacesItemDecoration(orientation = LinearLayoutManager.VERTICAL)
            )
            recyclerCompanies.adapter = adapter
        }
    }

    private fun onCompanyClick(): (company: PartnerCompany?) -> Unit = {
        val bottomSheet = RegisterPartnerCompanyFragment.newInstance(it).apply {
            registerClick = { c ->
                preferencesViewModel.savePartnerCompany(c)
            }
        }
        showBottomSheet(bottomSheet)
    }
}