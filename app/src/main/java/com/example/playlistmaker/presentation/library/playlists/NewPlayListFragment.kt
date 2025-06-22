package com.example.playlistmaker.presentation.library.playlists


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentEditPlaylistBinding
import com.example.playlistmaker.util.Utils.showStyledSnackbar
import com.example.playlistmaker.util.debounce
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

open class NewPlayListFragment : Fragment() {

    private var _binding: FragmentEditPlaylistBinding? = null
    val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistsViewModel>()
    var name: String = ""
    var description: String = ""
    var path: String = ""
    private lateinit var debouncedClick: (Unit) -> Unit
    var playlistId: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val pickImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.addPhotoIcon.isVisible = false
                    binding.uploadContainer.background = null
                    binding.cover.setImageURI(uri)
                    binding.cover.isVisible = true
                    path = viewModel.saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.uploadContainer.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.backButton.setOnClickListener {
            confirmExit()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                confirmExit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        val simpleEditNameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.createButton.isEnabled = !s.isNullOrEmpty()
                name = s.toString().trim()
            }

            override fun afterTextChanged(s: Editable?) {

            }


        }

        val simpleEditDescriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                description = s.toString().trim()
            }

            override fun afterTextChanged(s: Editable?) {

            }


        }

        binding.editName.addTextChangedListener(simpleEditNameTextWatcher)
        binding.editDescription.addTextChangedListener(simpleEditDescriptionTextWatcher)

        debouncedClick = debounce(
            delayMillis = CLICK_DEBOUNCE_DELAY,
            coroutineScope = lifecycleScope,
            useLastParam = true,
            action = {
                val description = binding.editDescription.text.toString()
                viewModel.createPlaylist(name, description, path)

                binding.root.showStyledSnackbar(
                    requireContext(),
                    getString(R.string.playlis_created, name),
                    R.font.ys_display_regular,
                    R.color.main,
                    R.color.main_inverse
                )

                findNavController().navigateUp()

            }
        )

        binding.createButton.setOnClickListener {

            if (name.isNotEmpty()) {
                debouncedClick(Unit)
            }
        }
    }


    private fun confirmExit() {
        if (playlistId != null) {
            findNavController().navigateUp()

        } else {
            if (name.isNotBlank() || description.isNotBlank() ||
                path.isNotBlank()
            ) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.confirm_exit_title)
                    .setMessage(R.string.confirm_exit_message)
                    .setNeutralButton(R.string.cancel) { dialog, witch ->
                    }.setNegativeButton(R.string.finish) { dialog, witch ->
                        findNavController().navigateUp()
                    }.show()
            } else {
                findNavController().navigateUp()
            }
        }
    }


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }


}