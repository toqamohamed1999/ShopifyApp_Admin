package eg.gov.iti.jets.shopifyapp_admin.discounts.data.remote

import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.*
import retrofit2.http.*

interface DiscountAPIInterface {

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @POST("price_rules.json")
    suspend fun createPriceRule(
        @Body body : PriceRuleBody
    ): PriceRuleResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @POST("price_rules/{price_rule_id}/discount_codes.json")
    suspend fun createDiscountCode(
        @Path("price_rule_id") ruleID : Long,
        @Body body: DiscountCodeBody
    ): DiscountCodeResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("price_rules.json?")
    suspend fun getPriceRules(): PriceRuleRoot

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("price_rules/{price_rule_id}/discount_codes.json")
    suspend fun getDiscounts(@Path("price_rule_id") ruleID : Long): DiscountCodeRoot

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @PUT("price_rules/{price_rule_id}/discount_codes/{discount_code_id}.json")
    suspend fun updateDiscount(
        @Path("price_rule_id") ruleID : Long,
        @Path("discount_code_id") discountId : Long,
        @Body body: DiscountCodeResponse
    ): DiscountCodeResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @DELETE("price_rules/{price_rule_id}/discount_codes/{discount_code_id}.json")
    suspend fun deleteDiscount(
        @Path("price_rule_id") ruleID : Long,
        @Path("discount_code_id") discountId : Long,
    )

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @PUT("price_rules/{price_rule_id}.json")
    suspend fun updatePriceRule(
        @Path("price_rule_id") ruleID : Long,
        @Body body: PriceRuleResponse
    ): PriceRuleResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @DELETE("price_rules/{price_rule_id}.json")
    suspend fun deletePriceRule(
        @Path("price_rule_id") ruleID : Long
    )


}
