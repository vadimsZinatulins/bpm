package utils

object ServiceLocator {
    private val services = mutableMapOf<String, Any>()
    fun <T : Any> registerService(service: T, key: String) {
        services[key] = service
    }

    fun <T> getService(key: String): T {
        @Suppress("UNCHECKED_CAST")
        return services[key] as T
    }
}