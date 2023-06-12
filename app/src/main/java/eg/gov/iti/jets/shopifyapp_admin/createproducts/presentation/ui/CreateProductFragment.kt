package eg.gov.iti.jets.shopifyapp_admin.createproducts.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import eg.gov.iti.jets.shopifyapp_admin.R
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.PriceRuleB
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.PriceRuleBody
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.remote.APIState
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.remote.CreateDiscountRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.repo.CreateDiscountRepoImp
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.presentation.viewmodel.CreateDiscountViewModel
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.presentation.viewmodel.CreateDiscountViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductB
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductResponse
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.remote.CreateProductRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.repo.CreateProductRepoImp
import eg.gov.iti.jets.shopifyapp_admin.createproducts.domain.remote.CreateProductRemoteSource
import eg.gov.iti.jets.shopifyapp_admin.createproducts.presentation.viewmodel.CreateProductViewModel
import eg.gov.iti.jets.shopifyapp_admin.createproducts.presentation.viewmodel.CreateProductViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentCreateDiscountBinding
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentCreateProductBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CreateProductFragment : Fragment() {

    private val TAG = "CreateProductFragment"
    private lateinit var binding: FragmentCreateProductBinding

    private val viewModel: CreateProductViewModel by lazy {

        val factory = CreateProductViewModelFactory(
            CreateProductRepoImp.getInstance(CreateProductRemoteSourceImp())!!
        )

        ViewModelProvider(this, factory)[CreateProductViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateProductBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // observeCreateProduct()
       // observeGetProduct()
        //observeUpdateProduct()
        observeDeleteProduct()
    }


    private fun observeCreateProduct() {

        viewModel.createProduct(ProductBody(ProductB("sdmlkkl", "sdmk", "lkdsmms", "dsm")))

        lifecycleScope.launch {
            viewModel.productState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeCreateProduct: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeCreateProduct: $it")
                    }
                }
            }
        }
    }

    private fun observeGetProduct() {

        viewModel.getProducts()

        lifecycleScope.launch {
            viewModel.getProductState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeGetProduct: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeGetProduct: $it")
                    }
                }
            }
        }
    }

    private fun observeUpdateProduct() {

        viewModel.updateProduct(8354252652825,ProductResponse(
            Product(id = 8354252652825, title = "test new title 1")))

        lifecycleScope.launch {
            viewModel.updateProductState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeUpdateProduct: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeUpdateProduct: $it")
                    }
                }
            }
        }
    }

    private fun observeDeleteProduct() {

        viewModel.deleteProduct(8354252652825)

        lifecycleScope.launch {
            viewModel.deleteProductState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeDeleteProduct: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeDeleteProduct: $it")
                    }
                }
            }
        }
    }

}