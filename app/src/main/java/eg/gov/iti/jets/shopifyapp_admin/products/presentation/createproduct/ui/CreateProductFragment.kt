package eg.gov.iti.jets.shopifyapp_admin.products.presentation.createproduct.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentCreateProductBinding
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ImageB
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductB
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Variant
import eg.gov.iti.jets.shopifyapp_admin.products.data.remote.ProductRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.products.data.repo.ProductRepoImp
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.createproduct.viewmodel.CreateProductViewModel
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.createproduct.viewmodel.CreateProductViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog
import eg.gov.iti.jets.shopifyapp_user.products.presentation.ui.ProductsAdapter
import eg.gov.iti.jets.shopifyapp_user.products.presentation.ui.VariantAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class CreateProductFragment : Fragment() {

    private val TAG = "CreateProductFragment"
    private lateinit var binding: FragmentCreateProductBinding
    private val STORAGE_PERMISSION_CODE = 1002
    private val PICK_IMAGE_CODE = 105
    private var imageUri: Uri? = null
    private lateinit var variantAdapter : VariantAdapter
    private var variantList = mutableListOf(Variant())

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
        setUpRecyclerView()
        addVariantAction()
    }

    private fun setUpRecyclerView() {
        variantAdapter = VariantAdapter(variantList, requireActivity())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.variantsRecyclerView.layoutManager = layoutManager
        binding.variantsRecyclerView.adapter = variantAdapter
    }
    private fun addVariantAction(){
        binding.addVariantBtn.setOnClickListener {
            variantAdapter.variantsList.add(Variant())
            variantAdapter.notifyDataSetChanged()
        }
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
        var productB = ProductB(
            title = binding.titleEditText.text.toString(),
            bodyHtml = binding.descEditText.text.toString(),
            vendor = binding.vendorEditText.text.toString(),
            productType = binding.typeEditText.text.toString(),
        )

        if (imageUri != null) {
            productB.images = arrayListOf(ImageB(attachment = convertImageToBase64()))
        }

        productB.variants = variantAdapter.variantsList

        return productB
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
        if (imageUri == null) {
            Toast.makeText(requireActivity(),"you should choose a picture", Toast.LENGTH_LONG).show()
            return false
        }
        if(!variantAdapter.checkDataValidation()){
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
            imageUri = data?.data
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

    private fun convertImageToBase64(): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val bitmap = binding.imageView.drawable.toBitmap()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
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