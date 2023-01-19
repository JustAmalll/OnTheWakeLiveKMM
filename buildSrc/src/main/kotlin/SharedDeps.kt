object SharedDeps {

    // Multiplatform Settings
    // This is a Kotlin library for Multiplatform apps, so that common code can persist key-value data.
    const val kmmSettings = "com.russhwolf:multiplatform-settings-no-arg:1.0.0-RC"
    const val kmmSettingsCoroutines = "com.russhwolf:multiplatform-settings-coroutines:1.0.0-RC"

    // KOTLIN DATE TIME
    private const val dateTimeVersion = "0.4.0"
    const val kotlinDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeVersion"

    // Serialization
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"

    // KTOR
    private const val ktorVersion = "2.2.1"
    const val ktorCore = "io.ktor:ktor-client-core:$ktorVersion"
    const val ktorWebSockets = "io.ktor:ktor-client-websockets:$ktorVersion"
    const val ktorLogging = "io.ktor:ktor-client-logging:$ktorVersion"
    const val ktorSerialization = "io.ktor:ktor-client-content-negotiation:$ktorVersion"
    const val ktorSerializationJson = "io.ktor:ktor-serialization-kotlinx-json:$ktorVersion"

    // SQLDELIGHT
    private const val sqlDelightVersion = "1.5.4"
    const val sqlDelightRuntime = "com.squareup.sqldelight:runtime:$sqlDelightVersion"
    const val sqlDelightCoroutinesExtensions = "com.squareup.sqldelight:coroutines-extensions:$sqlDelightVersion"

    // GRADLE PLUGINS
    private const val sqlDelightGradleVersion = "1.5.3"
    const val sqlDelightGradlePlugin = "com.squareup.sqldelight:gradle-plugin:$sqlDelightGradleVersion"
}