import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.nativeCoroutines)
    alias(libs.plugins.sentry)
}

val localProperties = providers.fileContents(rootProject.layout.projectDirectory.file("local.properties")).asText
    .map { text -> Properties().apply { load(text.reader()) } }

// Takes the DSN from local.properties for local builds and from an environment variable on CI.
// Without either, the DSN is empty and Sentry stays disabled.
fun generateSentryDsn(sourceSet: String, localPropertiesKey: String, environmentVariable: String) =
    tasks.register("generate${sourceSet.replaceFirstChar(Char::uppercase)}SentryDsn") {
        val dsn = localProperties.map { it.getProperty(localPropertiesKey) }
            .orElse(providers.environmentVariable(environmentVariable))
            .orElse("")
        val outputDir = layout.buildDirectory.dir("generated/sentryDsn/$sourceSet")
        inputs.property("dsn", dsn)
        outputs.dir(outputDir)
        doLast {
            outputDir.get().file("sikv/lingomate/SentryDsn.kt").asFile.apply {
                parentFile.mkdirs()
                writeText("package sikv.lingomate\n\ninternal actual val sentryDsn: String = \"${dsn.get()}\"\n")
            }
        }
    }

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
            api(project(":feature:manageApiKeys"))
            api(project(":onDeviceLLM"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(project(":api:remoteConfig"))
            implementation(project(":api:openai"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
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
