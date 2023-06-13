package eg.gov.iti.jets.shopifyapp_admin.discounts.domain.remote

import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.*

interface DiscountRemoteSource {

    suspend fun createPriceRule(body : PriceRuleBody): PriceRuleResponse?
    suspend fun createDiscountCode(ruleID : Long, body : DiscountCodeBody): DiscountCode
    suspend fun getPriceRules(): List<PriceRule>
    suspend fun getDiscounts(ruleID : Long) : List<DiscountCode>
    suspend fun updateDiscount(ruleID : Long, discountId : Long, body: DiscountCodeResponse)
        : DiscountCodeResponse
    suspend fun deleteDiscount(ruleID : Long, discountId : Long) : String
    suspend fun updatePriceRule(ruleID : Long, body: PriceRuleResponse): PriceRuleResponse
    suspend fun deletePriceRule(ruleID : Long) : String

}