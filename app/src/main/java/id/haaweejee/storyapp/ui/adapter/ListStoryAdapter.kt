package id.haaweejee.storyapp.ui.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.haaweejee.storyapp.databinding.ItemStoryBinding
import id.haaweejee.storyapp.service.data.liststory.StoryResults
import id.haaweejee.storyapp.utils.rotateBitmap

class ListStoryAdapter : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {

    private val listStory = ArrayList<StoryResults>()


    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClick(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(story: List<StoryResults>){
        listStory.clear()
        listStory.addAll(story)
        notifyDataSetChanged()
        Log.d("TAG", "cek item masuk $listStory")
    }

    inner class ListViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root){
        internal fun bind(story: StoryResults){
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
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .apply(RequestOptions().override(200,200))
                .into(binding.storyPhoto)
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
        val user = listStory[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = listStory.size

    interface OnItemClickCallback{
        fun onItemClicked(data: StoryResults, optionsCompat: ActivityOptionsCompat)
    }
}