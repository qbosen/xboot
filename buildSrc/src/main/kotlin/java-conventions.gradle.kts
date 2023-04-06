plugins {
    id("idea")
    id("java-library")
    id("io.freefair.lombok")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    gradlePluginPortal()
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}