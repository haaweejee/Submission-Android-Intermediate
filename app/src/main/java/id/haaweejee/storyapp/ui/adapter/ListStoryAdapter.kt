@file:OptIn(DelicateCoroutinesApi::class)

package id.haaweejee.storyapp.ui.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.haaweejee.storyapp.R
import id.haaweejee.storyapp.databinding.ItemStoryBinding
import id.haaweejee.storyapp.service.data.liststory.StoryResults
import id.haaweejee.storyapp.utils.getBitmap
import id.haaweejee.storyapp.utils.rotateBitmap
import id.haaweejee.storyapp.utils.uriToFile
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListStoryAdapter :
    PagingDataAdapter<StoryResults, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClick(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal fun bind(story: StoryResults) {
            binding.root.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.storyPhoto, "photo"),
                        Pair(binding.tvUsername, "name")
                    )
                onItemClickCallback?.onItemClicked(story, optionsCompat)

            }
            binding.tvUsername.text = story.name
            GlobalScope.launch {
                binding.storyPhoto.load(
                    rotateBitmap(
                        getBitmap(story.photoUrl, itemView.context),
                        true
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: StoryResults, optionsCompat: ActivityOptionsCompat)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResults>() {
            override fun areItemsTheSame(oldItem: StoryResults, newItem: StoryResults): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryResults, newItem: StoryResults): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}