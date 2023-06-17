package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.creatediscount.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.discounts.domain.repo.DiscountRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateDiscountViewModel(private val repo: DiscountRepo) : ViewModel() {

    private val TAG = "CreateDiscountViewModel"

    private var _createDiscountState: MutableStateFlow<APIState<DiscountCode>> = MutableStateFlow(
        APIState.Loading()
    )
    var createDiscountState: StateFlow<APIState<DiscountCode>> = _createDiscountState

    fun createDiscount(ruleID : Long, body: DiscountCodeBody) {
        viewModelScope.launch {
            try {
                repo.createDiscountCode(ruleID,body).collect {
                    _createDiscountState.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _createDiscountState.value = APIState.Error()
                Log.i(TAG, "createDiscount: "+e.message)
            }
        }
    }

}

class CreateDiscountViewModelFactory(private val repo: DiscountRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(CreateDiscountViewModel::class.java)) {
            CreateDiscountViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("CreateDiscountViewModel class not found")
        }
    }
}
