package eg.gov.iti.jets.shopifyapp_admin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContentProviderCompat.requireContext
import eg.gov.iti.jets.shopifyapp_admin.auth.ui.LoginActivity
import eg.gov.iti.jets.shopifyapp_admin.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        Handler(Looper.myLooper()!!).postDelayed({
            var intent : Intent = if (isLogin()) {
                Intent(this@SplashActivity, MainActivity::class.java)
            }else{
                Intent(this@SplashActivity, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun isLogin() : Boolean {
        sharedPreferences = applicationContext.getSharedPreferences("mypref", Context.MODE_PRIVATE)
        return  sharedPreferences.getBoolean("isLogin", false)
    }

}