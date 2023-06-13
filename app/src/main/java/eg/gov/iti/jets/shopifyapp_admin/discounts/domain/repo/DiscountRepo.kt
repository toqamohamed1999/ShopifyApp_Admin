package eg.gov.iti.jets.shopifyapp_admin.discounts.domain.repo

import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.*
import kotlinx.coroutines.flow.Flow

interface DiscountRepo {

    suspend fun createPriceRule(body : PriceRuleBody): Flow<PriceRuleResponse?>
    suspend fun createDiscountCode( ruleID : Long, body : DiscountCodeBody):  Flow<DiscountCode>
    suspend fun getPriceRules(): Flow<List<PriceRule>>
    suspend fun getDiscounts(ruleID: Long): Flow<List<DiscountCode>>
    suspend fun updateDiscount(ruleID : Long, discountId : Long, body: DiscountCodeResponse)
            : Flow<DiscountCodeResponse>
    suspend fun deleteDiscount(ruleID : Long, discountId : Long) : Flow<String>
    suspend fun updatePriceRule(ruleID : Long, body: PriceRuleResponse): Flow<PriceRuleResponse>
    suspend fun deletePriceRule(ruleID : Long) : Flow<String>

}