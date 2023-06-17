package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.updaterule.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.DiscountCode
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.DiscountCodeBody
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.PriceRuleBody
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.PriceRuleResponse
import eg.gov.iti.jets.shopifyapp_admin.discounts.domain.repo.DiscountRepo
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.creatediscount.viewmodel.CreateDiscountViewModel
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UpdateRuleViewModel (private val repo: DiscountRepo) : ViewModel() {

    private val TAG = "UpdateRuleViewModel"

    private var _updateRuleState: MutableStateFlow<APIState<PriceRuleResponse>> = MutableStateFlow(
        APIState.Loading()
    )
    var updateRuleState: StateFlow<APIState<PriceRuleResponse>> = _updateRuleState

    private var _deleteRuleState: MutableStateFlow<APIState<String>> = MutableStateFlow(
        APIState.Loading()
    )
    var deleteRuleState: StateFlow<APIState<String>> = _deleteRuleState

    fun updatePriceRule(ruleId : Long, body: PriceRuleResponse) {
        viewModelScope.launch {
            try {
                repo.updatePriceRule(ruleId,body).collect {
                    _updateRuleState.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _updateRuleState.value = APIState.Error()
                Log.i(TAG, "updatePriceRule: "+e.message)
            }
        }
    }

    fun deletePriceRule(ruleId : Long) {
        viewModelScope.launch {
            try {
                repo.deletePriceRule(ruleId).collect {
                    _deleteRuleState.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _deleteRuleState.value = APIState.Error()
                Log.i(TAG, "deletePriceRule: "+e.message)
            }
        }
    }
}

class UpdateRuleViewModelFactory(private val repo: DiscountRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(UpdateRuleViewModel::class.java)) {
            UpdateRuleViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("UpdateRuleViewModel class not found")
        }
    }
}
