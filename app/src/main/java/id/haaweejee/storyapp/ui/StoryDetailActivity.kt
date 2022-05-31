package id.haaweejee.storyapp.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import id.haaweejee.storyapp.R
import id.haaweejee.storyapp.databinding.ActivityStoryDetailBinding
import id.haaweejee.storyapp.service.data.liststory.StoryEntity
import id.haaweejee.storyapp.utils.getBitmap
import id.haaweejee.storyapp.utils.rotateBitmap
import kotlinx.coroutines.launch

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.story_detail)

        val data = intent.getParcelableExtra<StoryEntity>(STORY_DETAIL)


        if (data != null){
            binding.tvName.text = data.name
            binding.tvDescriptionValue.text = data.description
            Glide.with(this)
                .asBitmap()
                .load(data.photoUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        binding.photoDetails.setImageBitmap(rotateBitmap(resource, true))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        binding.btnMaps.setOnClickListener {
            intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(MapsActivity.DATA, data)
            startActivity(intent)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }


    companion object {
        const val STORY_DETAIL = "story_detail"
    }

}