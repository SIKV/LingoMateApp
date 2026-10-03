plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidLibrary {
        namespace = "sikv.lingomate.data.config"
        compileSdk = Configs.ANDROID_COMPILE_SDK
        minSdk = Configs.ANDROID_MIN_SDK

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    val xcfName = "data:configKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }
    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }
    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(project(":api:remoteConfig"))
                implementation(project(":data:keyValueStorage"))
                implementation(project(":logger"))
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.mock)
            }
        }
        androidMain {
            dependencies { }
        }
        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }
        iosMain {
            dependencies { }
        }
    }
}
