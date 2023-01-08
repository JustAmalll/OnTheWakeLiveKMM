object SharedDeps {

    // Multiplatform Settings
    // This is a Kotlin library for Multiplatform apps, so that common code can persist key-value data.
    const val kmmSettings = "com.russhwolf:multiplatform-settings-no-arg:1.0.0-RC"
    const val kmmSettingsCoroutines = "com.russhwolf:multiplatform-settings-coroutines:1.0.0-RC"

    // KOTLIN DATE TIME
    private const val dateTimeVersion = "0.4.0"
    const val kotlinDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeVersion"

    // KTOR
    private const val ktorVersion = "2.2.1"
    const val ktorCore = "io.ktor:ktor-client-core:$ktorVersion"
    const val ktorWebSockets = "io.ktor:ktor-client-websockets:$ktorVersion"
    const val ktorLogging = "io.ktor:ktor-client-logging:$ktorVersion"
    const val ktorSerialization = "io.ktor:ktor-client-content-negotiation:$ktorVersion"
    const val ktorSerializationJson = "io.ktor:ktor-serialization-kotlinx-json:$ktorVersion"
}