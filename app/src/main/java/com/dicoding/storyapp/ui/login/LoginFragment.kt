package com.dicoding.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.databinding.FragmentLoginBinding
import com.dicoding.storyapp.ui.home.HomeActivity
import com.dicoding.storyapp.ui.register.RegisterFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

    private var _fragmentLoginBinding: FragmentLoginBinding? = null
    private val binding get() = _fragmentLoginBinding!!

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_submit -> handleSubmit()
            R.id.tv_register -> showRegisterPage()
        }
    }

    private fun handleSubmit() {
        val email = binding.edtEmail.editText?.text.toString().trim()
        val password = binding.edtPassword.editText?.text.toString().trim()

        if (validateAllFields(email, password)) {
            lifecycleScope.launch {
                loginViewModel.login(email, password).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                            binding.btnSubmit.isEnabled = false
                        }
                        is Result.Success -> {
                            binding.progressIndicator.visibility = View.GONE
                            binding.btnSubmit.isEnabled = true

                            loginViewModel.saveBearerToken(result.data.loginResult.token)

                            val homeActivity = Intent(requireActivity(), HomeActivity::class.java)
                            startActivity(homeActivity)
                            requireActivity().finish()
                        }
                        is Result.Error -> {
                            binding.progressIndicator.visibility = View.GONE
                            binding.btnSubmit.isEnabled = true

                            Snackbar.make(binding.root, result.error, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    // Bagian kode yang ini mas.
    // Kodenya saya rubah kembali ke sebelumnya
    private fun showRegisterPage() {
        val fragmentManager = parentFragmentManager
        val registerFragment = RegisterFragment()

        fragmentManager.commit {
            replace(R.id.container, registerFragment, RegisterFragment::class.java.simpleName)
            addToBackStack(null)
        }
    }

    private fun validateAllFields(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.edtEmail.error = resources.getString(R.string.error_required_email_message)
            return false
        }

        if (!isValidEmail(email)) {
            binding.edtEmail.error = resources.getString(R.string.error_format_email_message)
            return false
        }

        if (password.isEmpty()) {
            binding.edtPassword.error =
                resources.getString(R.string.error_required_password_message)
            return false
        }

        return true
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentLoginBinding = null
    }
}