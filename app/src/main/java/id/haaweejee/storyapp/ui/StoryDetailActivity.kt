package id.haaweejee.storyapp.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import id.haaweejee.storyapp.R
import id.haaweejee.storyapp.databinding.ActivityStoryDetailBinding
import id.haaweejee.storyapp.service.data.liststory.StoryResults
import id.haaweejee.storyapp.utils.getBitmap
import id.haaweejee.storyapp.utils.rotateBitmap
import kotlinx.coroutines.launch

class StoryDetailActivity : AppCompatActivity() {

    companion object {
        const val STORY_DETAIL = "story_detail"
    }

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.story_detail)

        val data = intent.getParcelableExtra<StoryResults>(STORY_DETAIL)



        if (data != null){
            binding.tvName.text = data.name
            binding.tvDescriptionValue.text = data.description
            lifecycleScope.launch {
                binding.photoDetails.load(rotateBitmap(getBitmap(data.photoUrl, this@StoryDetailActivity), true))
            }
        }

        binding.btnMaps.setOnClickListener {
            intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(MapsActivity.PHOTO_LOCATION, data)
            startActivity(intent)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}