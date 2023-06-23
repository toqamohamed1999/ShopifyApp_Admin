package eg.gov.iti.jets.shopifyapp_admin.products.presentation.allproducts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.VariantRoot
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

    private var _deleteProductState: MutableStateFlow<APIState<String>> = MutableStateFlow(
        APIState.Loading()
    )
    var deleteProductState: StateFlow<APIState<String>> = _deleteProductState

    private var _variantState: MutableStateFlow<APIState<VariantRoot>> = MutableStateFlow(
        APIState.Loading()
    )
    var variantState: StateFlow<APIState<VariantRoot>> = _variantState

    fun getProducts() {
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

    fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            try {
                repo.deleteProduct(productId).collect {
                    _deleteProductState.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _deleteProductState.value = APIState.Error()
                Log.i(TAG, "deleteProduct: "+e.message)
            }
        }
    }

    fun getVariantById(variantId: Long) {
        viewModelScope.launch {
            try {
                repo.getVariantBYId(variantId).collect {
                    _variantState.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _variantState.value = APIState.Error()
                Log.i(TAG, "getVariantById: "+e.message)
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
