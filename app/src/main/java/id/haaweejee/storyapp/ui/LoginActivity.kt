package id.haaweejee.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import id.haaweejee.storyapp.databinding.ActivityLoginBinding
import id.haaweejee.storyapp.service.data.login.LoginRequest
import id.haaweejee.storyapp.service.preferences.SettingsPreference
import id.haaweejee.storyapp.utils.ViewModelFactory
import id.haaweejee.storyapp.viewmodel.PreferencesViewModel
import id.haaweejee.storyapp.viewmodel.UserViewModel


class LoginActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityLoginBinding

    //ViewModel
    private lateinit var viewModel: UserViewModel
    private lateinit var prefViewModel: PreferencesViewModel
    private var emailCondition = false
    private var passwordCondition = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val pref = SettingsPreference.getInstance(dataStore)
        prefViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferencesViewModel::class.java]

        binding.tvRegister.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            login()
        }

        viewModel.loginResponse.observe(this){
            if (it != null){
                if (it.error == true){
                    showLoading(false)
                    Snackbar.make(binding.root, "Login gagal cobalah beberapa saat lagi", Toast.LENGTH_SHORT).show()
                }else{
                    showLoading(false)
                    intent = Intent(this, MainActivity::class.java)
                    prefViewModel.saveLoginState(true)
                    prefViewModel.saveBearerToken(it.loginResult?.token!!)
                    startActivity(intent)
                    finish()
                }
            }
        }


        playAnimation()
    }

    private fun login(){
        val email = binding.edtEmail.text?.toString()?.trim()
        val password = binding.edtPassword.text?.toString()?.trim()

        if (email != null) {
            when{
                email.isEmpty() -> {
                    binding.tlEmail.error = "Silahkan masukkan Email"
                    emailCondition = false
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->{
                    binding.tlEmail.error = "Email tidak valid"
                    emailCondition = false
                }
                else -> {
                    binding.tlEmail.error = null
                    emailCondition = true
                }
            }
        }

        if (password != null) {
            when {
                password.isEmpty() -> {
                    binding.tlPassword.error = "Silahkan masukkan password"
                    passwordCondition = false
                }
                password.length < 6 -> {
                    binding.tlPassword.error = "Password harus lebih dari 6 huruf"
                    passwordCondition = false
                }
                else -> {
                    binding.tlPassword.error = null
                    passwordCondition = true
                }
            }
        }

        if (emailCondition && passwordCondition){
            viewModel.userLogin(LoginRequest(email, password))
            showLoading(true)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        //email
        val textEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val textInputEmail = ObjectAnimator.ofFloat(binding.tlEmail, View.ALPHA, 1f).setDuration(500)

        //password
        val textPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val textInputPassword = ObjectAnimator.ofFloat(binding.tlPassword, View.ALPHA, 1f).setDuration(500)

        //button Login
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val registerLayout = ObjectAnimator.ofFloat(binding.layoutRegister, View.ALPHA, 1f).setDuration(500)

        val togetherEmail = AnimatorSet().apply {
            playTogether(textEmail, textInputEmail)
        }

        val togetherPassword = AnimatorSet().apply {
            playTogether(textPassword, textInputPassword)
        }

        AnimatorSet().apply {
            playSequentially(
                togetherEmail,
                togetherPassword,
                btnLogin,
                registerLayout
            )
            start()
        }
    }
}