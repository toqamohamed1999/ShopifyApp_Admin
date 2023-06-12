package eg.gov.iti.jets.shopifyapp_admin.util

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil

class MyDiffUtil<out T> : DiffUtil.ItemCallback<@UnsafeVariance T>() {

    override fun areItemsTheSame(@NonNull oldItem: @UnsafeVariance T, @NonNull newItem: @UnsafeVariance T): Boolean {
        return oldItem!! == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(@NonNull oldItem: @UnsafeVariance T, @NonNull newItem: @UnsafeVariance T): Boolean {
        return oldItem == newItem
    }
}