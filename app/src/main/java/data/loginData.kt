package data

data class loginData (
    val userId : String = "",
    val password : String = ""
)

val userData = mapOf("SS" to "11111")

data class loginResult(
    val success : Boolean = true,
    val error : String? = null
)
{
    constructor() : this(true,null)

    constructor(error: String) : this(false,error)
}
