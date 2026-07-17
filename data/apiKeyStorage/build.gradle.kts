plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {
    androidLibrary {
        namespace = "sikv.lingomate.data.apikeystorage"
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

    val xcfName = "data:apiKeyStorageKit"

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
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidx.security.crypto)
            }
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
