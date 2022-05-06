plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.3")
    implementation(kotlin("gradle-plugin","1.6.10"))
}