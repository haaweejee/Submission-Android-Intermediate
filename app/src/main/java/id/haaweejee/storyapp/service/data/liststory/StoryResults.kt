package id.haaweejee.storyapp.service.data.liststory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryResults(
    val id : String? = null,
    val name: String? = null,
    val description : String? = null,
    val photoUrl : String? = null,
    val createdAt : String? = null,
    val lat : Double? = 0.0,
    val lon : Double? = 0.0
): Parcelable
