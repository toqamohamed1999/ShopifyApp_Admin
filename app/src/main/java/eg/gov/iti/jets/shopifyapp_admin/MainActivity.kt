package eg.gov.iti.jets.shopifyapp_admin

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import eg.gov.iti.jets.newsapp.util.MyNetworkStatus
import eg.gov.iti.jets.newsapp.util.NetworkConnectivityObserver
import eg.gov.iti.jets.shopifyapp_admin.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        bottomNavigation = binding.bottomNavigationView

        navController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
        setUpNavBottom(navController)

        hideKeyBoard()
        observeNetwork()
        checkAuth()
    }

    private fun hideKeyBoard(){
        KeyboardVisibilityEvent.setEventListener(
            this
        ) { isOpen ->
            bottomNavigation.isVisible = !isOpen
        }
    }

    private fun setUpNavBottom(navController: NavController) {
        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            if (navDestination.id == R.id.allProductsFragment || navDestination.id == R.id.allRulesFragment
            ) {
                bottomNavigation.visibility = View.VISIBLE
            } else {
                hideKeyBoard()
                bottomNavigation.visibility = View.GONE
            }
        }
    }

    private fun checkAuth(){
        sharedPreferences = applicationContext.getSharedPreferences("mypref", Context.MODE_PRIVATE)

        if(!sharedPreferences.getBoolean("isLogin",false)){
            navController.navigate(R.id.loginFragment)
        }
    }

    private fun observeNetwork() {
        NetworkConnectivityObserver.initNetworkConnectivity(applicationContext)

        NetworkConnectivityObserver.observeNetworkConnection().onEach {
            if (it == MyNetworkStatus.Available) {
                Log.e(TAG, "network is $it")
                //networkTextView.visibility = View.GONE
            } else {
                //networkTextView.visibility = View.VISIBLE
                Log.e(TAG, "network is $it")
            }
        }.launchIn(lifecycleScope)
    }


}