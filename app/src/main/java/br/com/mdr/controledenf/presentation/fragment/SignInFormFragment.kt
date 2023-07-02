package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.mdr.controledenf.databinding.FragmentSignInFormBinding
import br.com.mdr.controledenf.presentation.activity.MainActivity
import br.com.mdr.controledenf.presentation.viewmodel.AuthViewModel
import br.com.mdr.controledenf.utils.MaskUtil
import br.com.mdr.controledenf.utils.extension.errorSnack
import br.com.mdr.controledenf.utils.extension.toCurrencyString
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val EMAIL = "email"
private const val MIN_CHAR = 6
private const val DEFAULT_BILLING_THRESHOLD = 81000f

class SignInFormFragment : Fragment() {
    private val viewModel: AuthViewModel by sharedViewModel()

    private var email: String? = null
    private var binding: FragmentSignInFormBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString(EMAIL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInFormBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEmailData()
        setupListeners()
        setupObservers()
        setupSlider()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setEmailData() {
        binding?.apply {
            edtEmail.setText(email)
        }
    }

    private fun setupObservers() {
        viewModel.apiError.observe(viewLifecycleOwner) {
            view?.errorSnack(it.error)
        }
        viewModel.company.observe(viewLifecycleOwner) {
            MainActivity.startMain(requireContext())
            activity?.finish()
        }
    }

    private fun setupListeners() {
        binding?.apply {
            btnContinue.setOnClickListener {
                viewModel.saveCompany(
                    email = edtEmail.text.toString(),
                    name = edtName.text.toString(),
                    document = edtCnpj.text.toString(),
                    companyName = edtCompanyName.text.toString(),
                    phone = edtPhone.text.toString(),
                    password = edtPassword.text.toString(),
                    keepLogged = checkAutoLogin.isChecked,
                    billingThreshold = sliderLimit.value
                )
            }
            edtPassword.addTextChangedListener(textChangeFocusListener(edtPassword))
            edtPasswordConfirm.addTextChangedListener(textChangeFocusListener(edtPasswordConfirm))
            edtCnpj.addTextChangedListener(MaskUtil.insertMask(edtCnpj, MaskUtil.MaskType.CNPJ))
            edtPhone.addTextChangedListener(MaskUtil.insertMask(edtPhone, MaskUtil.MaskType.PHONE))
        }
    }

    private fun setupSlider() {
        binding?.sliderLimit?.apply {
            this.valueTo = DEFAULT_BILLING_THRESHOLD
            this.value = DEFAULT_BILLING_THRESHOLD
            this.setLabelFormatter {
                value.toDouble().toCurrencyString()
            }
        }
    }

    private fun textChangeFocusListener(editText: TextInputEditText) = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            (editText as? TextInputEditText)?.apply {
                var errorMessage: String?

                val textLength = s?.length ?: start
                errorMessage = if ( textLength < MIN_CHAR) {
                    "Mínimo de 6 caracteres"
                } else {
                    null
                }
                binding?.let {
                    when {
                        this == it.edtPassword -> {
                            it.edtPassword.error = errorMessage
                            it.inputPassword.isErrorEnabled = errorMessage != null
                        }
                        this == it.edtPasswordConfirm -> {
                            errorMessage =
                                if (s.toString() != it.edtPassword.text.toString()) {
                                    "As senhas não são iguais"
                                } else {
                                    null
                                }
                            it.edtPasswordConfirm.error = errorMessage
                            it.inputPasswordConfirm.isErrorEnabled = errorMessage != null
                        }
                    }
                    it.btnContinue.isEnabled = errorMessage == null
                }
            }
        }

        override fun afterTextChanged(s: Editable?) = Unit

    }
}