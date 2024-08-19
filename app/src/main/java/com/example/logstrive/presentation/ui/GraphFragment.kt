package com.example.logstrive.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logstrive.MyApp
import com.example.logstrive.R
import com.example.logstrive.databinding.FragmentGraphBinding
import com.example.logstrive.presentation.adapter.GraphItemAdapter
import com.example.logstrive.presentation.factory.ViewModelFactory
import com.example.logstrive.presentation.viewModel.HabitViewModel
import com.example.logstrive.presentation.viewModel.HabitViewModelFactory
import com.example.logstrive.presentation.viewModel.UserViewModel
import com.example.logstrive.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GraphFragment : Fragment() {
    private lateinit var binding: FragmentGraphBinding
    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory((requireActivity().application as MyApp).userRepository)
    }
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(requireActivity().application, (requireActivity().application as MyApp).habitRepository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGraphBinding.inflate(inflater, container, false)
        habitViewModel.allHabits.observe(viewLifecycleOwner) { habits ->
            lifecycleScope.launch {
                val accountCreatedDate = withContext(Dispatchers.IO) {
                    userViewModel.getAccountCreatedDate(SessionManager.getId(requireContext()))
                }
                habitViewModel.getGraphItemsForUserForHabitsFrom(habits, accountCreatedDate)
            }
        }
        binding.tvAddHabitsToStartGraph.visibility = View.VISIBLE
        habitViewModel.graphItems.observe(viewLifecycleOwner){ graphItems ->
            binding.rvGraphItems.adapter = GraphItemAdapter(graphItems)
            binding.rvGraphItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            if(graphItems.isNotEmpty()){
                binding.tvAddHabitsToStartGraph.visibility = View.GONE
            }
        }
        return binding.root
    }
}