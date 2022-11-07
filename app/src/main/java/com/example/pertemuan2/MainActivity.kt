package com.example.pertemuan2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.example.pertemuan2.databinding.ActivityMainBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Flowable.combineLatest
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        processedLogin()
        onAction()

    }

    private fun onAction(){
        binding.apply {
            lbtn1.setOnClickListener {
                Intent(this@MainActivity, HomeActivity::class.java).also { intent ->  startActivity(intent)
                }
            }
        }
    }

    @SuppressLint("checkResult")
    private fun processedLogin(){
        binding.apply {
            val emailstream = RxTextView.textChanges(lemail).skipInitialValue().map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }

            emailstream.subscribe { showEmailExistAlert(it)

            }

            val passwordstream = RxTextView.textChanges(lpass)
                .skipInitialValue().map { password -> password.length < 8
                }
            passwordstream.subscribe { showPasswordExistAlert(it)

            }

            val invalidFieldStream = Observable.combineLatest(emailstream, passwordstream){
                emailInvalid,passwordInvalid -> !emailInvalid && !passwordInvalid
            }

            invalidFieldStream.subscribe {
                showButtonLogin(it)
            }
        }
    }

    private fun showButtonLogin(state: Boolean){
        binding.lbtn1.isEnabled = state
    }

    private fun showEmailExistAlert(state: Boolean){
        binding.lpass.error = if (state) resources.getString(R.string.pass_length_less_then_8) else null
    }

    private fun showPasswordExistAlert(state: Boolean) {
        binding.lemail.error = if (state) resources.getString(R.string.email_not_valid) else null
    }
}