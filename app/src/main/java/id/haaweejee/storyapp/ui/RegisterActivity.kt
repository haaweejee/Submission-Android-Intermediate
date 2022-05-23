package id.haaweejee.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import id.haaweejee.storyapp.R
import id.haaweejee.storyapp.databinding.ActivityRegisterBinding
import id.haaweejee.storyapp.service.data.register.RegisterRequest
import id.haaweejee.storyapp.utils.InteractionUtils.hideKeyboard
import id.haaweejee.storyapp.viewmodel.UserViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: UserViewModel
    private var nameCondition = false
    private var emailCondition = false
    private var passwordCondition = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.tvLogin.setOnClickListener {
            moveToLoginActivity()
        }

        binding.btnRegister.setOnClickListener {
            hideKeyboard()
            signUp()
        }

        viewModel.registerResponse.observe(this@RegisterActivity) {
            if (it.error == false) {
                showLoading(false)
                moveToLoginActivity()
                Log.d("Results: ", it.message)
            }else{
                showLoading(false)
                Log.d("Results: ", it.message)
                Snackbar.make(binding.root, getString(R.string.register_failed), Snackbar.LENGTH_SHORT).show()
            }

        }

        playAnimation()
    }

    private fun moveToLoginActivity() {
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signUp() {
        val name = binding.edtName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        binding.btnRegister.isClickable = false

        when{
            name.isEmpty() -> {
                binding.tlName.error = getString(R.string.please_enter_name)
                nameCondition = false
            }
            else -> {
                binding.tlName.error = null
                nameCondition = true
            }
        }

        when{
            email.isEmpty() -> {
                binding.tlEmail.error = getString(R.string.please_enter_your_email)
                emailCondition = false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->{
                binding.tlEmail.error = getString(R.string.email_not_valid)
                emailCondition = false
            }
            else -> {
                binding.tlEmail.error = null
                emailCondition = true
            }
        }

        when {
            password.isEmpty() -> {
                binding.tlPassword.error = getString(R.string.please_enter_your_password)
                passwordCondition = false
            }
            password.length < 6 -> {
                binding.tlPassword.error = getString(R.string.password_less_6_word)
                passwordCondition = false
            }
            else -> {
                binding.tlPassword.error = null
                passwordCondition = true
            }
        }

        if (nameCondition && emailCondition && passwordCondition) {
            viewModel.userRegister(
                RegisterRequest(
                    name, email, password
                )
            )
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        //email
        val textName = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val textInputName = ObjectAnimator.ofFloat(binding.tlName, View.ALPHA, 1f).setDuration(500)

        //email
        val textEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val textInputEmail =
            ObjectAnimator.ofFloat(binding.tlEmail, View.ALPHA, 1f).setDuration(500)

        //password
        val textPassword =
            ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val textInputPassword =
            ObjectAnimator.ofFloat(binding.tlPassword, View.ALPHA, 1f).setDuration(500)

        //button Login
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val registerLayout =
            ObjectAnimator.ofFloat(binding.layoutLogin, View.ALPHA, 1f).setDuration(500)


        val togetherName = AnimatorSet().apply {
            playTogether(textName, textInputName)
        }

        val togetherEmail = AnimatorSet().apply {
            playTogether(textEmail, textInputEmail)
        }

        val togetherPassword = AnimatorSet().apply {
            playTogether(textPassword, textInputPassword)
        }


        AnimatorSet().apply {
            playSequentially(
                togetherName,
                togetherEmail,
                togetherPassword,
                btnRegister,
                registerLayout
            )
            start()
        }
    }

}