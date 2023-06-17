package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.createrule.viewmodel

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

class CreateRuleViewModel (private val repo: DiscountRepo) : ViewModel() {

    private val TAG = "CreateRuleViewModel"

    private var _createRuleState: MutableStateFlow<APIState<PriceRuleResponse>> = MutableStateFlow(
        APIState.Loading()
    )
    var createRuleState: StateFlow<APIState<PriceRuleResponse>> = _createRuleState

    fun createPriceRule(body : PriceRuleBody) {
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
}

class CreateRuleViewModelFactory(private val repo: DiscountRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(CreateRuleViewModel::class.java)) {
            CreateRuleViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("CreateRuleViewModel class not found")
        }
    }
}
