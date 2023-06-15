package eg.gov.iti.jets.shopifyapp_admin.products.presentation.createproduct.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentCreateProductBinding
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductB
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.products.data.remote.ProductRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.products.data.repo.ProductRepoImp
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.createproduct.viewmodel.CreateProductViewModel
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.createproduct.viewmodel.CreateProductViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CreateProductFragment : Fragment() {

    private val TAG = "CreateProductFragment"
    private lateinit var binding: FragmentCreateProductBinding
    private val STORAGE_PERMISSION_CODE = 1002
    private val PICK_IMAGE_CODE = 105

    private val viewModel: CreateProductViewModel by lazy {

        val factory = CreateProductViewModelFactory(
            ProductRepoImp.getInstance(ProductRemoteSourceImp())!!
        )
        ViewModelProvider(this, factory)[CreateProductViewModel::class.java]
    }

    private val alertDialog: AlertDialog by lazy {
        createAlertDialog(requireContext(), "")
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

        addProductAction()
        observeCreateProduct()
        handleImageAction()
    }

    private fun addProductAction() {
        binding.addBtn.setOnClickListener {
            if (isDataValidate()) {
                viewModel.createProduct(ProductBody(buildProduct()))
                alertDialog.show()
            }
        }
    }

    private fun observeCreateProduct() {
        lifecycleScope.launch {
            viewModel.productState.collectLatest {
                when (it) {
                    is APIState.Loading -> {
                    }
                    is APIState.Success -> {
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(), "Created successfully", Toast.LENGTH_LONG)
                            .show()
                        findNavController(binding.root).popBackStack()
                        Log.i(TAG, "observeCreateProduct: ${it.data}")
                    }
                    else -> {
                        alertDialog.dismiss()
                        Log.i(TAG, "observeCreateProduct: $it")
                        Toast.makeText(requireActivity(), "Creation failed", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun buildProduct(): ProductB {
        return ProductB(
            title = binding.titleEditText.text.toString(),
            bodyHtml = binding.titleEditText.text.toString(),
            vendor = binding.vendorEditText.text.toString(),
            productType = binding.typeEditText.text.toString()
        )
    }

    private fun isDataValidate(): Boolean {
        if (binding.titleEditText.text.toString().isNullOrEmpty()) {
            binding.titleEditText.error = "should have a title"
            return false
        }
        if (binding.vendorEditText.text.toString().isNullOrEmpty()) {
            binding.titleEditText.error = "should have a vendor"
            return false
        }
        if (binding.descEditText.text.toString().isNullOrEmpty()) {
            binding.titleEditText.error = "should have a description"
            return false
        }
        if (binding.typeEditText.text.toString().isNullOrEmpty()) {
            binding.titleEditText.error = "should have a type"
            return false
        }
        return true
    }
    private fun handleImageAction() {
        binding.imageView.setOnClickListener {
            if (checkPermission()) {
                pickPicture()
            } else {
                requestPermission()
            }
        }
    }
    private fun pickPicture() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_CODE) {
            var imageUri = data?.data
            binding.imageView.setImageURI(imageUri)
        }
    }
    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermission() {
        this.requestPermissions( //Method of Fragment
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), STORAGE_PERMISSION_CODE
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickPicture()
            }
        }
    }


    /*
    private fun observeUpdateProduct() {

//        viewModel.updateProduct(8354252652825,ProductResponse(
//            Product(id = 8354252652825, title = "test new title 1")))

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



     */

}