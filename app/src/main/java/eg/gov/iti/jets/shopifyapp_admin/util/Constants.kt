package eg.gov.iti.jets.shopifyapp_admin.util

import eg.gov.iti.jets.shopifyapp_admin.BuildConfig
import java.util.regex.Pattern

const val BASE_URL = "https://${BuildConfig.api_key}:${BuildConfig.api_password}@${BuildConfig.store_name}.myshopify.com/admin/api/${BuildConfig.api_version}/"
//const val BASE_URL = "https://${BuildConfig.store_name}.myshopify.com/admin/api/${BuildConfig.api_version}/"

const val PRICE_RULES = "price_rules.json"

const val DISCOUNT_CODES = "discount_codes.json"

const val DISCOUNT_CODES_CREATION = "price_rules/{price_rule_id}/discount_codes.json"


private const val apiKey = "9d218f5e95606bd93e59d4317577066b"
private const val hostName = "mad43-sv-and2.myshopify.com"
private const val password = "503186a0cc453efff3f2f50991f194bf"

