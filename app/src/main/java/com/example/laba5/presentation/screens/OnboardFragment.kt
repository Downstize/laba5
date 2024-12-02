package com.example.laba5.presentation.screens.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.laba5.R
import com.example.laba5.databinding.FragmentOnboardBinding

class OnboardFragment : Fragment() {

    private var _binding: FragmentOnboardBinding? = null
    private val binding get() = _binding!!
    private val TAG = "OnboardFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onCreateView called")

        binding.getStartedButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardFragment_to_signInFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
