package eg.gov.iti.jets.shopifyapp_admin.home.presentation.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import eg.gov.iti.jets.shopifyapp_admin.R
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentCreateDiscountBinding
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        addNavigation()
    }

    private fun setUpView(){
        binding.inventoryLayout.constraintLayout.setBackgroundColor(Color.parseColor("#FF9C27B0"))
        binding.inventoryLayout.titleTextview.text = "Inventory"
        binding.inventoryLayout.imageView.setImageResource(R.drawable.inventory);
        binding.discountsLayout.constraintLayout.setBackgroundColor(Color.parseColor("#FF8FE493"))
        binding.discountsLayout.titleTextview.text = "Discounts"
        binding.discountsLayout.imageView.setImageResource(R.drawable.discount);
    }

    private fun addNavigation(){
        binding.productsLayout.constraintLayout.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.allProductsFragment)
        }
        binding.discountsLayout.constraintLayout.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.allDiscountFragment)
        }
    }
}