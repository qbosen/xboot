plugins {
    id("java-conventions")
    id("xboot-publishing-conventions")
}


dependencies {
    implementation(libs.slf4j.api)
    testImplementation(libs.slf4j.simple)
    testImplementation(libs.bundles.junit.jupiter)
}