package com.example.playlistmaker.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.net.toUri

class SettingsFragment : Fragment() {
    private val viewModel by viewModel<SettingsViewModel>()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.themeSwitcher.isChecked = viewModel.isNightMode

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)

        }

        binding.share.setOnClickListener {
            shareApp()
        }

        binding.support.setOnClickListener {
            sendToSupport()
        }

        binding.userAgreement.setOnClickListener {
            openUserAgreement()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun shareApp() {

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message))
            type = "text/plain"
        }
        val intentMessage = getString(R.string.share_intent_message)

        val shareIntent = Intent.createChooser(sendIntent, intentMessage)

        startActivity(shareIntent)
    }

    private fun sendToSupport() {

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_message_to_support))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.message_to_support))
        }
        startActivity(sendIntent)
    }

    private fun openUserAgreement() {

        val intent = Intent(Intent.ACTION_VIEW, getString(R.string.user_agreement_url).toUri())
        startActivity(intent)
    }
}