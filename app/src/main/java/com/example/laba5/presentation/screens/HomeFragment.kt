package com.example.laba5.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laba5.databinding.FragmentHomeBinding
import com.example.laba5.models.Chat
import com.example.laba5.presentation.adapter.ChatAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView called")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatList = generateMockChatData()

        setupRecyclerView(chatList)
    }

    // Метод для инициализации RecyclerView
    private fun setupRecyclerView(chatList: List<Chat>) {
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ChatAdapter(chatList)
        }
    }

    private fun generateMockChatData(): List<Chat> {
        val senders = listOf("Евгений", "Дмитрий", "Вячеслав", "Александр", "Никита")
        val messages = listOf(
            "Привет, как дела?",
            "Не забудь про встречу",
            "Давай встретимся!",
            "Увидимся скоро!",
            "Ты придёшь на мероприятие?",
            "Позвони мне, когда освободишься",
            "Ты закончил задачу?",
            "Я сегодня опоздаю",
            "С днём рождения!",
            "Поздравляю с повышением!",
            "Давай встретимся на кофе",
            "Мы всё ещё идём на ужин?",
            "Какие новости?",
            "Ты можешь отправить мне файлы?",
            "Я опаздываю, извини!",
            "Давай устроим видеозвонок",
            "Проверь свою почту",
            "Мне нужен твой совет",
            "Как прошли выходные?",
            "Давно не виделись!"
        )

        val times = listOf(
            "10:30", "09:45", "08:15", "14:00", "16:30",
            "11:20", "13:45", "12:30", "15:50", "17:10"
        )

        return List(20) {
            val sender = senders.random()
            val message = messages.random()
            val time = times.random()
            Chat(sender, message, time)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "onDestroyView called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }
}
