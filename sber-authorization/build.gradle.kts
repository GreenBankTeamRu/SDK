plugins {
    id("java")
    `java-library`
}

group = "ru.sberbank.sbbol.sberbusinessapi"
version = "unspecified"

val tokenName = project.properties["tokenName"] as String?
val tokenPassword = project.properties["tokenPassword"] as String?

repositories {
    mavenCentral()
}

dependencies {
    api("org.projectlombok:lombok:1.18.30")
    api("commons-io:commons-io:2.8.0")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    api("com.fasterxml.jackson.core:jackson-databind:2.12.3")
    api("org.hibernate.validator:hibernate-validator:6.2.5.Final")
    api("jakarta.validation:jakarta.validation-api:3.0.2")
    api("com.squareup.okhttp3:okhttp:4.12.0")
    api("com.squareup.okhttp3:logging-interceptor:4.12.0")
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-jackson:2.9.0")
    api("com.squareup.retrofit2:converter-scalars:2.9.0")
    api("javax.validation:validation-api:2.0.1.Final")
    api("org.bouncycastle:bcprov-jdk15on:1.70")
    api("org.bouncycastle:bcpkix-jdk15on:1.70")
    api("com.nimbusds:nimbus-jose-jwt:9.37.3")
    api("org.slf4j:slf4j-api:1..7.36")
    api("ch.qos.logback:logback-classic:1.2.13")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

}

tasks.test {
    useJUnitPlatform()
}