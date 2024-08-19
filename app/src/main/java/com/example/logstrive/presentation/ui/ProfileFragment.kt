package com.example.logstrive.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.logstrive.MyApp
import com.example.logstrive.R
import com.example.logstrive.databinding.FragmentProfileBinding
import com.example.logstrive.presentation.factory.ViewModelFactory
import com.example.logstrive.presentation.viewModel.HabitViewModelFactory
import com.example.logstrive.presentation.viewModel.UserViewModel
import com.example.logstrive.util.SessionManager
import com.example.logstrive.util.Validator
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val userViewModel: UserViewModel by viewModels{
        ViewModelFactory((requireActivity().application as MyApp).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val newUsernameEtv = binding.etvUsername
        newUsernameEtv.setText( SessionManager.getUsername(requireContext()) )
        binding.editBtn.setOnClickListener {
            if(newUsernameEtv.text?.isNotBlank()==true && Validator.isValidUsername(newUsernameEtv.text.toString())) {
                userViewModel.updateUsername(requireContext(), newUsernameEtv.text.toString(), SessionManager.getId(requireContext())){ success ->
                    if(success){
                        Snackbar.make(binding.root, R.string.username_edited, Snackbar.LENGTH_SHORT).show()
                    }else{
                        Snackbar.make(binding.root, "looks like username already exists!", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
            else{
                Snackbar.make(binding.root, R.string.please_enter_username_correctly, Snackbar.LENGTH_LONG).show()
            }
        }
        binding.logoutBtn.setOnClickListener {
            userViewModel.logout(requireContext())
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            Snackbar.make(binding.root, R.string.bye, Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }

}