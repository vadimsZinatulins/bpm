package data

data class Group(
    val name: String,
    val groups: List<Group>,
    val credentials: List<Credential>
)
