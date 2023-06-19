package eg.gov.iti.jets.shopifyapp_admin.auth.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.Display.Mode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import eg.gov.iti.jets.shopifyapp_admin.BuildConfig
import eg.gov.iti.jets.shopifyapp_admin.R
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentHomeBinding
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var editor: Editor


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editor = requireContext().getSharedPreferences("mypref", Context.MODE_PRIVATE).edit()

        binding.btnLogIn.setOnClickListener {
            if(validateData()){
                login()
                editor.putBoolean("isLogin",true).commit()
            }
        }
    }

    private fun validateData() : Boolean{
        if (binding.userNameEditText.text.toString() != BuildConfig.store_name){
            binding.userNameEditText.error = "user name is not correct"
            return false
        }
        if (binding.passwordEditText.text.toString() != BuildConfig.admin_passowrd){
            binding.userNameEditText.error = "password is not correct"
            return false
        }
        Toast.makeText(requireContext(),"Login Successfully",Toast.LENGTH_LONG).show()
        return true
    }

    private fun login(){
        Navigation.findNavController(binding.root).navigate(R.id.allProductsFragment)
    }

}