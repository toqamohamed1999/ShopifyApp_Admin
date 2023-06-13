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

    private var _ruleState: MutableStateFlow<APIState<List<PriceRule>>> = MutableStateFlow(
        APIState.Loading()
    )
    var ruleState: StateFlow<APIState<List<PriceRule>>> = _ruleState

    private var _createRuleState: MutableStateFlow<APIState<PriceRuleResponse>> = MutableStateFlow(
        APIState.Loading()
    )
    var createRuleState: StateFlow<APIState<PriceRuleResponse>> = _createRuleState

    private var _createDiscountState: MutableStateFlow<APIState<DiscountCode>> = MutableStateFlow(
        APIState.Loading()
    )
    var createDiscountState: StateFlow<APIState<DiscountCode>> = _createDiscountState

    private var _getDiscountState: MutableStateFlow<APIState<List<DiscountCode>>> = MutableStateFlow(
        APIState.Loading()
    )
    var getDiscountState: StateFlow<APIState<List<DiscountCode>>> = _getDiscountState

    private var _updateDiscountState: MutableStateFlow<APIState<DiscountCodeResponse>> = MutableStateFlow(
        APIState.Loading()
    )
    var updateDiscountState: StateFlow<APIState<DiscountCodeResponse>> = _updateDiscountState

    private var _deleteDiscountState: MutableStateFlow<APIState<String>> = MutableStateFlow(
        APIState.Loading()
    )
    var deleteDiscountState: StateFlow<APIState<String>> = _deleteDiscountState
////////////////////////////
    private var _updateRuleState: MutableStateFlow<APIState<PriceRuleResponse>> = MutableStateFlow(
        APIState.Loading()
    )
    var updateRuleState: StateFlow<APIState<PriceRuleResponse>> = _updateRuleState

    private var _deleteRuleState: MutableStateFlow<APIState<String>> = MutableStateFlow(
        APIState.Loading()
    )
    var deleteRuleState: StateFlow<APIState<String>> = _deleteDiscountState

    init {
       // getPriceRules()
    }

    private fun getPriceRules() {
        viewModelScope.launch {
            try {
                repo.getPriceRules().collect {
                    _ruleState.value = APIState.Success(it)
                }
            } catch (e: java.lang.Exception) {
                _ruleState.value = APIState.Error()
                Log.i(TAG, "getPriceRules: "+e.message)
            }
        }
    }

    fun createRule(body : PriceRuleBody) {
        viewModelScope.launch {
            try {
                repo.createPriceRule(body).collect {
                    _createRuleState.value = APIState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _createRuleState.value = APIState.Error()
                Log.i(TAG, "createRule: "+e.message)
            }
        }
    }

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

class CreateDiscountViewModelFactory(private val repo: DiscountRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(CreateDiscountViewModel::class.java)) {
            CreateDiscountViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("CreateDiscountViewModel class not found")
        }
    }
}
