package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentAllDiscountBinding
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.DiscountCode
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.remote.DiscountRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.repo.DiscountRepoImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.viewmodel.AllDiscountsViewModel
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.viewmodel.AllDiscountsViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AllDiscountFragment : Fragment() {

    private val TAG = "AllDiscountFragment"

    private lateinit var binding: FragmentAllDiscountBinding
    private lateinit var discountAdapter: DiscountAdapter
    private var discountsList: List<DiscountCode> = ArrayList()

    private val viewModel: AllDiscountsViewModel by lazy {

        val factory = AllDiscountsViewModelFactory(
                DiscountRepoImp.getInstance(
                    DiscountRemoteSourceImp()
                )!!)
        ViewModelProvider(this, factory)[AllDiscountsViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeGetDiscounts()
    }

    private fun setUpRecyclerView() {
        discountAdapter = DiscountAdapter()

        binding.discountRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = discountAdapter
        }
    }


    private fun observeGetDiscounts() {
        viewModel.getDiscounts(1385614967065)

        lifecycleScope.launch {
            viewModel.getDiscountState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {
                        Log.i(TAG, "observeGetDiscounts: ${it.data}")
                        discountsList = it.data
                        discountAdapter.submitList(discountsList)
                    }
                    else -> {
                        Log.i(TAG, "observeGetDiscounts: $it")
                    }
                }
            }
        }
    }

}