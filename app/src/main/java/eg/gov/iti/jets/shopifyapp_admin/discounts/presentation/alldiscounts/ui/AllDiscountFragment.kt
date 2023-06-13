package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.ui

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentAllDiscountBinding
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.DiscountCode
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.DiscountCodeResponse
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.PriceRule
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.remote.DiscountRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.repo.DiscountRepoImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.viewmodel.AllDiscountsViewModel
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.viewmodel.AllDiscountsViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.creatediscount.ui.CreateDiscountFragment
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.updatediscount.ui.UpdateDiscountFragment
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AllDiscountFragment : Fragment() , DiscountListener{

    private val TAG = "AllDiscountFragment"

    private lateinit var binding: FragmentAllDiscountBinding
    private lateinit var discountAdapter: DiscountAdapter
    private var discountsList: MutableList<DiscountCode> = mutableListOf()
    private val args: AllDiscountFragmentArgs by navArgs()
    private lateinit var priceRule : PriceRule
    private lateinit var discountCode: DiscountCode

    private val viewModel: AllDiscountsViewModel by lazy {

        val factory = AllDiscountsViewModelFactory(
                DiscountRepoImp.getInstance(
                    DiscountRemoteSourceImp()
                )!!)
        ViewModelProvider(this, factory)[AllDiscountsViewModel::class.java]
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
        binding = FragmentAllDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        priceRule = args.priceRule

        setUpRecyclerView()
        addDiscountAction()
        observeGetDiscounts()
        observeDeleteDiscount()
    }

    private fun setUpRecyclerView() {
        discountAdapter = DiscountAdapter(this,priceRule.value!!)

        binding.discountRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = discountAdapter
        }
    }


    private fun observeGetDiscounts() {
        viewModel.getDiscounts(priceRule.id!!)

        lifecycleScope.launch {
            viewModel.getDiscountState.collectLatest {
                when (it) {
                    is APIState.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }
                    is APIState.Success -> {
                        Log.i(TAG, "observeGetDiscounts: ${it.data}")
                        discountsList = it.data.toMutableList()
                        discountAdapter.submitList(discountsList)
                        binding.progressbar.visibility = View.GONE
                    }
                    else -> {
                        binding.progressbar.visibility = View.GONE
                        Log.i(TAG, "observeGetDiscounts: $it")
                    }
                }
            }
        }
    }

    private fun observeDeleteDiscount() {
        lifecycleScope.launch {
            viewModel.deleteDiscountState.collectLatest {
                when (it) {
                    is APIState.Loading -> {
                    }
                    is APIState.Success -> {
                        alertDialog.dismiss()
                        discountsList.remove(discountCode)
                        discountAdapter.submitList(discountsList)
                        discountAdapter.changeData()
                        Toast.makeText(requireActivity(),"deleted successfully",Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(),"deletion failed", Toast.LENGTH_LONG).show()
                        Log.i(TAG, "observeDeleteDiscount: $it")
                    }
                }
            }
        }
    }


    private fun addDiscountAction(){
        binding.addFloatingBtn.setOnClickListener {
            CreateDiscountFragment.newInstance(priceRule,this)
                .show(childFragmentManager, CreateDiscountFragment.TAG)
        }
    }

    override fun getDiscounts() {
        viewModel.getDiscounts(priceRule.id!!)
    }

    override fun updateDiscount(discountCode: DiscountCode) {
        UpdateDiscountFragment.newInstance(priceRule,discountCode,this)
            .show(childFragmentManager, UpdateDiscountFragment.TAG)
    }
    override fun deleteDiscount(discountCode: DiscountCode) {
        this.discountCode = discountCode
        handleDeleteAction()
    }

    private fun handleDeleteAction(){
        AlertDialog.Builder(context)
            .setTitle("Delete Discount")
            .setMessage("Are you sure you want to delete this discount?")
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                viewModel.deleteDiscount(priceRule.id!!,discountCode.id!!)
                alertDialog.show()
            }
            .setNegativeButton("Cancel", null)
            .setIcon(R.drawable.ic_dialog_alert)
            .show()
    }

}