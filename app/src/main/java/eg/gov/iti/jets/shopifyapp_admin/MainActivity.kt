package eg.gov.iti.jets.shopifyapp_admin

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
    private var navDestinationId: Int = 0
    private lateinit var hostFragment: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        hostFragment = findViewById(R.id.nav_host_fragment)

        setupNavigation()
        hideKeyBoard()
        observeNetwork()
        checkAuth()

        if(isOnline(applicationContext)){
            showView()
        }else{
            hideView()
        }
    }

    private fun setupNavigation() {
        bottomNavigation = binding.bottomNavigationView

        navController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
        setUpNavBottom(navController)
    }

    private fun hideKeyBoard() {
        KeyboardVisibilityEvent.setEventListener(
            this
        ) { isOpen ->
            bottomNavigation.isVisible = !isOpen
            checkNav()
        }
    }

    private fun setUpNavBottom(navController: NavController) {
        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            this.navDestinationId = navDestination.id
            checkNav()
        }
    }

    private fun checkNav() {
        if (navDestinationId == R.id.allProductsFragment || navDestinationId == R.id.allRulesFragment
        ) {
            bottomNavigation.visibility = View.VISIBLE
        } else {
            hideKeyBoard()
            bottomNavigation.visibility = View.GONE
        }
    }

    private fun checkAuth() {
        sharedPreferences = applicationContext.getSharedPreferences("mypref", Context.MODE_PRIVATE)

        if (!sharedPreferences.getBoolean("isLogin", false)) {
            navController.navigate(R.id.loginFragment)
        }
    }

    private fun observeNetwork() {
        NetworkConnectivityObserver.initNetworkConnectivity(applicationContext)

        NetworkConnectivityObserver.observeNetworkConnection().onEach {
            if (it == MyNetworkStatus.Available) {
                Log.e(TAG, "network is $it")
                showView()
            } else {
                Log.e(TAG, "network is $it")
                hideView()
            }
        }.launchIn(lifecycleScope)
    }


    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i(TAG, "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i(TAG, "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i(TAG, "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private fun hideView() {
        binding.noInternetLayout.visibility = View.VISIBLE
        bottomNavigation.visibility = View.GONE
        hostFragment.visibility = View.GONE
    }

    private fun showView(){
        binding.noInternetLayout.visibility = View.GONE
        bottomNavigation.visibility = View.VISIBLE
        hostFragment.visibility = View.VISIBLE
        navController.navigate(navDestinationId)
    }


}