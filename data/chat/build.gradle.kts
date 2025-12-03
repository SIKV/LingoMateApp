plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.nativeCoroutines)
}

kotlin {
    androidLibrary {
        namespace = "sikv.lingomate.data.chat"
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

    val xcfName = "data:chatKit"

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
                implementation(project(":api"))
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
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

    sourceSets.all {
        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    }
}
