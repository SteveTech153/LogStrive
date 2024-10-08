package com.example.logstrive.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.logstrive.MyApp
import com.example.logstrive.R
import com.example.logstrive.util.SessionManager
import com.example.logstrive.presentation.factory.ViewModelFactory
import com.example.logstrive.databinding.FragmentLoginBinding
import com.example.logstrive.presentation.viewModel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory((requireActivity().application as MyApp).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.tvSingupNavigate.setOnClickListener{
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(action)
        }

        binding.loginBtn.setOnClickListener{
            val username = binding.etvUsername.text.toString()
            val password = binding.etvPassword.text.toString()

            userViewModel.login(requireContext(), username, password){ success ->
                if(success){
                    if(!SessionManager.isLoggedIn(requireContext())){
                        Toast.makeText(context, getString(R.string.login_successful), Toast.LENGTH_LONG).show()
                    }
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }else{
                    Snackbar.make(binding.root, getString(R.string.invalid_login_credentials), Snackbar.LENGTH_LONG).setTextMaxLines(5).show()
                }
            }
        }

        return binding.root
    }

}