package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.player.ui.PlayerActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var tracks: List<Track>
    private lateinit var historyList: List<Track>
    private var isClickAllowed = true
    private lateinit var binding: ActivitySearchBinding
    private var enteredText: String = ""
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var viewModel: SearchViewModel
    private val adapter: TrackAdapter by lazy {
        TrackAdapter(tracks) { track ->

            if (clickDebounce()) {
                viewModel.addTrackToHistory(
                    track
                )
                val displayIntent = Intent(this, PlayerActivity::class.java)
                displayIntent.putExtra(TRACK, track)
                startActivity(displayIntent)
            }
        }
    }
    private val historyAdapter: HistoryAdapter by lazy {
        HistoryAdapter(historyList) { track ->
            if (clickDebounce()) {
                val displayIntent = Intent(this, PlayerActivity::class.java)
                displayIntent.putExtra(TRACK, track)
                startActivity(displayIntent)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        viewModel.observeState().observe(this) {
            render(it)
        }

        binding = ActivitySearchBinding.inflate(layoutInflater)


        setContentView(binding.root)

        tracks = viewModel.getTracks()
        historyList = viewModel.getHistoryList()


        binding.searchArea.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.doSearch(binding.searchArea.text.toString())
            }

            true

        }
        binding.placeholderButtonRenew.setOnClickListener {
            viewModel.doSearch(binding.searchArea.text.toString())
        }


        if (savedInstanceState != null) {
            enteredText = savedInstanceState.getString(ENTERED_TEXT, "")
            binding.searchArea.setText(enteredText)
        }

        binding.clearButton.setOnClickListener {
            binding.searchArea.setText("")
            viewModel.clearTracks()
            adapter.notifyDataSetChanged()
            showHistory()
        }

        binding.backButton.setOnClickListener {
            finish()
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
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvTracksHistory.adapter = historyAdapter
        binding.rvTracksHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.clearHistoryButton.setOnClickListener() {
            viewModel.clearHistoryList()
            binding.llSearchHistory.isVisible = false
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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


    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ENTERED_TEXT, enteredText)
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun showLoading() {
        hideKeyboard()
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
        adapter.notifyDataSetChanged()
        binding.placeholderMessage.isVisible = true
        binding.placeholderImage.setImageDrawable(
            ContextCompat.getDrawable(
                this@SearchActivity,
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
                this@SearchActivity,
                R.drawable.no_internet
            )
        )
        binding.placeholderText.text = getString(R.string.no_internet)
        binding.placeholderButtonRenew.isVisible = true
    }


    private fun showContent() {
        adapter.notifyDataSetChanged()
        binding.progressBar.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.placeholderButtonRenew.isVisible = false
        binding.rvTracks.isVisible = true
    }

    private fun showHistory() {
        hideKeyboard()
        binding.progressBar.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.placeholderButtonRenew.isVisible = false
        historyList = viewModel.getHistoryList()
        historyAdapter.updateData(historyList)
        binding.llSearchHistory.isVisible = historyList.isNotEmpty()
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.Content -> showContent()
            is SearchScreenState.History -> showHistory()
            is SearchScreenState.NotFound -> showNotFound()
            is SearchScreenState.NoInternet -> showNoInternet()
        }
    }

    companion object {
        private const val ENTERED_TEXT = "ENTERED_TEXT"
        private const val TRACK = "TRACK"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }


}