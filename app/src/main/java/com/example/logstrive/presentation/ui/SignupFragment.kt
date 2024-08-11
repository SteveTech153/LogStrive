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
import com.example.logstrive.presentation.factory.ViewModelFactory
import com.example.logstrive.databinding.FragmentSignupBinding
import com.example.logstrive.presentation.viewModel.UserViewModel
import com.example.logstrive.util.Validator
import com.google.android.material.snackbar.Snackbar

class SignupFragment : Fragment() {
    lateinit var binding: FragmentSignupBinding
    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory((requireActivity().application as MyApp).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater)

        binding.tvLoginNavigate.setOnClickListener{
            val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        binding.signupBtn.setOnClickListener{
            val username = binding.etvNewusername.text.toString()
            val newPassword = binding.etvNewpassword.text.toString()
            val confirmPassword = binding.etvConfirmpassword.text.toString()
            when{
                !Validator.isValidUsername(username) -> {
                    Snackbar.make(binding.root, getString(R.string.username_condition), Snackbar.LENGTH_LONG).setTextMaxLines(5).show()
                }
                !Validator.isPasswordValid(newPassword) -> {
                    Snackbar.make(binding.root, getString(R.string.password_condition), Snackbar.LENGTH_LONG).setTextMaxLines(5).show()
                }
                !newPassword.equals(confirmPassword) -> {
                    Snackbar.make(binding.root, getString(R.string.passwords_didnt_match), Snackbar.LENGTH_LONG).setTextMaxLines(5).show()
                }
                else -> {
                    userViewModel.signup(requireContext(), username, newPassword){ success ->
                        if(success){
                            Toast.makeText(context, getString(R.string.login_successful), Toast.LENGTH_LONG).show()
                            val intent = Intent(activity, HomeActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }else{
                            Snackbar.make(binding.root, getString(R.string.username_already_exists), Snackbar.LENGTH_LONG).setTextMaxLines(5).show()
                        }
                    }
                }
            }
        }

        return binding.root
    }

}