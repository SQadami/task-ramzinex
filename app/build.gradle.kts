@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

val packageName = "ir.salman.ramzinex"
val appVersionCode = propOrDef("APP_VERSION_CODE", "1").toInt()
println("APK version code: $appVersionCode")
val appVersionName = propOrDef("APP_VERSION_NAME", "1.0.0")
println("APK version name: $appVersionName")

android {
    namespace = packageName

    defaultConfig {
        applicationId = packageName
        versionCode = appVersionCode
        versionName = appVersionName
    }

    lint {
        baseline = file("lint-baseline.xml")
        // Disable lintVital. Not needed since lint is run on CI
        checkReleaseBuilds = false
        // Ignore any tests
        ignoreTestSources = true
        // Make the build fail on any lint errors
        abortOnError = true
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }

    buildTypes {
        debug {
            versionNameSuffix = "-dev"
            applicationIdSuffix = ".dev"
        }

        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")
        }
    }
}

dependencies {

    implementation(projects.binding)

    implementation(libs.kotlin)

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.lifecycle.viewmodelKtx)

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.fragmentKtx)

    implementation(libs.okhttp.okhttp)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.retrofit.retrofit)
    implementation(libs.retrofit.moshiConverter)
    implementation(libs.moshi)

    debugImplementation(libs.leakCanary)
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> propOrDef(propertyName: String, defaultValue: T): T {
    val propertyValue = project.properties[propertyName] as T?
    return propertyValue ?: defaultValue
}
