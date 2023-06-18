package eg.gov.iti.jets.shopifyapp_admin.products.presentation.productdetails.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import eg.gov.iti.jets.shopifyapp_admin.R
import eg.gov.iti.jets.shopifyapp_admin.databinding.CouponImageBinding
import eg.gov.iti.jets.shopifyapp_admin.databinding.ImageItemBinding
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Image
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.createproduct.ui.ImageListener
import java.io.ByteArrayOutputStream

class ImagesAdapter(
    var images: MutableList<Image>, private val imageListener: ImageListener, val context: Context,
    var imagesUriList: MutableList<Uri> = mutableListOf()
) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    private val TAG = "ImagesAdapter"
    lateinit var binding: ImageItemBinding
    lateinit var holder: ViewHolder
    var bindKey = "create"

    inner class ViewHolder(var binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ImageItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]
        this.holder = holder
        if (position == 0) holder.binding.deleteImage.visibility = View.GONE

       bind(position)

        binding.couponImageView.setOnClickListener {
            handleImageAction(position)
        }

        binding.deleteImage.setOnClickListener {
            images.removeAt(position)
            notifyDataSetChanged()
        }
    }

    private fun handleImageAction(position: Int) {
        holder.binding.couponImageView.setOnClickListener {
            imageListener.handleImageAction(position)
        }
    }

    fun getImagesInBase64(): MutableList<Image> {
        for (i in images.indices) {
            images[i].attachment = convertImageToBase64(i)
        }
        return images
    }


    private fun convertImageToBase64(position: Int): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, imagesUriList[position]))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imagesUriList[position])
        }
       // val bitmap = holder.binding.couponImageView.drawable.toBitmap()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    fun validateImagesUriList(): Boolean {
        if (imagesUriList.size == 0) {
            return false
        }
        return true
    }

    fun validateImagesListCount(): Boolean {
        if (imagesUriList.size != images.size) {
            return false
        }
        return true
    }

    fun setUpdateProductData(imageList : MutableList<Image>){
        this.images = imageList
        notifyDataSetChanged()
    }

    private fun bind(position: Int){
        if(bindKey == "create") {
            if (imagesUriList.size > position) {
                holder.binding.couponImageView.setImageURI(imagesUriList[position])
            } else {
                holder.binding.couponImageView.setImageResource(R.drawable.baseline_add_photo_alternate_24)
            }
        }else{
            Glide.with(context)
                .load(images[position].src)
                .into(holder.binding.couponImageView)
        }
    }


}