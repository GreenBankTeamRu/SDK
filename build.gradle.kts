plugins {
    java
    jacoco
    `maven-publish`
}

group = "ru.sberbank.sbbol.sberbusinessapi"

val tokenName = project.properties["tokenName"] as String?
val tokenPassword = project.properties["tokenPassword"] as String?

allprojects {

    val projectVersion = project.properties["version"] as String

    tasks.withType<JavaExec> {
        systemProperty("app.version", projectVersion)
    }

    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation("commons-io:commons-io:2.8.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.3")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("jakarta.validation:jakarta.validation-api:3.0.0")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
    implementation("com.nimbusds:nimbus-jose-jwt:9.37.3")
//    complileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

}

apply(from = "publishing.gradle.kts")