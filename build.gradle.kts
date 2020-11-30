plugins {
    kotlin("multiplatform") version "1.4.20"
    id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.2"
}
group = "me.sebarthel"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    js {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
    }
    iosArm64 { binaries.framework("MyFramework") }
    iosX64 { binaries.framework("MyFramework") }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")

    
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val nativeMain by creating {
            dependencies { }
        }
        val nativeTest by creating {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        getByName("iosArm64Main") { dependsOn(nativeMain) }
        getByName("iosArm64Test") { dependsOn(nativeTest) }
        getByName("iosX64Main") { dependsOn(nativeMain) }
        getByName("iosX64Test") { dependsOn(nativeTest) }

    }

    tasks {
        register("universalFrameworkDebug", org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask::class) {
            baseName = "MyFramework"
            from(
                iosArm64().binaries.getFramework("MyFramework", "Debug"),
                iosX64().binaries.getFramework("MyFramework", "Debug")
            )
            destinationDir = buildDir.resolve("bin/universal/debug")
            group = "Universal framework"
            description = "Builds a universal (fat) debug framework"
            dependsOn("linkMyFrameworkDebugFrameworkIosArm64")
            dependsOn("linkMyFrameworkDebugFrameworkIosX64")
        }

        register("universalFrameworkRelease", org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask::class) {
            baseName = "MyFramework"
            from(
                iosArm64().binaries.getFramework("MyFramework", "Release"),
                iosX64().binaries.getFramework("MyFramework", "Release")
            )
            destinationDir = buildDir.resolve("bin/universal/release")
            group = "Universal framework"
            description = "Builds a universal (fat) release framework"
            dependsOn("linkMyFrameworkReleaseFrameworkIosArm64")
            dependsOn("linkMyFrameworkReleaseFrameworkIosX64")
        }

        val buildUniversalFramework by register("universalFramework") {
            dependsOn("universalFrameworkDebug")
            dependsOn("universalFrameworkRelease")
        }

        build {
            dependsOn(buildUniversalFramework)
        }

    }
}

multiplatformSwiftPackage {
    swiftToolsVersion("5.3")
    targetPlatforms {
        iOS { v("13") }
    }
}
