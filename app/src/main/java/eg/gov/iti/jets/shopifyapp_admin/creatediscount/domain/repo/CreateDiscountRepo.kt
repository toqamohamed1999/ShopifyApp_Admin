package eg.gov.iti.jets.shopifyapp_admin.creatediscount.domain.repo

import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.*
import kotlinx.coroutines.flow.Flow

interface CreateDiscountRepo {

    suspend fun createPriceRule(body : PriceRuleBody): Flow<PriceRuleResponse?>
    suspend fun createDiscountCode( ruleID : Long, body : DiscountCodeBody):  Flow<DiscountCode>
    suspend fun getPriceRules(): Flow<List<PriceRuleX>>
    suspend fun getDiscounts(ruleID: Long): Flow<List<DiscountCode>>
    suspend fun updateDiscount(ruleID : Long, discountId : Long, body: DiscountCodeResponse)
            : Flow<DiscountCodeResponse>
    suspend fun deleteDiscount(ruleID : Long, discountId : Long) : Flow<String>
    suspend fun updatePriceRule(ruleID : Long, body: PriceRuleResponse): Flow<PriceRuleResponse>
    suspend fun deletePriceRule(ruleID : Long) : Flow<String>

}