package eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.remote

import android.util.Log
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.domain.remote.CreateDiscountRemoteSource
import eg.gov.iti.jets.shopifyapp_user.base.remote.AppRetrofit

class CreateDiscountRemoteSourceImp : CreateDiscountRemoteSource{
    private val TAG = "CreateDiscountRemoteSou"

    private object APIClient{
        val apiInterface : CreateDiscountAPIInterface by lazy {
            AppRetrofit.retrofit.create(CreateDiscountAPIInterface::class.java)
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

}