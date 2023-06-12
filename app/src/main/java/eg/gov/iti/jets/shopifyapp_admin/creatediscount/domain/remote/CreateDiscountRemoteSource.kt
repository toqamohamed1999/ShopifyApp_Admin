package eg.gov.iti.jets.shopifyapp_admin.creatediscount.domain.remote

import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.*
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Headers
import retrofit2.http.Path

interface CreateDiscountRemoteSource {

    suspend fun createPriceRule(body : PriceRuleBody): PriceRuleResponse?
    suspend fun createDiscountCode(ruleID : Long, body : DiscountCodeBody): DiscountCode
    suspend fun getPriceRules(): List<PriceRuleX>
    suspend fun getDiscounts(ruleID : Long) : List<DiscountCode>
    suspend fun updateDiscount(ruleID : Long, discountId : Long, body: DiscountCodeResponse)
        : DiscountCodeResponse
    suspend fun deleteDiscount(ruleID : Long, discountId : Long) : String
    suspend fun updatePriceRule(ruleID : Long, body: PriceRuleResponse): PriceRuleResponse
    suspend fun deletePriceRule(ruleID : Long) : String

}