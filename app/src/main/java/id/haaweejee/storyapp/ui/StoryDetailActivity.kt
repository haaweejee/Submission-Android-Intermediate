package id.haaweejee.storyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.haaweejee.storyapp.R
import id.haaweejee.storyapp.databinding.ActivityStoryDetailBinding
import id.haaweejee.storyapp.service.data.liststory.StoryResults

class StoryDetailActivity : AppCompatActivity() {

    companion object{
        const val STORY_DETAIL = "story_detail"
    }

    private lateinit var binding : ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.story_detail)

        val data = intent.getParcelableExtra<StoryResults>(STORY_DETAIL)

        binding.tvName.text = data?.name
        binding.tvDescriptionValue.text = data?.description
        Glide.with(this)
            .load(data?.photoUrl)
            .apply(RequestOptions().override(200,200))
            .into(binding.photoDetails)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}