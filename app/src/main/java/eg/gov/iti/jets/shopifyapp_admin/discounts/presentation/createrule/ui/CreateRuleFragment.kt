package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.createrule.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentCreateRuleBinding
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.remote.DiscountRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.repo.DiscountRepoImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.createrule.viewmodel.CreateRuleViewModel
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.createrule.viewmodel.CreateRuleViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.util.buildDate
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog
import eg.gov.iti.jets.shopifyapp_admin.util.get12HourFormat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


class CreateRuleFragment : Fragment() {

    private val TAG = "CreateRuleFragment"
    private lateinit var binding: FragmentCreateRuleBinding
    private var startDate : String? = null
    private var endDate : String? = null

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
        addTimePicker()
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
            startsAt = startDate,
            endsAt = endDate,
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
            binding.startAtEditText.error = "should have a start date"
            return false
        }
        if (binding.startTimeEditText.text.toString().isNullOrEmpty()) {
            binding.startTimeEditText.error = "should have a start time"
            return false
        }
        if (!binding.endsAtEditText.text.toString().isNullOrEmpty()) {
            if (binding.endTimeEditText.text.toString().isNullOrEmpty()) {
                binding.endTimeEditText.error = "should have a end time"
                return false
            }
        }
        if (!binding.endTimeEditText.text.toString().isNullOrEmpty()) {
            if (binding.endsAtEditText.text.toString().isNullOrEmpty()) {
                binding.endsAtEditText.error = "should have a end date"
                return false
            }
        }
        if(!validateDate()){
            Toast.makeText(requireContext(),"End date before start date",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun validateDate() : Boolean{
        startDate = binding.startAtEditText.text.toString() + " " + binding.startTimeEditText.text
        endDate = binding.endsAtEditText.text.toString() + " " + binding.endTimeEditText.text

       if(!binding.endsAtEditText.text.toString().isNullOrEmpty())
           return buildDate(startDate!!)!!.before(buildDate(endDate!!))

        return true
    }

    private fun addDatePricker() {
        binding.startAtEditText.setOnClickListener {
            showDatePicker(it)
        }
        binding.endsAtEditText.setOnClickListener {
            showDatePicker(it)
        }
    }

    private fun addTimePicker() {
        binding.startTimeEditText.setOnClickListener {
            showTimeDialog(it)
        }
        binding.endTimeEditText.setOnClickListener {
            showTimeDialog(it)
        }
    }

    private fun showDatePicker(view: View) {
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

    private fun showTimeDialog(view: View) {
        val c2: Calendar = Calendar.getInstance()
        val mHour = c2.get(Calendar.HOUR_OF_DAY)
        val mMinute = c2.get(Calendar.MINUTE)


        val timePickerDialog = TimePickerDialog(
            requireActivity(), { _, hourOfDay, minute ->

                val time = get12HourFormat(hourOfDay, minute)
                if (view.id == binding.startTimeEditText.id) {
                    binding.startTimeEditText.setText(time)
                } else {
                    binding.endTimeEditText.setText(time)
                }

            }, mHour!!, mMinute!!, false
        )
        timePickerDialog.show()
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