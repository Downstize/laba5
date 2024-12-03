package com.example.laba5.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.laba5.R
import com.example.laba5.utils.FileUtils
import com.example.laba5.data.DataStoreManager
import com.example.laba5.data.SharedPrefsManager
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var sharedPreferencesManager: SharedPrefsManager
    private lateinit var charactersJson: String

    private var currentLanguage = "en"
    private var currentFontSize = 18f
    private val fileName = "characters_24.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            charactersJson = SettingsFragmentArgs.fromBundle(it).charactersJson
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataStoreManager = DataStoreManager(requireContext())
        sharedPreferencesManager = SharedPrefsManager(requireContext())

        val currentLanguageText = view.findViewById<TextView>(R.id.current_language_text)
        val currentFontSizeText = view.findViewById<TextView>(R.id.current_font_size_text)
        val switchDarkMode = view.findViewById<SwitchMaterial>(R.id.switch_dark_mode)
        val currentThemeText = view.findViewById<TextView>(R.id.current_theme_text)

        lifecycleScope.launch {
            currentLanguage = sharedPreferencesManager.getLanguage()
            currentFontSize = sharedPreferencesManager.getFontSize()

            currentLanguageText.text = "Language: $currentLanguage"
            currentFontSizeText.text = "Font Size: $currentFontSize"

            val isDarkMode = dataStoreManager.theme.first()
            switchDarkMode.isChecked = isDarkMode
            currentThemeText.text = if (isDarkMode) "Theme: Dark" else "Theme: Light"
        }

        view.findViewById<View>(R.id.change_language_button).setOnClickListener {
            currentLanguage = if (currentLanguage == "en") "ru" else "en"
            currentLanguageText.text = "Language: $currentLanguage"
        }

        view.findViewById<View>(R.id.save_language_button).setOnClickListener {
            sharedPreferencesManager.saveLanguage(currentLanguage)
        }

        view.findViewById<View>(R.id.change_font_size_button).setOnClickListener {
            currentFontSize = if (currentFontSize == 18f) 24f else 18f
            currentFontSizeText.text = "Font Size: $currentFontSize"
        }

        view.findViewById<View>(R.id.save_font_size_button).setOnClickListener {
            sharedPreferencesManager.saveFontSize(currentFontSize)
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                dataStoreManager.saveTheme(isChecked)
                currentThemeText.text = if (isChecked) "Theme: Dark" else "Theme: Light"
            }
        }

        val fileStatusText = view.findViewById<TextView>(R.id.file_status_text)

        fileStatusText.text = if (FileUtils.isFileExists(requireContext(), fileName)) {
            "File exists"
        } else {
            "File does not exist"
        }

        view.findViewById<View>(R.id.save_file_button).setOnClickListener {
            if (FileUtils.saveFileToExternalStorage(requireContext(), charactersJson, fileName)) {
                fileStatusText.text = "File saved to Documents"
            } else {
                fileStatusText.text = "Error saving file"
            }
        }

        view.findViewById<View>(R.id.delete_file_button).setOnClickListener {
            if (FileUtils.deleteFileFromExternalStorage(requireContext(), fileName)) {
                fileStatusText.text = "File deleted"
            } else {
                fileStatusText.text = "Error deleting file"
            }
        }

        view.findViewById<View>(R.id.backup_file_button).setOnClickListener {
            if (FileUtils.backupFile(requireContext(), fileName)) {
                fileStatusText.text = "Backup created successfully"
            } else {
                fileStatusText.text = "Backup creation failed"
            }
        }

        view.findViewById<View>(R.id.restore_file_button).setOnClickListener {
            if (FileUtils.restoreFileFromBackup(requireContext(), fileName)) {
                fileStatusText.text = "File restored successfully"
            } else {
                fileStatusText.text = "File restore failed"
            }
        }
    }
}