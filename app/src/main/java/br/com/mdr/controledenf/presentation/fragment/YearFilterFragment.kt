package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.mdr.controledenf.R
import br.com.mdr.controledenf.databinding.FragmentBottomSheetYearFilterBinding
import br.com.mdr.controledenf.presentation.adapter.YearFilterAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class YearFilterFragment: BottomSheetDialogFragment() {
    private var binding: FragmentBottomSheetYearFilterBinding? = null

    var yearClick: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetYearFilterBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupRecyclerView() {
        binding?.recyclerYears?.apply {
            adapter = YearFilterAdapter {
                yearClick?.invoke(it)
                dismiss()
            }.apply {
                submitList(resources.getStringArray(R.array.years).toList())
            }
        }
    }
}