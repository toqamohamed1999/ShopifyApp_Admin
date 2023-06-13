package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.updatediscount.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import eg.gov.iti.jets.shopifyapp_admin.R
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentCreateDiscountBinding
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.remote.DiscountRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.repo.DiscountRepoImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.ui.DiscountListener
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.creatediscount.ui.CreateDiscountFragment
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.creatediscount.viewmodel.CreateDiscountViewModel
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.creatediscount.viewmodel.CreateDiscountViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.updatediscount.viewmodel.UpdateDiscountViewModel
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.updatediscount.viewmodel.UpdateDiscountViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class UpdateDiscountFragment : DialogFragment() {

    private lateinit var binding : FragmentCreateDiscountBinding

    private val viewModel: UpdateDiscountViewModel by lazy {

        val factory = UpdateDiscountViewModelFactory(
            DiscountRepoImp.getInstance(DiscountRemoteSourceImp())!!
        )

        ViewModelProvider(this, factory)[UpdateDiscountViewModel::class.java]
    }

    private val alertDialog : AlertDialog by lazy {
        createAlertDialog(requireContext(),"")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    companion object {
        private lateinit var priceRule: PriceRule
        private lateinit var discountCode: DiscountCode
        private lateinit var discountListener: DiscountListener
        const val TAG = "UpdateDiscountFragment"

        fun newInstance(priceRule: PriceRule,discountCode: DiscountCode, discountListener: DiscountListener): UpdateDiscountFragment {
            this.priceRule = priceRule
            this.discountCode = discountCode
            this.discountListener = discountListener
            return UpdateDiscountFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateDiscountBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editActionBtn()
        observeUpdateDiscount()
    }

    private fun editActionBtn(){
        binding.addBtn.setOnClickListener {
            if (!binding.codeEditText.text.toString().isNullOrEmpty()) {
                viewModel.updateDiscount(priceRule.id!!,discountCode.id!!,
                       DiscountCodeResponse(DiscountCode(discountCode.id!!, code = binding.codeEditText.text.toString()))
                )
                alertDialog.show()
            }else{
                binding.codeEditText.error = "should have a code"
            }
        }
    }

    private fun observeUpdateDiscount() {
        lifecycleScope.launch {
            viewModel.updateDiscountState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeUpdateDiscount: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeUpdateDiscount: $it")
                    }
                }
            }
        }
    }
}