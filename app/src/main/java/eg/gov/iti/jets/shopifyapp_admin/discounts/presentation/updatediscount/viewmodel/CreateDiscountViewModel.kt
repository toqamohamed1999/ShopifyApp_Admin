package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.updatediscount.viewmodel

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

class UpdateDiscountViewModel(private val repo: DiscountRepo) : ViewModel() {

    private val TAG = "UpdateDiscountViewModel"

    private var _updateDiscountState: MutableStateFlow<APIState<DiscountCodeResponse>> = MutableStateFlow(
        APIState.Loading()
    )
    var updateDiscountState: StateFlow<APIState<DiscountCodeResponse>> = _updateDiscountState


    fun updateDiscount(ruleId : Long,discountId: Long, body: DiscountCodeResponse) {
        viewModelScope.launch {
            try {
                repo.updateDiscount(ruleId,discountId,body).collect {
                    _updateDiscountState.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _updateDiscountState.value = APIState.Error()
                Log.i(TAG, "updateDiscount: "+e.message)
            }
        }
    }


}

class UpdateDiscountViewModelFactory(private val repo: DiscountRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(UpdateDiscountViewModel::class.java)) {
            UpdateDiscountViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("UpdateDiscountViewModel class not found")
        }
    }
}
