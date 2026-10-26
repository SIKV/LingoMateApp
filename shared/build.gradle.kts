import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.nativeCoroutines)
    alias(libs.plugins.sentry)
}

val secretsProperties: Provider<Properties> = providers.fileContents(rootProject.layout.projectDirectory.file("secrets.properties")).asText
    .map { text -> Properties().apply { load(text.reader()) } }

fun generateBuildValue(
    name: String,
    declaration: String,
    sourceSet: String,
    propertiesKey: String,
): TaskProvider<Task> =
    tasks.register("generate${sourceSet.replaceFirstChar(Char::uppercase)}$name") {
        val value: Provider<String> = secretsProperties.map { it.getProperty(propertiesKey) }
            .orElse("")
        val outputDir: Provider<Directory> = layout.buildDirectory.dir("generated/$name/$sourceSet")
        inputs.property("value", value)
        outputs.dir(outputDir)
        doLast {
            outputDir.get().file("sikv/lingomate/$name.kt").asFile.apply {
                parentFile.mkdirs()
                writeText("package sikv.lingomate\n\n$declaration = \"${value.get()}\"\n")
            }
        }
    }

fun generateSentryDsn(sourceSet: String, propertiesKey: String): TaskProvider<Task> =
    generateBuildValue("SentryDsn", "internal actual val sentryDsn: String", sourceSet, propertiesKey)

// One PostHog project serves both apps, so its values go to commonMain.
fun generatePostHogValue(name: String, propertiesKey: String): TaskProvider<Task> =
    generateBuildValue("PostHog$name", "val postHog$name: String", "commonMain", propertiesKey)

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true

            export(project(":onDeviceLLM"))
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            api(libs.koin.core)
            api(libs.koin.compose)
            api(libs.koin.compose.viewmodel)
            api(project(":data:chat"))
            api(project(":data:config"))
            api(project(":data:apiKeyStorage"))
            api(project(":data:keyValueStorage"))
            api(project(":feature:startChat"))
            api(project(":feature:chat"))
            api(project(":feature:chatRecap"))
            api(project(":feature:manageApiKeys"))
            api(project(":feature:feedback"))
            api(project(":onDeviceLLM"))
            api(project(":analytics"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(project(":api:remoteConfig"))
            implementation(project(":api:openai"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        commonMain {
            kotlin.srcDir(generatePostHogValue("ProjectToken", "posthogProjectToken"))
            kotlin.srcDir(generatePostHogValue("Host", "posthogHost"))
        }
        androidMain {
            kotlin.srcDir(generateSentryDsn("androidMain", "sentryDSNAndroid"))
        }
        iosMain {
            kotlin.srcDir(generateSentryDsn("iosMain", "sentryDSNIOS"))
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    }
}

android {
    namespace = "sikv.lingomate.shared"
    compileSdk = Configs.ANDROID_COMPILE_SDK
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = Configs.ANDROID_MIN_SDK
    }
}
