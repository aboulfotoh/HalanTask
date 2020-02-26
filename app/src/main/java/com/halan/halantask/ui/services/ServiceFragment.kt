package com.halan.halantask.ui.services


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.halan.halantask.data.model.Service
import com.halan.halantask.databinding.FragmentServicesBinding

/**
 * A simple [Fragment] subclass.
 */
class ServiceFragment : Fragment() {

    private lateinit var binding: FragmentServicesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServicesBinding.inflate(inflater)
        binding.service = arguments?.get("service") as Service
        // Inflate the layout for this fragment
        return binding.root
    }


}
