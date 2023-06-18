package eg.gov.iti.jets.shopifyapp_admin.products.presentation.updateproduct.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.products.domain.repo.ProductRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UpdateProductViewModel(private val repo: ProductRepo) : ViewModel() {

    private val TAG = "UpdateProductViewModel"

    private var _updateProductState: MutableStateFlow<APIState<ProductResponse>> = MutableStateFlow(
        APIState.Loading()
    )
    var updateProductState: StateFlow<APIState<ProductResponse>> = _updateProductState

    private var _updateProductQuantity: MutableStateFlow<APIState<InventoryLevelResponse>> = MutableStateFlow(
        APIState.Loading()
    )
    var updateProductQuantity: StateFlow<APIState<InventoryLevelResponse>> = _updateProductQuantity

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

    fun updateProductQuantity( body: UpdateQuantityBody) {
        viewModelScope.launch {
            try {
                repo.updateProductQuantity(body).collect {
                    _updateProductQuantity.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _updateProductQuantity.value = APIState.Error()
                Log.i(TAG, "updateVariant: "+e.message)
            }
        }
    }

}


class UpdateProductViewModelFactory(private val repo: ProductRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(UpdateProductViewModel::class.java)) {
            UpdateProductViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("UpdateProductViewModel class not found")
        }
    }
}
