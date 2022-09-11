package com.dicoding.storyapp.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.databinding.FragmentRegisterBinding
import com.dicoding.storyapp.ui.login.LoginFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment(), View.OnClickListener {

    private var _fragmentRegisterBinding: FragmentRegisterBinding? = null
    private val binding get() = _fragmentRegisterBinding!!

    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentRegisterBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_submit -> handleSubmit()
            R.id.tv_login -> showLoginPage()
        }
    }

    private fun handleSubmit() {
        val name = binding.edtName.editText?.text.toString().trim()
        val email = binding.edtEmail.editText?.text.toString().trim()
        val password = binding.edtPassword.editText?.text.toString().trim()

        if (validateAllFields(name, email, password)) {
            lifecycleScope.launch {
                registerViewModel.register(name, email, password).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                            binding.btnSubmit.isEnabled = false
                        }
                        is Result.Success -> {
                            binding.progressIndicator.visibility = View.GONE
                            binding.btnSubmit.isEnabled = true

                            Toast.makeText(
                                requireContext(),
                                resources.getString(R.string.successful_registration_message),
                                Toast.LENGTH_SHORT
                            ).show()

                            showLoginPage()
                        }
                        is Result.Error -> {
                            binding.progressIndicator.visibility = View.GONE
                            binding.btnSubmit.isEnabled = true

                            Snackbar.make(binding.root, result.error, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    // Bagian kode yang ini mas.
    // Kodenya saya rubah kembali ke sebelumnya
    private fun showLoginPage() {
        val fragmentManager = parentFragmentManager
        val loginFragment = LoginFragment()

        fragmentManager.commit {
            replace(R.id.container, loginFragment, LoginFragment::class.java.simpleName)
            addToBackStack(null)
        }
    }

    private fun validateAllFields(name: String, email: String, password: String): Boolean {
        if (name.isEmpty()) {
            binding.edtName.error = resources.getString(R.string.error_required_name_message)
            return false
        }

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
        _fragmentRegisterBinding = null
    }
}
