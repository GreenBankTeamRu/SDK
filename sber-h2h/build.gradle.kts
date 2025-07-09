plugins {
    id("java")
}

group = "ru.sberbank.sbbol.sberbusinessapi"


dependencies {
    implementation(project(":sber-authorization"))
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.test {
    useJUnitPlatform()
}