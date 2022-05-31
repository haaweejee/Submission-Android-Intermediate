package id.haaweejee.storyapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.haaweejee.storyapp.databinding.FragmentHomeBinding
import id.haaweejee.storyapp.service.data.liststory.StoryEntity
import id.haaweejee.storyapp.service.preferences.SettingsPreference
import id.haaweejee.storyapp.ui.adapter.ListStoryAdapter
import id.haaweejee.storyapp.ui.adapter.LoadingStateAdapter
import id.haaweejee.storyapp.utils.PreferenceViewModelFactory
import id.haaweejee.storyapp.viewmodel.ListStoryViewModel
import id.haaweejee.storyapp.viewmodel.PreferencesViewModel
import id.haaweejee.storyapp.viewmodel.ViewModelFactory


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val storyViewModel: ListStoryViewModel by viewModels {
        ViewModelFactory(requireContext())
    }
    private lateinit var prefViewModel: PreferencesViewModel
    private lateinit var adapter: ListStoryAdapter

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingsPreference.getInstance(requireContext().dataStore)
        prefViewModel =
            ViewModelProvider(
                this,
                PreferenceViewModelFactory(pref)
            )[PreferencesViewModel::class.java]
        prefViewModel.getBearerToken().observe(viewLifecycleOwner) {
            val bearer = "Bearer $it"
            Log.d("MainActivity", bearer)
            showLoading(true)
            storyViewModel.getListStory(bearer).observe(viewLifecycleOwner) { data ->
                showLoading(false)
                adapter.submitData(lifecycle, data)
            }
        }

        adapter = ListStoryAdapter()
        adapter.setOnItemClick(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StoryEntity, optionsCompat: ActivityOptionsCompat) {
                val intent = Intent(context, StoryDetailActivity::class.java)
                intent.putExtra(StoryDetailActivity.STORY_DETAIL, data)
                startActivity(intent, optionsCompat.toBundle())
            }
        })

        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(context)
            rvStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            rvStory.setHasFixedSize(true)

        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }
}