package com.example.logstrive

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
import com.example.logstrive.databinding.FragmentLoginBinding
import com.example.logstrive.databinding.FragmentSignupBinding
import com.google.android.material.snackbar.Snackbar

class SignupFragment : Fragment() {
    lateinit var binding: FragmentSignupBinding
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((requireActivity().application as MyApp).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)

        binding.tvLoginNavigate.setOnClickListener{
            val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        binding.signupBtn.setOnClickListener{
            val username = binding.etvNewusername.text.toString()
            val newPassword = binding.etvNewpassword.text.toString()
            val confirmPassword = binding.etvConfirmpassword.text.toString()
            when{
                !userViewModel.isUsernameValid(username) -> {
                    Snackbar.make(binding.root, "Username must be alphanumeric and between 6 and 15 characters.", Snackbar.LENGTH_LONG).show()
                }
                !userViewModel.isPasswordValid(newPassword) -> {
                    Snackbar.make(binding.root, "Password must contain a lowercase, uppercase, number, a special character and between 6 and 12 characters.", Snackbar.LENGTH_LONG).show()
                }
                !newPassword.equals(confirmPassword) -> {
                    Snackbar.make(binding.root, "Passwords didn't match", Snackbar.LENGTH_LONG).show()
                }
                else -> {
                    userViewModel.signup(requireContext(), username, newPassword){ success ->
                        if(success){
                            Toast.makeText(context, "Login successful !", Toast.LENGTH_LONG).show()
                            val intent = Intent(activity, HomeActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }else{
                            Snackbar.make(binding.root, "Looks like username already exists", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        return binding.root
    }
}