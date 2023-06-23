package eg.gov.iti.jets.shopifyapp_admin.products.presentation.allproducts.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import eg.gov.iti.jets.shopifyapp_admin.R
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.products.data.remote.ProductRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.products.data.repo.ProductRepoImp
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.allproducts.viewmodel.AllProductViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.allproducts.viewmodel.AllProductsViewModel
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentAllProductsBinding
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog
import eg.gov.iti.jets.shopifyapp_user.products.presentation.ui.ProductListener
import eg.gov.iti.jets.shopifyapp_user.products.presentation.ui.ProductsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AllProductsFragment : Fragment(), ProductListener {

    private val TAG = "AllProductsFragment"
    private lateinit var binding: FragmentAllProductsBinding
    private var productsList: MutableList<Product> = mutableListOf()
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var product: Product


    private val viewModel: AllProductsViewModel by lazy {

        val factory = AllProductViewModelFactory(
            ProductRepoImp.getInstance(ProductRemoteSourceImp())!!
        )
        ViewModelProvider(this, factory)[AllProductsViewModel::class.java]
    }

    private val alertDialog : AlertDialog by lazy {
        createAlertDialog(requireContext(),"")
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
        addAction()
        observeGetProduct()
        observeDeleteProduct()
        searchProduct()
        //observeGetVariant()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getProducts()
    }

    private fun setUpRecyclerView() {
        productsAdapter = ProductsAdapter(ArrayList(), requireActivity(), this)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productsRecyclerView.layoutManager = layoutManager
        binding.productsRecyclerView.adapter = productsAdapter
    }

    private fun addAction() {
        binding.addFloatingBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.createProductFragment)
        }
    }

    private fun observeGetProduct() {
        lifecycleScope.launch {
            viewModel.getProductState.collectLatest {
                when (it) {
                    is APIState.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }
                    is APIState.Success -> {
                        binding.progressbar.visibility = View.GONE
                        productsList = it.data as MutableList<Product>
                        productsAdapter.setProductList(productsList)
                        Log.i(TAG, "observeGetProduct: " + it.data)
                    }
                    else -> {
                        binding.progressbar.visibility = View.GONE
                        Log.i(TAG, "observeGetProduct: $it")
                    }
                }
            }
        }
    }

    private fun observeDeleteProduct() {
        lifecycleScope.launch {
            viewModel.deleteProductState.collectLatest {
                when (it) {
                    is APIState.Loading -> {
                    }
                    is APIState.Success -> {
                        alertDialog.dismiss()
                        productsList.remove(product)
                        productsAdapter.changeData(productsList)
                        Toast.makeText(requireActivity(),"deleted successfully", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(),"deletion failed", Toast.LENGTH_LONG).show()
                        Log.i(TAG, "observeDeleteProduct: $it")
                    }
                }
            }
        }
    }


    override fun deleteProduct(product: Product) {
        this.product = product
        handleDeleteAction()
    }

    private fun handleDeleteAction(){
        AlertDialog.Builder(context)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                viewModel.deleteProduct(product.id!!)
                alertDialog.show()
            }
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun searchProduct(){
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = filterProducts(s.toString())
                showNoMatching(filteredList)
                if (filteredList != null) {
                    productsAdapter.setProductList(filteredList)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterProducts(s: String): List<Product>? {

        return productsList?.filter { it.title!!.lowercase().contains(s.lowercase()) }
    }
    private fun showNoMatching(filteredList: List<Product>?) {
        if (filteredList.isNullOrEmpty()) {
            binding.txtNoResults.visibility = View.VISIBLE
            binding.productsRecyclerView.visibility = View.GONE
        } else {

            binding.txtNoResults.visibility = View.GONE
            binding.productsRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun observeGetVariant() {
        viewModel.getVariantById(45349164220697)

        lifecycleScope.launch {
            viewModel.variantState.collectLatest {
                when (it) {
                    is APIState.Loading -> {
                    }
                    is APIState.Success -> {
                        Log.i(TAG, "observeGetVariant: " + it.data)
                    }
                    else -> {
                        Log.i(TAG, "observeGetVariant: $it")
                    }
                }
            }
        }
    }

}