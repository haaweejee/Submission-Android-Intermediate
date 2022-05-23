package id.haaweejee.storyapp.service.data.liststory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryResults(
    val id : String? = null,
    var name: String? = null,
    val description : String? = null,
    var photoUrl : String? = null,
    var createdAt : String? = null,
    val lat : Double? = 0.0,
    val lon : Double? = 0.0
): Parcelable
