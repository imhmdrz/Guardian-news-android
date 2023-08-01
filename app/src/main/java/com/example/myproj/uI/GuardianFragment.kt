package com.example.myproj.uI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproj.databinding.FragmentGuardianBinding
import com.example.myproj.repository.GuardianRepository
import kotlinx.coroutines.launch
class GuardianFragment(val type : String) : Fragment() {
    companion object {
        fun newInstance(type : String) = GuardianFragment(type)
    }
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
        lifecycle.coroutineScope.launch{
            GuardianRepository.getGuardianData(null)!!.collect(){
                if(it.response == null){
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                }else{
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = RvAdapter(it.response.results)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

}