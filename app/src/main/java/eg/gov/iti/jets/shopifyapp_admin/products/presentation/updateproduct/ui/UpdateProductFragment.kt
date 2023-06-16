package eg.gov.iti.jets.shopifyapp_admin.products.presentation.updateproduct.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentUpdateProductBinding
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.products.data.remote.ProductRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.products.data.repo.ProductRepoImp
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.updateproduct.viewmodel.UpdateProductViewModel
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.updateproduct.viewmodel.UpdateProductViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog
import eg.gov.iti.jets.shopifyapp_user.products.presentation.ui.VariantAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class UpdateProductFragment : Fragment() {

    private val TAG = "UpdateProductFragment"
    private lateinit var binding: FragmentUpdateProductBinding
    private val args: UpdateProductFragmentArgs by navArgs()
    private val STORAGE_PERMISSION_CODE = 1002
    private val PICK_IMAGE_CODE = 105
    private var imageUri: Uri? = null
    private lateinit var variantAdapter: VariantAdapter
    private var variantList = mutableListOf<Variant>()
    private var product: Product? = null

    private val viewModel: UpdateProductViewModel by lazy {

        val factory = UpdateProductViewModelFactory(
            ProductRepoImp.getInstance(ProductRemoteSourceImp())!!
        )
        ViewModelProvider(this, factory)[UpdateProductViewModel::class.java]
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
        binding = FragmentUpdateProductBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        product = args.product

        bindProductData()
        addProductAction()
        observeUpdateProduct()
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

    private fun addVariantAction() {
        binding.addVariantBtn.setOnClickListener {
            variantAdapter.variantsList.add(Variant())
            variantAdapter.notifyDataSetChanged()
        }
    }

    private fun addProductAction() {
        binding.saveBtn.setOnClickListener {
            if (isDataValidate()) {
                buildProduct()
                viewModel.updateProduct(product?.id!!, ProductResponse(product!!))
                alertDialog.show()
            }
        }
    }

    private fun observeUpdateProduct() {
        lifecycleScope.launch {
            viewModel.updateProductState.collectLatest {
                when (it) {
                    is APIState.Loading -> {
                    }
                    is APIState.Success -> {
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(), "Updated successfully", Toast.LENGTH_LONG)
                            .show()
                        Navigation.findNavController(binding.root).popBackStack()
                        Log.i(TAG, "observeUpdateProduct: ${it.data}")
                    }
                    else -> {
                        alertDialog.dismiss()
                        Log.i(TAG, "observeUpdateProduct: $it")
                        Toast.makeText(requireActivity(), "Update failed", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun buildProduct() {
        product?.title = binding.titleEditText.text.toString()
        product?.bodyHtml = binding.descEditText.text.toString()
        product?.vendor = binding.vendorEditText.text.toString()
        product?.productType = binding.typeEditText.text.toString()

        if (imageUri != null) {
            product?.images = arrayListOf(Image(attachment = convertImageToBase64()))
        }
        product?.variants = variantAdapter.variantsList
    }

    private fun bindProductData() {
        if (product != null) {

            binding.titleEditText.setText(product?.title)
            binding.descEditText.setText(product?.bodyHtml)
            binding.vendorEditText.setText(product?.vendor)
            binding.typeEditText.setText(product?.productType)

            variantList = product?.variants as MutableList<Variant>
            setUpRecyclerView()

            Glide.with(requireContext())
                .load(product?.image?.src)
                .into(binding.imageView)
        }
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
        if (imageUri == null && product?.image?.src == null) {
            Toast.makeText(requireActivity(), "you should choose a picture", Toast.LENGTH_LONG)
                .show()
            return false
        }
        if (!variantAdapter.checkDataValidation()) {
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
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_CODE) {
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


}