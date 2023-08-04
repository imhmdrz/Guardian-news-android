package com.example.myproj.uI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproj.databinding.FragmentGuardianBinding
import com.example.myproj.loadDataFromInternet.ApiResult
import com.example.myproj.repository.GuardianRepository
import kotlinx.coroutines.launch

class GuardianFragment(val type: String) : Fragment() {
    companion object {
        fun newInstance(type: String) = GuardianFragment(type)
    }
    private lateinit var viewModel: GuardianViewModel
    private var _binding: FragmentGuardianBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuardianBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = GuardianViewModelFactory(GuardianRepository)
        viewModel = ViewModelProvider(
            this,
            factory
        ).get(GuardianViewModel::class.java)
        lifecycle.coroutineScope.launch {
            uIType()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun uIProvider(it: List<ApiResult>){
        if (it.isNotEmpty()) {
            if (it[0].id == "NO INTERNET CONNECTION") {
                binding.recyclerView.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.tvError.setOnClickListener {
                    viewModel.refreshData()
                }
            } else {
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = RvAdapter(requireContext(), it)
                binding.progressBar.visibility = View.GONE
                binding.tvError.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }
    private suspend fun uIType() {
        when (type) {
            "Home" -> viewModel.dataHome.collect() {
                uIProvider(it)
            }

            "World" -> viewModel.dataWorld.collect() {
                uIProvider(it)
            }

            "Science" -> viewModel.dataScience.collect() {
                uIProvider(it)
            }

            "Sport" -> viewModel.dataSport.collect() {
                uIProvider(it)
            }

            "Environment" -> viewModel.dataEnvironment.collect() {
                uIProvider(it)
            }
        }
    }
}