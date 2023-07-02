package br.com.mdr.controledenf.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.mdr.controledenf.databinding.FragmentLoginEmailBinding
import br.com.mdr.controledenf.presentation.activity.MainActivity
import br.com.mdr.controledenf.presentation.viewmodel.AuthViewModel
import br.com.mdr.controledenf.utils.extension.errorSnack
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val EMAIL = "email"

class LoginEmailFragment : Fragment() {
    private val viewModel: AuthViewModel by sharedViewModel()

    private var email: String? = null
    private var binding: FragmentLoginEmailBinding? = null

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
        binding = FragmentLoginEmailBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupListeners() {
        binding?.apply {
            btnContinue.setOnClickListener {
                viewModel.loginWithEmail(
                    edtEmail.text.toString(),
                    edtPassword.text.toString(),
                    checkAutoLogin.isChecked
                )
            }
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            company.observe(viewLifecycleOwner) {
                it?.let {
                    MainActivity.startMain(requireContext())
                    activity?.finish()
                }
            }

            apiError.observe(viewLifecycleOwner) {
                view?.errorSnack(it.error)
            }
        }
    }
}