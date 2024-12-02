package com.example.laba5.presentation.screens

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.laba5.R
import com.example.laba5.databinding.FragmentSignInBinding
import com.example.laba5.models.User

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val TAG = "SignInFragment"

    private var registeredUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated called")

        val args: SignInFragmentArgs by navArgs()
        val user = args.user

        binding.emailEditText.setText(user?.email)
        binding.passwordEditText.setText(user?.password)

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if ((user?.email == user?.email)
            ) {
                val username = registeredUser?.username ?: "Test User"
                findNavController().navigate(R.id.action_signInFragment_to_characterListFragment)
            } else {
                Toast.makeText(requireContext(), "Попадалово, ошиблись паролем или логином", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
