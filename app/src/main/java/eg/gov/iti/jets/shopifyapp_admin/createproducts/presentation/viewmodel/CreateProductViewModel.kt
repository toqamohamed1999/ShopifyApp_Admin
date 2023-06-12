package eg.gov.iti.jets.shopifyapp_admin.createproducts.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.remote.APIState
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductResponse
import eg.gov.iti.jets.shopifyapp_admin.createproducts.domain.repo.CreateProductRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateProductViewModel(private val repo: CreateProductRepo) : ViewModel() {

    private val TAG = "CreateProductViewModel"

    private var _productState: MutableStateFlow<APIState<ProductResponse>> = MutableStateFlow(
        APIState.Loading()
    )
    var productState: StateFlow<APIState<ProductResponse>> = _productState

    private var _getProductState: MutableStateFlow<APIState<List<Product>>> = MutableStateFlow(
        APIState.Loading()
    )
    var getProductState: StateFlow<APIState<List<Product>>> = _getProductState

    private var _updateProductState: MutableStateFlow<APIState<ProductResponse>> = MutableStateFlow(
        APIState.Loading()
    )
    var updateProductState: StateFlow<APIState<ProductResponse>> = _updateProductState

    private var _deleteProductState: MutableStateFlow<APIState<String>> = MutableStateFlow(
        APIState.Loading()
    )
    var deleteProductState: StateFlow<APIState<String>> = _deleteProductState


    init {
    }



    fun createProduct(body : ProductBody) {
        viewModelScope.launch {
            try {
                repo.createProduct(body).collect {
                    _productState.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _productState.value = APIState.Error()
                Log.i(TAG, "createProduct: "+e.message)
            }
        }
    }

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

    fun updateProduct(productId: Long, body: ProductResponse) {
        viewModelScope.launch {
            try {
                repo.updateProduct(productId,body).collect {
                    _updateProductState.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _updateProductState.value = APIState.Error()
                Log.i(TAG, "updateProduct: "+e.message)
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

}

class CreateProductViewModelFactory(private val repo: CreateProductRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(CreateProductViewModel::class.java)) {
            CreateProductViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("CreateProductViewModel class not found")
        }
    }
}
