package eg.gov.iti.jets.shopifyapp_admin.auth.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import eg.gov.iti.jets.shopifyapp_admin.BuildConfig
import eg.gov.iti.jets.shopifyapp_admin.MainActivity
import eg.gov.iti.jets.shopifyapp_admin.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        editor = applicationContext.getSharedPreferences("mypref", Context.MODE_PRIVATE).edit()

        binding.btnLogIn.setOnClickListener {
            if(validateData()){
                login()
                editor.putBoolean("isLogin",true).commit()
            }
        }
    }

    private fun validateData() : Boolean{
        if (binding.userNameEditText.text.toString() != BuildConfig.store_name
            || binding.passwordEditText.text.toString() != BuildConfig.admin_passowrd){
            binding.warningText.visibility = View.VISIBLE
            return false
        }
        binding.warningText.visibility = View.GONE
        return true
    }

    private fun login(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
        Toast.makeText(applicationContext,"Login Successfully", Toast.LENGTH_LONG).show()
    }

}