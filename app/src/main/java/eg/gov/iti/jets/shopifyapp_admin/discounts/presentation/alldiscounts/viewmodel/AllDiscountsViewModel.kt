package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.viewmodel

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

class AllDiscountsViewModel(private val repo: DiscountRepo) : ViewModel() {

    private val TAG = "AllDiscountsViewModel"

    private var _getDiscountState: MutableStateFlow<APIState<List<DiscountCode>>> = MutableStateFlow(
        APIState.Loading()
    )
    var getDiscountState: StateFlow<APIState<List<DiscountCode>>> = _getDiscountState

    private var _deleteDiscountState: MutableStateFlow<APIState<String>> = MutableStateFlow(
        APIState.Loading()
    )
    var deleteDiscountState: StateFlow<APIState<String>> = _deleteDiscountState

    fun getDiscounts(ruleID : Long) {
        viewModelScope.launch {
            try {
                repo.getDiscounts(ruleID).collect {
                    _getDiscountState.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _getDiscountState.value = APIState.Error()
                Log.i(TAG, "getDiscounts: "+e.message)
            }
        }
    }

    fun deleteDiscount(ruleId : Long,discountId: Long) {
        viewModelScope.launch {
            try {
                repo.deleteDiscount(ruleId,discountId).collect {
                    _deleteDiscountState.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _deleteDiscountState.value = APIState.Error()
                Log.i(TAG, "deleteDiscount: "+e.message)
            }
        }
    }

}

class AllDiscountsViewModelFactory(private val repo: DiscountRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(AllDiscountsViewModel::class.java)) {
            AllDiscountsViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("AllDiscountsViewModel class not found")
        }
    }
}
