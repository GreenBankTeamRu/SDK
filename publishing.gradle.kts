import org.gradle.api.tasks.bundling.Jar

val tokenName: String? by project
val tokenPassword: String? by project

repositories {
    mavenCentral()
}

tasks.register<Zip>("fullDistrib") {
    dependsOn(":build-src:buildInstantPayment", ":build-src:buildH2h")

    destinationDirectory.set(file("${layout.buildDirectory.get()}"))
    archiveFileName.set("${rootProject.name}.zip")

    from("${rootDir}/build-src/build/libs") {
        include("*.jar")
        into("libs")
    }

    from(rootDir) {
        exclude("**/build/**")
        exclude("**/.gradle/**")
        exclude("**/.idea/**")
        exclude("**/*.iml")
        into("src")
    }
}


// Исправленный блок publishing (без круглых скобок)
configure<org.gradle.api.publish.PublishingExtension> {
    publications {
        create<MavenPublication>("publish") {
            groupId = "${project.properties["groupId"]}"
            artifactId = "${project.properties["artifactId"]}"

            artifact(tasks["fullDistrib"]) {
                classifier = "distrib"
            }
        }
    }
    repositories {
        maven {
            name = "publish"
            url = uri(project.properties["repo"]!!)
            isAllowInsecureProtocol = true
            credentials {
                username = tokenName
                password = tokenPassword
            }
        }
    }
}