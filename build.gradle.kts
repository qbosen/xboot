//
//allprojects {
//    apply plugin: "idea"
//}
//
//
//static String loadMavenUrl(Project pj, String prop) {
//    def url = pj.findProperty(prop) as String
//    return url == null || url.trim().length() == 0 ? null : url.trim()
//}

//subprojects {
//    apply plugin: "java-library"
//    apply plugin: "signing"
//    repositories {
//        def localMavenPublicRU = loadMavenUrl(project, "MavenPublicRU")
//
//        if (localMavenPublicRU != null) {
//            maven {
//                url localMavenPublicRU
//                allowInsecureProtocol true
//            }
//        }
//        maven { url "https://maven.aliyun.com/repository/public" }
//        mavenCentral()
//    }
//    configurations {
//        compileOnly {
//            extendsFrom annotationProcessor
//        }
//    }
//    sourceCompatibility = 1.8
//    targetCompatibility = 1.8
//
//    [compileJava, compileTestJava]*.options*.encoding = "UTF-8"
//
//    configurations.all {
//        exclude group: "junit", module: "junit"
//        resolutionStrategy.cacheChangingModulesFor 0, "seconds"
//    }
//    afterEvaluate {
//        def localMavenReleaseRU = loadMavenUrl(project, "localMavenReleaseRU")
//        def localMavenSnapshotRU = loadMavenUrl(project, "localMavenSnapshotRU")
//
//        if (plugins.hasPlugin("maven-publish") && localMavenReleaseRU != null && localMavenSnapshotRU != null) {
//            publishing {
//                publications {
//                    mavenJava(MavenPublication) {
//                        groupId = rootProject.group
//                        version = rootProject.version
//                        artifactId = project.name
//
//                        from components.java
//
//                        versionMapping {
//                            usage("java-api") {
//                                fromResolutionOf("runtimeClasspath")
//                            }
//                            usage("java-runtime") {
//                                fromResolutionResult()
//                            }
//                        }
//                        pom {
//                            name = "xboot"
//                            packaging "jar"
//                            description = "A scaffold to help you develop DDD java projects quickly."
//                            url = "https://github.com/qbosen/xboot"
//
//                            scm {
//                                connection = "scm:git:https://github.com:qbosen/xboot.git"
//                                developerConnection = "scm:git:https://github.com:qbosen/xboot.git"
//                                url = "https://github.com/qbosen/xboot.git"
//                            }
//
//                            licenses {
//                                license {
//                                    name = "The Apache License, Version 2.0"
//                                    url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
//                                }
//                            }
//
//                            developers {
//                                developer {
//                                    id = "qbosen"
//                                    name = "Baisen Qiu"
//                                    email = "abosen@qq.com"
//                                }
//                            }
//                        }
//                    }
//                }
//
//                repositories {
//                    maven {
//                        name "cust"
//                        credentials { username "$mavenUsername"; password "$mavenPassword" }
//                        url = version.endsWith("SNAPSHOT") ? localMavenSnapshotRU : localMavenReleaseRU
//                        allowInsecureProtocol true
//                    }
//                    maven {
//                        name "central"
//                        credentials { username "$mavenUsername"; password "$mavenPassword" }
//                        url = version.endsWith("SNAPSHOT") ? localMavenSnapshotRU : localMavenReleaseRU
//                    }
//                }
//
//                signing {
//                    required { gradle.taskGraph.allTasks.any { it.name == "publishMavenJavaPublicationToCentralRepository" } }
//
//                    sign publishing.publications.mavenJava
//                }
//            }
//        }
//    }
//    test {
//        useJUnitPlatform()
//    }
//
//    apply from: "$rootDir/gradle/spotbugs/spotbugs.gradle"
//    apply from: "$rootDir/gradle/checkstyle/checkstyle.gradle"
////    apply from: "$rootDir/gradle/git-hooks/git-hooks.gradle"
//    apply from: "$rootDir/gradle/dependency-check/dependency-check.gradle"
//    apply from: "$rootDir/gradle/version-info.gradle"
////    apply from: "$rootDir/gradle/jacoco.gradle"
//}

