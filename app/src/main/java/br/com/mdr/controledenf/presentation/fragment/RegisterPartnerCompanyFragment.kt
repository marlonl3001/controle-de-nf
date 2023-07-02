package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.mdr.base.domain.PartnerCompany
import br.com.mdr.controledenf.databinding.FragmentRegisterPartnerCompanyBinding
import br.com.mdr.controledenf.utils.MaskUtil
import br.com.mdr.controledenf.utils.MaskUtil.Companion.maskCNPJ
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val COMPANY = "company"

class RegisterPartnerCompanyFragment : BottomSheetDialogFragment() {
    private var company: PartnerCompany? = null

    companion object {
        fun newInstance(company: PartnerCompany? = null) =
            RegisterPartnerCompanyFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(COMPANY, company)
                }
            }
    }

    var registerClick: ((PartnerCompany) -> Unit)? = null
    var binding: FragmentRegisterPartnerCompanyBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            company = it.getParcelable(COMPANY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterPartnerCompanyBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupListeners() {
        binding?.apply {
            btnRegister.setOnClickListener {
                registerClick?.invoke(getPartnerCompany(this))
                dismiss()
            }
            edtCnpj.addTextChangedListener(textChangeFocusListener())
            edtFantasyName.addTextChangedListener(textChangeFocusListener())
            edtCompanyName.addTextChangedListener(textChangeFocusListener())
            edtCnpj.addTextChangedListener(MaskUtil.insertMask(edtCnpj, MaskUtil.MaskType.CNPJ))
        }
    }

    private fun setupView() {
        company?.let {
            binding?.apply {
                edtCnpj.setText(it.document)
                edtCompanyName.setText(it.companyName)
                edtFantasyName.setText(it.fantasyName)
            }
        }
    }

    private fun getPartnerCompany(binding: FragmentRegisterPartnerCompanyBinding) =
        with(binding) {
            val document = edtCnpj.text.toString()
            val fantasyName = edtFantasyName.text.toString()
            val companyName = edtCompanyName.text.toString()
            company?.apply {
                this.document = document
                this.companyName = companyName
                this.fantasyName = fantasyName
            } ?: PartnerCompany(
                document = document,
                fantasyName = fantasyName,
                companyName = companyName
            )
        }

    private fun textChangeFocusListener() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding?.apply {
                btnRegister.isEnabled =
                    edtCnpj.text.toString().length == maskCNPJ.length &&
                            edtCompanyName.text.toString().isNotEmpty() &&
                            edtFantasyName.text.toString().isNotEmpty()

            }
        }

        override fun afterTextChanged(s: Editable?) = Unit
    }
}