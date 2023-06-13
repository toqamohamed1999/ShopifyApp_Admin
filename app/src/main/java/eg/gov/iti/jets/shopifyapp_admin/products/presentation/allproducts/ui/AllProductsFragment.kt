package eg.gov.iti.jets.shopifyapp_admin.products.presentation.allproducts.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.products.data.remote.ProductRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.products.data.repo.ProductRepoImp
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.allproducts.viewmodel.AllProductViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.allproducts.viewmodel.AllProductsViewModel
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentAllProductsBinding
import eg.gov.iti.jets.shopifyapp_user.products.presentation.ui.ProductListener
import eg.gov.iti.jets.shopifyapp_user.products.presentation.ui.ProductsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AllProductsFragment : Fragment(), ProductListener {

    private val TAG = "AllProductsFragment"
    private lateinit var binding: FragmentAllProductsBinding
    private var productsList:List<Product> = emptyList()
    private lateinit var productsAdapter : ProductsAdapter


    private val viewModel: AllProductsViewModel by lazy {

        val factory = AllProductViewModelFactory(
            ProductRepoImp.getInstance(ProductRemoteSourceImp())!!
        )

        ViewModelProvider(this, factory)[AllProductsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllProductsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeGetProduct()
    }

    private fun setUpRecyclerView(){
        productsAdapter = ProductsAdapter(ArrayList(), requireActivity() , this)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productsRecyclerView.layoutManager = layoutManager
        binding.productsRecyclerView.adapter = productsAdapter
    }

    private fun observeGetProduct() {
        lifecycleScope.launch {
            viewModel.getProductState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {
                        Log.i(TAG, "observeGetProduct: "+it.data)
                        productsList = it.data
                        productsAdapter.setProductList(productsList)
                    }
                    else -> {
                        Log.i(TAG, "observeGetProduct: $it")
                    }
                }
            }
        }
    }


    override fun deleteProduct(product: Product) {

    }
}