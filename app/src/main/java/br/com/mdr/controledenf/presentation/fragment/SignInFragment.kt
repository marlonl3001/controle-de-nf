package br.com.mdr.controledenf.presentation.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import br.com.mdr.controledenf.R
import br.com.mdr.controledenf.databinding.FragmentSignInBinding
import br.com.mdr.controledenf.presentation.activity.MainActivity
import br.com.mdr.controledenf.presentation.viewmodel.AuthViewModel
import br.com.mdr.controledenf.utils.extension.errorSnack
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val FB_EMAIL_PERMISSION = "email"
private const val FB_PUBLIC_PROFILE_PERMISSION = "public_profile"

class SignInFragment : Fragment() {
    private val viewModel: AuthViewModel by sharedViewModel()
    private val googleSignInClient: GoogleSignInClient by inject()
    private lateinit var callbackManager: CallbackManager

    private var binding: FragmentSignInBinding? = null
    private lateinit var mAuth: FirebaseAuth

    private val googleSignInResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    if (task.isSuccessful) {
                        try {
                            val account = task.getResult(ApiException::class.java)
                            if (account != null) {
                                firebaseAuthWithGoogle(account.idToken)
                            }
                        } catch (e: ApiException) {
                            e.localizedMessage?.let { view?.errorSnack(it) }
                        }
                    } else {
                        task.exception?.message?.let { view?.errorSnack(it) }
                    }
                }
                Activity.RESULT_CANCELED -> Unit
                else -> showDefaultError()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
        setupGoogleLogin()
        setupFacebookLogin()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupObservers() {
        with(viewModel) {
            goToStepTwo.observe(viewLifecycleOwner) {
                if (it) {
                    view?.findNavController()?.navigate(
                        SignInFragmentDirections.actionSignInFragmentToSignInFormFragment(
                            FirebaseAuth.getInstance().currentUser?.email
                        ))
                }
            }
            company.observe(viewLifecycleOwner) {
                MainActivity.startMain(requireContext())
                activity?.finish()
            }
        }
    }

    private fun setupListeners() {
        binding?.apply {
            btnRegister.setOnClickListener {
                findNavController().navigate(
                    SignInFragmentDirections.actionSignInFragmentToSignInFormFragment(null)
                )
            }
            btnHasRegister.setOnClickListener {
                findNavController().navigate(
                    SignInFragmentDirections.actionSignInFragmentToLoginEmailFragment()
                )
            }
            btnGoogle.setOnClickListener {
                authenticateWithGoogle()
            }
            btnFacebook.setOnClickListener {
                authenticateWithFacebook()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                mAuth.currentUser?.let { user ->
                    user.email?.let { email -> viewModel.verifyUserWithEmail(email) }
                } ?: run {
                    showDefaultError()
                }
            }
    }

    private fun authenticateWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInResultLauncher.launch(signInIntent)
    }

    private fun authenticateWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            callbackManager,
            listOf(FB_PUBLIC_PROFILE_PERMISSION, FB_EMAIL_PERMISSION)
        )
    }

    private fun setupGoogleLogin() {
        mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()

        googleSignInClient.signOut()
    }

    private fun setupFacebookLogin() {
        LoginManager.getInstance().logOut()
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() = Unit

                override fun onError(error: FacebookException) {
                    showDefaultError(error.message)
                }
            }
        )
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        activity?.apply {
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) {
                    mAuth.currentUser?.let { user ->
                        user.email?.let { email -> viewModel.verifyUserWithEmail(email) }
                    } ?: run {
                        showDefaultError()
                    }
                }
        }
    }

    private fun showDefaultError(text: String? = null, resourceMessage: Int? = null) {
        val message = text ?: if (resourceMessage != null) {
            getString(resourceMessage)
        } else {
            getString(R.string.default_error)
        }
        view?.errorSnack(message)
    }
}