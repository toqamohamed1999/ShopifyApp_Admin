package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.allrules.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.PriceRule
import eg.gov.iti.jets.shopifyapp_admin.discounts.domain.repo.DiscountRepo
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AllRulesViewModel(private val repo: DiscountRepo) : ViewModel() {

    private val TAG = "AllRulesViewModel"

    private var _ruleState: MutableStateFlow<APIState<List<PriceRule>>> = MutableStateFlow(
        APIState.Loading()
    )
    var ruleState: StateFlow<APIState<List<PriceRule>>> = _ruleState

    fun getPriceRules() {
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
}

class AllRulesViewModelFactory(private val repo: DiscountRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(AllRulesViewModel::class.java)) {
            AllRulesViewModel(repo) as T
        } else {
            throw java.lang.IllegalArgumentException("AllRulesViewModel class not found")
        }
    }
}