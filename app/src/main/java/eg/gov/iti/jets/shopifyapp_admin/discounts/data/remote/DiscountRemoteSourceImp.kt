package eg.gov.iti.jets.shopifyapp_admin.discounts.data.remote

import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.discounts.domain.remote.DiscountRemoteSource
import eg.gov.iti.jets.shopifyapp_user.base.remote.AppRetrofit

class DiscountRemoteSourceImp : DiscountRemoteSource{
    private val TAG = "CreateDiscountRemoteSou"

    private object APIClient{
        val apiInterface : DiscountAPIInterface by lazy {
            AppRetrofit.retrofit.create(DiscountAPIInterface::class.java)
        }
    }

    override suspend fun createPriceRule(body : PriceRuleBody): PriceRuleResponse? {
        return APIClient.apiInterface.createPriceRule(body)
    }

    override suspend fun createDiscountCode(ruleID : Long, body : DiscountCodeBody,): DiscountCode {
        return APIClient.apiInterface.createDiscountCode(ruleID,body).discountCode
    }

    override suspend fun getPriceRules(): List<PriceRuleX>{
        return APIClient.apiInterface.getPriceRules().priceRules
    }

    override suspend fun getDiscounts(ruleID: Long): List<DiscountCode> {
        return APIClient.apiInterface.getDiscounts(ruleID).discount_codes
    }

    override suspend fun updateDiscount(
        ruleID: Long,
        discountId: Long,
        body: DiscountCodeResponse
    ): DiscountCodeResponse {
        return APIClient.apiInterface.updateDiscount(ruleID,discountId,body)
    }

    override suspend fun deleteDiscount(ruleID: Long, discountId: Long) : String{
        return try {
            APIClient.apiInterface.deleteDiscount(ruleID, discountId)
            "success"
        }catch (ex : Exception){
            "error"
        }
    }

    override suspend fun updatePriceRule(ruleID: Long, body: PriceRuleResponse): PriceRuleResponse {
        return APIClient.apiInterface.updatePriceRule(ruleID,body)
    }

    override suspend fun deletePriceRule(ruleID: Long): String {
        return try {
            APIClient.apiInterface.deletePriceRule(ruleID)
            "success"
        }catch (ex : Exception){
            "error"
        }
    }

}