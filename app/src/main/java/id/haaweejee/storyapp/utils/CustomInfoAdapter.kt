package id.haaweejee.storyapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import id.haaweejee.storyapp.R
import id.haaweejee.storyapp.service.data.liststory.StoryResults
import java.util.*


class CustomInfoAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getInfoContents(marker: Marker): View {
        val view = (context as AppCompatActivity)
            .layoutInflater
            .inflate(R.layout.custom_info_windows, null)

        val tvName = view.findViewById<TextView>(R.id.tvNameInfo)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)
        val imageView = view.findViewById<ImageView>(R.id.ivImagePhoto)
        val infoWindowData = marker.tag as StoryResults?

        val date = DateFormatter.formatDateAndTimeO(infoWindowData?.createdAt!!, TimeZone.getDefault().id)


        tvName.text = infoWindowData.name
        tvDate.text = date
        Glide.with(context)
            .load(infoWindowData.photoUrl)
            .placeholder(R.drawable.placeholder)
            .listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    e?.printStackTrace()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (dataSource?.equals(DataSource.MEMORY_CACHE) != true) {
                        marker.showInfoWindow()
                    }
                    return false
                }
            })
            .into(imageView)



        return view
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    class MarkerCallback internal constructor(marker: Marker?) :
        RequestListener<Drawable> {

        var marker: Marker? = null

        private fun onSuccess() {
            if (marker != null && marker!!.isInfoWindowShown) {
                marker!!.hideInfoWindow()
                marker!!.showInfoWindow()
            }
        }

        init {
            this.marker = marker
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            Log.e(javaClass.simpleName, "Error loading thumbnail! -> $e")
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onSuccess()
            return false
        }
    }
}