package com.example.playlistmaker.presentation.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.entity.SearchState
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var enteredText: String = ""
    private var historyList: List<Track> = emptyList()
    private val viewModel by viewModel<SearchViewModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private var adapter: SearchAdapter? = null
    private var historyAdapter: HistoryAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupObservers()

        viewModel.loadHistoryList()


        onTrackClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
                viewModel.putTrackForPlayer(track)
                findNavController().navigate(R.id.action_searchFragment_to_playerFragment3)
            }

        adapter = SearchAdapter(emptyList()) { track ->
            onTrackClickDebounce(track)
            lifecycleScope.launch {
                viewModel.addTrackToHistory(track)
            }
        }
        historyAdapter = HistoryAdapter(emptyList()) { track ->
            onTrackClickDebounce(track)
        }

        binding.searchArea.setOnEditorActionListener { _, actionId, _ ->
            lifecycleScope.launch {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.doSearch(binding.searchArea.text.toString())
                }
            }
            true

        }
        binding.placeholderButtonRenew.setOnClickListener {
            lifecycleScope.launch {
                viewModel.doSearch(binding.searchArea.text.toString())
            }
        }

        if (savedInstanceState != null) {
            enteredText = savedInstanceState.getString(ENTERED_TEXT, "")
            binding.searchArea.setText(enteredText)
        }

        binding.clearButton.setOnClickListener {
            binding.searchArea.setText("")
            viewModel.clearTracks()
            viewModel.loadHistoryList()
            showHistory()
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.visibility = clearButtonVisibility(s)
                placeholdersAndTracksListVisibility(s)
                enteredText = s.toString()
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {

            }


        }
        binding.searchArea.addTextChangedListener(simpleTextWatcher)

        binding.searchArea.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) showHistory()
        }

        binding.rvTracks.adapter = adapter
        binding.rvTracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvTracksHistory.adapter = historyAdapter
        binding.rvTracksHistory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistoryList()
            binding.llSearchHistory.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun placeholdersAndTracksListVisibility(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            binding.rvTracks.isVisible = false
            showHistory()
        }
    }


    private fun hideKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ENTERED_TEXT, enteredText)
    }

    private fun showLoading() {
        hideKeyboard(binding.root)
        binding.llSearchHistory.isVisible = false
        binding.rvTracks.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.placeholderButtonRenew.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun showNotFound() {
        binding.progressBar.isVisible = false
        binding.llSearchHistory.isVisible = false
        binding.rvTracks.isVisible = false
        adapter?.notifyDataSetChanged()
        binding.placeholderMessage.isVisible = true
        binding.placeholderImage.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.not_found
            )
        )
        binding.placeholderText.text = getString(R.string.nothing_found)
    }

    private fun showNoInternet() {
        binding.progressBar.isVisible = false
        binding.llSearchHistory.isVisible = false
        binding.rvTracks.isVisible = false
        binding.placeholderMessage.isVisible = true
        binding.placeholderImage.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.no_internet
            )
        )
        binding.placeholderText.text = getString(R.string.no_internet)
        binding.placeholderButtonRenew.isVisible = true
    }


    private fun showContent() {
        adapter?.notifyDataSetChanged()
        binding.progressBar.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.placeholderButtonRenew.isVisible = false
        binding.rvTracks.isVisible = true
    }

    private fun showHistory() {
        hideKeyboard(binding.root)
        binding.progressBar.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.placeholderButtonRenew.isVisible = false
        binding.llSearchHistory.isVisible = historyList.isNotEmpty()
    }

    private fun render(state: SearchState) {
        when (state) {
            SearchState.LOADING -> showLoading()
            SearchState.CONTENT -> showContent()
            SearchState.HISTORY -> showHistory()
            SearchState.NOTFOUND -> showNotFound()
            SearchState.NOINTERNET -> showNoInternet()
            SearchState.DEFAULT -> {}
        }
    }

    private fun setupObservers() {
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state.searchState)
            adapter?.updateData(state.tracks)
            historyAdapter?.updateData(state.history)
            historyList = state.history

        }

    }

    companion object {
        private const val ENTERED_TEXT = "ENTERED_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }


}