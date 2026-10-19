import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.nativeCoroutines)
    alias(libs.plugins.sentry)
}

val localProperties: Provider<Properties> = providers.fileContents(rootProject.layout.projectDirectory.file("local.properties")).asText
    .map { text -> Properties().apply { load(text.reader()) } }

// Takes a value from local.properties for local builds and from an environment variable on CI.
// Without either, the value is empty and the SDK that reads it stays disabled.
fun generateBuildValue(
    name: String,
    declaration: String,
    sourceSet: String,
    localPropertiesKey: String,
    environmentVariable: String,
): TaskProvider<Task> =
    tasks.register("generate${sourceSet.replaceFirstChar(Char::uppercase)}$name") {
        val value: Provider<String> = localProperties.map { it.getProperty(localPropertiesKey) }
            .orElse(providers.environmentVariable(environmentVariable))
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

fun generateSentryDsn(sourceSet: String, localPropertiesKey: String, environmentVariable: String): TaskProvider<Task> =
    generateBuildValue("SentryDsn", "internal actual val sentryDsn: String", sourceSet, localPropertiesKey, environmentVariable)

// One PostHog project serves both apps, so its values go to commonMain.
fun generatePostHogValue(name: String, localPropertiesKey: String, environmentVariable: String): TaskProvider<Task> =
    generateBuildValue("PostHog$name", "val postHog$name: String", "commonMain", localPropertiesKey, environmentVariable)

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
            kotlin.srcDir(generatePostHogValue("ProjectToken", "posthog.projectToken", "POSTHOG_PROJECT_TOKEN"))
            kotlin.srcDir(generatePostHogValue("Host", "posthog.host", "POSTHOG_HOST"))
        }
        androidMain {
            kotlin.srcDir(generateSentryDsn("androidMain", "sentry.dsn.android", "SENTRY_DSN_ANDROID"))
        }
        iosMain {
            kotlin.srcDir(generateSentryDsn("iosMain", "sentry.dsn.ios", "SENTRY_DSN_IOS"))
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
