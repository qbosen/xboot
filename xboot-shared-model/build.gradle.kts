plugins {
    id("java-conventions")
    id("xboot-publishing-conventions")
}

dependencies {
    //utils
    api(libs.google.guava)
    api(libs.apache.commons.lang)
    api(libs.apache.commons.collection)

    testImplementation(libs.bundles.xboot.test)
}
