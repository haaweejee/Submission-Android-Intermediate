package id.haaweejee.storyapp.service.data.login

data class LoginResponse (
    val error: Boolean? = null,
    val message: String = "",
    val loginResult: LoginResult? = null
)