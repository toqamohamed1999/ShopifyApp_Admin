package eg.gov.iti.jets.shopifyapp_admin.products.presentation.allproducts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.products.domain.repo.ProductRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AllProductsViewModel(private val repo: ProductRepo) : ViewModel() {

    private val TAG = "AllProductsViewModel"

    private var _getProductState: MutableStateFlow<APIState<List<Product>>> = MutableStateFlow(
        APIState.Loading()
    )
    var getProductState: StateFlow<APIState<List<Product>>> = _getProductState

    init {
        getProducts()
    }


    private fun getProducts() {
        viewModelScope.launch {
            try {
                repo.getProducts().collect {
                    _getProductState.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _getProductState.value = APIState.Error()
                Log.i(TAG, "getProducts: "+e.message)
            }
        }
    }
}


class AllProductViewModelFactory(private val repo: ProductRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(AllProductsViewModel::class.java)) {
            AllProductsViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("AllProductsViewModel class not found")
        }
    }
}
