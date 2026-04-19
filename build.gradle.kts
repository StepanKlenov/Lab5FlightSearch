
plugins {
    // Объявляем версию AGP (Android Gradle Plugin)
    id("com.android.application") version "8.7.0" apply false

    // Kotlin и Compose (версии должны совпадать)
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false

    // KSP для Room
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}