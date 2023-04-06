/**
 * config in ~/.gradle/gradle.properties
 * signing.keyId, signing.password, signing.secretKeyRingFile
 * xxReleaseRepoUrl, xxSnapshotRepoUrl, xxUsername, xxPassword
 */

plugins {
    signing
    `java-library`
    `maven-publish`
}

group = "top.abosen.xboot"
version = Ci.publishVersion

val publications: PublicationContainer = (extensions.getByName("publishing") as PublishingExtension).publications
val javadoc = tasks.named("javadoc")

val javadocJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles java doc to jar"
    archiveClassifier.set("javadoc")
    from(javadoc)
}


data class MavenConf(val name: String) {
    private val releaseRepoUrlLiteral: String = "${name}ReleaseRepoUrl"
    private val snapshotRepoUrlLiteral: String = "${name}SnapshotRepoUrl"
    private val usernameLiteral: String = "${name}Username"
    private val passwordLiteral: String = "${name}Password"

    private fun property(literal: String): String {
        return project.findProperty(literal) as String? ?: ""
    }

    val releaseUrl: String get() = property(releaseRepoUrlLiteral)

    val snapshotUrl: String get() = property(snapshotRepoUrlLiteral)
    val username: String get() = System.getenv(usernameLiteral) ?: property(usernameLiteral)

    val password: String get() = System.getenv(passwordLiteral) ?: property(passwordLiteral)
    fun isAllowInsecureProtocol(): Boolean = releaseUrl.startsWith("http:") || snapshotUrl.startsWith("http:")

    fun isValidConfig(): Boolean =
        listOf(releaseUrl, snapshotUrl, username, password).stream().allMatch(String::isNotEmpty)

}


publishing {
    repositories {
        for (item in listOf("central", "aliyun", "inner")) {
            val conf = MavenConf(item)
            if(!conf.isValidConfig()) continue;
            maven {
                name = conf.name
                url = if (Ci.isRelease) uri(conf.releaseUrl) else uri(conf.snapshotUrl)
                isAllowInsecureProtocol = conf.isAllowInsecureProtocol()
                credentials {
                    username = conf.username
                    password = conf.password
                }
            }
        }
    }
    publications{
        create<MavenPublication>("maven"){
            from(components["java"])
            artifact(javadocJar)
            println("publishing $artifactId:$version")
        }
    }

    publications.withType<MavenPublication>().forEach {
        it.apply {
            pom {
                name.set("xboot")
                description.set("Java scaffold tool collection")
                url.set("https://github.com/qbosen/xboot")

                scm {
                    connection.set("scm:git:https://github.com/qbosen/xboot")
                    developerConnection.set("scm:git:https://github.com/qbosen")
                    url.set("https://github.com/qbosen/xboot")
                }

                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://opensource.org/licenses/Apache-2.0")
                    }
                }

                developers {
                    developer {
                        id.set("qbosen")
                        name.set("Baisen Qiu")
                        email.set("abosen@qq.com")
                    }
                }
            }
        }
    }

    signing {
        if (Ci.isRelease) {
            sign(publications)
        }
    }
}