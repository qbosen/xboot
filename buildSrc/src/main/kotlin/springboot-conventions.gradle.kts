
plugins{
    id("java-conventions")
}

dependencies{
    implementation(platform(Versions.SPRING_BOOT_BOOM))
    annotationProcessor(platform(Versions.SPRING_BOOT_BOOM))
    implementation(platform(Versions.SPRING_CLOUD_BOOM))
}