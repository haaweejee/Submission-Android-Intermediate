package id.haaweejee.storyapp.service.data.liststory

data class StoryResponse(
    val error: Boolean? = null,
    val message: String = "",
    val listStory: List<StoryResults>
)
