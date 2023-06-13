package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.creatediscount.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.remote.DiscountRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.repo.DiscountRepoImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.creatediscount.viewmodel.CreateDiscountViewModel
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.creatediscount.viewmodel.CreateDiscountViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentCreateDiscountBinding
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.ui.DiscountListener
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CreateDiscountFragment : DialogFragment() {

    private lateinit var binding : FragmentCreateDiscountBinding

    private val viewModel: CreateDiscountViewModel by lazy {

        val factory = CreateDiscountViewModelFactory(
            DiscountRepoImp.getInstance(DiscountRemoteSourceImp())!!
        )

        ViewModelProvider(this, factory)[CreateDiscountViewModel::class.java]
    }

    private val alertDialog : AlertDialog by lazy {
        createAlertDialog(requireContext(),"")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    companion object {
        private lateinit var priceRule: PriceRule
        private lateinit var discountListener: DiscountListener
        const val TAG = "CreateDiscountFragment"
        fun newInstance(priceRule1: PriceRule,discountListener1: DiscountListener): CreateDiscountFragment {
            priceRule = priceRule1
            discountListener = discountListener1
            return CreateDiscountFragment()
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
        addActionBtn()
        observeCreateDiscount()
    }

    private fun addActionBtn(){
        binding.addBtn.setOnClickListener {
            if (!binding.codeEditText.text.toString().isNullOrEmpty()) {
                viewModel.createDiscount(
                    priceRule.id ?: 0,
                    DiscountCodeBody(DiscountCodeB(binding.codeEditText.text.toString()))
                )
                alertDialog.show()
            }else{
                binding.codeEditText.error = "should have a code"
            }
        }
    }

    private fun observeCreateDiscount() {
        lifecycleScope.launch {
            viewModel.createDiscountState.collectLatest {
                when (it) {
                    is APIState.Loading -> {
                        //alertDialog.show()
                    }
                    is APIState.Success -> {
                        Log.i(TAG, "observeCreateDiscount: ${it.data}")
                        alertDialog.dismiss()
                        discountListener.getDiscounts()
                        Toast.makeText(requireActivity(),"Created successfully",Toast.LENGTH_LONG).show()
                        dismiss()
                    }
                    else -> {
                        Log.i(TAG, "observeCreateDiscount: $it")
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(),"Creation failed",Toast.LENGTH_LONG).show()
                        dismiss()
                    }
                }
            }
        }
    }


/*
    private fun observeRulesData() {

        lifecycleScope.launch {
            viewModel.ruleState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeRulesData: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "getRulesDataFromApi: $it")
                    }
                }
            }
        }
    }

    private fun observeCreateRule() {

        viewModel.createRule(PriceRuleBody
            (PriceRuleB("test title","line_item",
            "all","across","percentage","-24.0",
            "all",  "2021-21-19T17:59:10Z")))

        lifecycleScope.launch {
            viewModel.createRuleState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeCreateRule: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeCreateRule: $it")
                    }
                }
            }
        }
    }

        private fun observeGetDiscounts() {

            viewModel.getDiscounts(1385615130905)

            lifecycleScope.launch {
                viewModel.getDiscountState.collectLatest {
                    when (it) {
                        is APIState.Loading -> {

                        }
                        is APIState.Success -> {

                            Log.i(TAG, "observeGetDiscounts: ${it.data}")

                        }
                        else -> {

                            Log.i(TAG, "observeGetDiscounts: $it")
                        }
                    }
                }
            }
    }



    private fun observeDeleteDiscount() {

        viewModel.deleteDiscount(1385615130905,19605667971353)

        lifecycleScope.launch {
            viewModel.deleteDiscountState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeDeleteDiscount: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeDeleteDiscount: $it")
                    }
                }
            }
        }
    }

    private fun observeUpdateRule() {

        viewModel.updatePriceRule(1385615130905, PriceRuleResponse(
            PriceRule(id  = 1385615130905, title = "test title update", value = "-28.0")))

        lifecycleScope.launch {
            viewModel.updateRuleState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeUpdateRule: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeUpdateRule: $it")
                    }
                }
            }
        }
    }

    private fun observeDeleteRule() {

        viewModel.deletePriceRule(1385615130905)

        lifecycleScope.launch {
            viewModel.deleteRuleState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeDeleteRule: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeDeleteRule: $it")
                    }
                }
            }
        }
    }

*/
}