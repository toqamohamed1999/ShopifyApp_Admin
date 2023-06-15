package eg.gov.iti.jets.shopifyapp_user.products.presentation.ui

import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Product

interface ProductListener{
    fun deleteProduct(product : Product)
}