package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.createrule.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import eg.gov.iti.jets.shopifyapp_admin.R
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentCreateDiscountBinding
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentCreateRuleBinding
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.remote.DiscountRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.repo.DiscountRepoImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.ui.DiscountListener
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.creatediscount.ui.CreateDiscountFragment
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.creatediscount.viewmodel.CreateDiscountViewModel
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.creatediscount.viewmodel.CreateDiscountViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.createrule.viewmodel.CreateRuleViewModel
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.createrule.viewmodel.CreateRuleViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


class CreateRuleFragment : Fragment() {

    private val TAG = "CreateRuleFragment"
    private lateinit var binding: FragmentCreateRuleBinding

    private val viewModel: CreateRuleViewModel by lazy {

        val factory = CreateRuleViewModelFactory(
            DiscountRepoImp.getInstance(
                DiscountRemoteSourceImp()
            )!!
        )

        ViewModelProvider(this, factory)[CreateRuleViewModel::class.java]
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
        binding = FragmentCreateRuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addActionBtn()
        observeCreateRule()
        addDatePricker()
    }

    private fun addActionBtn() {
        binding.addBtn.setOnClickListener {
            if (isDataValidate()) {
                viewModel.createPriceRule(PriceRuleBody(buildRule()))
                alertDialog.show()
            }
        }
    }

    private fun observeCreateRule() {
        lifecycleScope.launch {
            viewModel.createRuleState.collectLatest {
                when (it) {
                    is APIState.Loading -> {
                    }
                    is APIState.Success -> {
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(), "Created successfully", Toast.LENGTH_LONG)
                            .show()
                        Navigation.findNavController(binding.root).popBackStack()
                        Log.i(TAG, "observeCreateRule: ${it.data}")
                    }
                    else -> {
                        alertDialog.dismiss()
                        Log.i(TAG, "observeCreateRule: $it")
                        Toast.makeText(requireActivity(), "Creation failed", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun buildRule(): PriceRuleB {
        val value = (binding.valueEditText.text.toString()).toDouble() * -1.0

        return PriceRuleB(
            title = binding.titleEditText.text.toString(),
            value = value.toString(),
            startsAt = binding.startAtEditText.text.toString(),
            endsAt = binding.endsAtEditText.text.toString(),
            targetType = "line_item",
            allocationMethod = "across",
            targetSelection = "all",
            customerSelection = "all",
            valueType = "percentage",
        )
    }

    private fun isDataValidate(): Boolean {
        if (binding.titleEditText.text.toString().isNullOrEmpty()) {
            binding.titleEditText.error = "should have a title"
            return false
        }
        if (binding.valueEditText.text.toString().isNullOrEmpty()) {
            binding.titleEditText.error = "should have a value"
            return false
        }
        if ((binding.valueEditText.text.toString()).toDouble() > 100
            || (binding.valueEditText.text.toString()).toDouble() < 1
        ) {
            binding.valueEditText.error = "discount value should be between 1 and 100"
            return false
        }
        if (binding.startAtEditText.text.toString().isNullOrEmpty()) {
            binding.titleEditText.error = "should have a start date"
            return false
        }
        return true
    }

    private fun addDatePricker() {
        binding.startAtEditText.setOnClickListener {
            startDatePicker(it)
        }
        binding.endsAtTextLayout.setOnClickListener {
            startDatePicker(it)
        }
    }

    private fun startDatePicker(view: View) {
        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->

                val strDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                if (view.id == binding.startAtEditText.id) {
                    binding.startAtEditText.setText(strDate)
                } else {
                    binding.endsAtEditText.setText(strDate)
                }
            },
            mYear!!,
            mMonth!!,
            mDay!!
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000;
        datePickerDialog.show()
    }

    //
//        viewModel.createPriceRule(
//            PriceRuleBody
//                (
//                PriceRuleB(
//                    "TESTAFTww", "line_item",
//                    "all", "across", "percentage", "-1.0",
//                    "all", "2017-11-19T17:59:10Z"
//                )
//            )
//        )

}