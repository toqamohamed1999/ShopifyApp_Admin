package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.updatediscount.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
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
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentUpdateDiscountBinding
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

    private lateinit var binding: FragmentUpdateDiscountBinding

    private val viewModel: UpdateDiscountViewModel by lazy {

        val factory = UpdateDiscountViewModelFactory(
            DiscountRepoImp.getInstance(DiscountRemoteSourceImp())!!
        )

        ViewModelProvider(this, factory)[UpdateDiscountViewModel::class.java]
    }

    private val alertDialog: AlertDialog by lazy {
        createAlertDialog(requireContext(), "")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        private lateinit var priceRule: PriceRule
        private lateinit var discountCode: DiscountCode
        private lateinit var discountListener: DiscountListener
        const val TAG = "UpdateDiscountFragment"

        fun newInstance(
            priceRule: PriceRule,
            discountCode: DiscountCode,
            discountListener: DiscountListener
        ): UpdateDiscountFragment {
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
        binding = FragmentUpdateDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.codeEditText.setText(discountCode.code!!)

        editActionBtn()
        observeUpdateDiscount()
    }

    private fun editActionBtn() {
        binding.saveEditBtn.setOnClickListener {
            if (discountCode.code == binding.codeEditText.text.toString()) {
                binding.codeEditText.error = "code have no change to save"
            } else if (binding.codeEditText.text.toString().isNullOrEmpty()) {
                handleEditAction()
            } else {
                binding.codeEditText.error = "should have a code"
            }
        }
    }

    private fun edit() {
        viewModel.updateDiscount(
            priceRule.id!!, discountCode.id!!,
            DiscountCodeResponse(
                DiscountCode(
                    discountCode.id!!,
                    code = binding.codeEditText.text.toString()
                )
            )
        )
        alertDialog.show()
    }


    private fun observeUpdateDiscount() {
        lifecycleScope.launch {
            viewModel.updateDiscountState.collectLatest {
                when (it) {
                    is APIState.Loading -> {
                        //alertDialog.show()
                    }
                    is APIState.Success -> {
                        Log.i(TAG, "observeUpdateDiscount: ${it.data}")
                        alertDialog.dismiss()
                        discountListener.getDiscounts()
                        Toast.makeText(requireActivity(), "Updated successfully", Toast.LENGTH_LONG)
                            .show()
                        dismiss()
                    }
                    else -> {
                        Log.i(CreateDiscountFragment.TAG, "observeUpdateDiscount: $it")
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(), "Update failed", Toast.LENGTH_LONG).show()
                        dismiss()
                    }
                }
            }
        }
    }

    private fun handleEditAction() {
        AlertDialog.Builder(context)
            .setTitle("Delete Discount")
            .setMessage("Are you sure you want to save edit?")
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                edit()
            }
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
}