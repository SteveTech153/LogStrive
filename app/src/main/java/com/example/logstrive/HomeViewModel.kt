package com.example.logstrive

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
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
//                val quote = RetrofitInstance.api.getQuote()[0]
//                binding.tvQuote.text = quote.q ?: "Bla Balabla bla"
//                binding.tvAuthor.text = " - ${quote.a}"
                _quote.value = RetrofitInstance.api.getQuote()[0]
            } catch (e: Exception) {
                // Handle error
//                binding.tvQuote.text = "Failed to fetch quote"
//                Log.e("HomeFragment", "Error fetching quote", e)
            }
        }
    }
}