package com.example.logstrive.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logstrive.data.entity.Quote
import com.example.logstrive.data.network.RetrofitInstance
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _quote = MutableLiveData<Quote>()
    val quote: LiveData<Quote>
        get() = _quote
    init {
        if(_quote.value == null){
            fetchQuote()
        }
    }
    private fun fetchQuote() {
        viewModelScope.launch {
            try {
//                binding.progressBar.visibility = View.VISIBLE
                _quote.value = RetrofitInstance.api.getQuote()[0]
            } catch (e: Exception) {
                // set a default quote
            }
        }
    }
}