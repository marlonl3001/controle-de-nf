package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mdr.controledenf.R
import br.com.mdr.controledenf.databinding.FragmentPreferencesBinding
import br.com.mdr.controledenf.presentation.adapter.PreferencesAdapter
import br.com.mdr.controledenf.presentation.viewmodel.AuthViewModel
import br.com.mdr.controledenf.presentation.viewmodel.PreferencesViewModel
import br.com.mdr.controledenf.utils.SpacesItemDecoration
import br.com.mdr.controledenf.utils.extension.navigateTo
import br.com.mdr.controledenf.utils.extension.successSnack
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PreferencesFragment : Fragment() {
    private var binding: FragmentPreferencesBinding? = null
    private val authViewModel: AuthViewModel by sharedViewModel()
    private val preferencesViewModel: PreferencesViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreferencesBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupObservers() {
        authViewModel.company.observe(viewLifecycleOwner) {
            binding?.company = it
        }

        preferencesViewModel.successMessage.observe(viewLifecycleOwner) {
            it?.apply {
                view?.successSnack(getString(this))
            }
        }
    }

    private fun setupView() {
        binding?.apply {
            val adapter = PreferencesAdapter(
                onItemClick = {
                    verifyItemClick(it)
                },
                onCheckChange = {

                }
            )
            recyclerPreferences.addItemDecoration(
                SpacesItemDecoration(orientation = LinearLayoutManager.VERTICAL)
            )
            recyclerPreferences.adapter = adapter
            adapter.submitList(resources.getStringArray(R.array.preference_items).toList())
        }
    }

    private fun verifyItemClick(position: Int) {
        when (position) {
            PreferenceItems.PARTNER_COMPANIES.index -> {
                navigateTo(PreferencesFragmentDirections
                    .actionPreferencesToPartnerCompaniesFragmentFragment())
            }
            PreferenceItems.CATEGORIES.index -> {
                navigateTo(PreferencesFragmentDirections
                    .actionPreferencesToExpenseCategoriesFragment())
            }
        }
    }

    private enum class PreferenceItems(val index: Int) {
        PARTNER_COMPANIES(0),
        CATEGORIES(1)
    }
}