package eg.gov.iti.jets.shopifyapp_admin.products.presentation.createproduct.ui

import android.Manifest
import android.R
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
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.products.data.remote.ProductRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.products.data.repo.ProductRepoImp
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.createproduct.viewmodel.CreateProductViewModel
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.createproduct.viewmodel.CreateProductViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.productdetails.ui.ImagesAdapter
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.productdetails.ui.ProductImageViewPagerAdapter
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog
import eg.gov.iti.jets.shopifyapp_user.products.presentation.ui.ProductsAdapter
import eg.gov.iti.jets.shopifyapp_user.products.presentation.ui.VariantAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class CreateProductFragment : Fragment(), ImageListener {

    private val TAG = "CreateProductFragment"
    private lateinit var binding: FragmentCreateProductBinding
    private val STORAGE_PERMISSION_CODE = 1002
    private val PICK_IMAGE_CODE = 105
    private var imageUri: Uri? = null
    private lateinit var variantAdapter: VariantAdapter
    private var variantList = mutableListOf(Variant())
    private var imageList = mutableListOf(Image())
    private lateinit var imagesAdapter: ImagesAdapter
    private var imagePosition = 0

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
        setUpVariantRecyclerView()
        addVariantAction()
        setUpImagesRecyclerView()
        setUpMenus()
        handleAddImage()
    }

    private fun setUpVariantRecyclerView() {
        variantAdapter = VariantAdapter(variantList, requireActivity())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.variantsRecyclerView.layoutManager = layoutManager
        binding.variantsRecyclerView.adapter = variantAdapter
    }

    private fun setUpImagesRecyclerView() {
        imagesAdapter = ImagesAdapter(imageList, this,requireContext())
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.imagesRecyclerView.layoutManager = layoutManager
        binding.imagesRecyclerView.adapter = imagesAdapter
    }

    private fun setUpMenus(){
        val vendorsList =
            listOf(
                "ADIDAS", "ASICS TIGER", "CONVERSE","DR MARTENS","FLEX FIT","HERSCHEL","NIKE","PALLADUIM","PUMA",
                "TIMBERLAND","SUPRA","VANS")

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.select_dialog_item, vendorsList)

        binding.vendorEditText.threshold = 1
        binding.vendorEditText.setAdapter(adapter)
        binding.vendorEditText.setTextColor(Color.BLACK)

        val typeList = listOf("SHOES", "ACCESSORIES", "T-SHIRTS")

        val adapter2: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.select_dialog_item, typeList)

        binding.typeEditText.threshold = 1
        binding.typeEditText.setAdapter(adapter2)
        binding.typeEditText.setTextColor(Color.BLACK)
    }

    private fun addVariantAction() {
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

        imageList = imagesAdapter.images
        if (imageUri != null) {
            productB.images = imagesAdapter.getImagesInBase64() as ArrayList<Image>
        }

        productB.variants = variantAdapter.variantsList
        productB.variants[0].price = binding.priceEditText.text.toString()

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
        if (binding.priceEditText.text.toString().isNullOrEmpty()
            || (binding.priceEditText.text.toString()).toDouble() <= 0.0) {
            binding.priceEditText.error = "should have a price greater than 0"
            return false
        }
        if (binding.descEditText.text.toString().isNullOrEmpty()) {
            binding.descEditText.error = "should have a description"
            return false
        }
        if (binding.typeEditText.text.toString().isNullOrEmpty()) {
            binding.typeEditText.error = "should have a type"
            return false
        }
        if (!imagesAdapter.validateImagesListCount()) {
            Toast.makeText(requireActivity(), "product should have at least one image", Toast.LENGTH_LONG)
                .show()
            return false
        }
        if (!imagesAdapter.validateImagesList()) {
            Toast.makeText(requireActivity(), "delete unused images", Toast.LENGTH_LONG)
                .show()
            return false
        }
        if (!variantAdapter.checkDataValidation()) {
            return false
        }
        return true
    }


    private fun handleAddImage() {
        binding.addImageBtn.setOnClickListener {
            imagesAdapter.images.add(Image())
            imagesAdapter.notifyDataSetChanged()
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
            imagesAdapter.images[imagePosition].alt = imageUri.toString()
            imagesAdapter.notifyDataSetChanged()
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
        this.requestPermissions(
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

    override fun handleImageAction(position: Int) {
        this.imagePosition = position
        if (checkPermission()) {
            pickPicture()
        } else {
            requestPermission()
        }
    }


}