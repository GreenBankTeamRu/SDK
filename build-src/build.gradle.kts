import org.gradle.api.tasks.bundling.Jar
import io.freefair.gradle.plugins.lombok.tasks.Delombok

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.freefair.lombok") version "8.4"
}

group = "ru.sberbank.sbbol.sberbusinessapi"
val versionSDK = project.version as String

dependencies {
    implementation(project(":sber-authorization"))
}

// Настройка задачи shadowJar для создания fatJAR
val buildInstantPayment by tasks.registering(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    archiveBaseName.set("sdk-instantpayment")
    archiveClassifier.set("")
    archiveVersion.set(versionSDK)

    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    from(delombokSourcesDir)
    from(project(":sber-authorization").sourceSets["main"].output)
    from(project(":sber-instantpayment").sourceSets["main"].output)
    configurations = listOf(project.configurations.runtimeClasspath.get())
    manifest {
        attributes(
            "Implementation-Title" to "${project.name}-instantpayment",
            "Implementation-Version" to project.version
        )
    }
    from(projectDir) {
        include("LICENSE")
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(tasks.named("javadoc")) {
        into("docs")
    }
}

val buildH2h by tasks.registering(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    archiveBaseName.set("sdk-h2h")
    archiveClassifier.set("")
    archiveVersion.set(versionSDK)

    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    from(delombokSourcesDir)
    from(project(":sber-authorization").sourceSets["main"].output)
    from(project(":sber-h2h").sourceSets["main"].output)
    configurations = listOf(project.configurations.runtimeClasspath.get())
    manifest {
        attributes(
            "Implementation-Title" to "${project.name}-h2h",
            "Implementation-Version" to versionSDK
        )
    }
    from(projectDir) {
        include("LICENSE")
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(tasks.named("javadoc")) {
        into("docs")
    }
}

// Delombok задача
val delombokSourcesDir = "${layout.buildDirectory.get()}/delombokSources"
tasks.named<Delombok>("delombok") {
    input.setFrom(files("src/main/java"))
    target.set(file(delombokSourcesDir))
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(delombokSourcesDir)
}

tasks.named("sourcesJar", Jar::class).configure {
    enabled = false
}

tasks.named("compileJava") {
    dependsOn("delombok")
}

tasks.withType<Checkstyle>().configureEach {
    sourceSets["main"].java.setSrcDirs(listOf("src/main/java"))
}

tasks.named("build").configure {
    dependsOn("sourcesJar")
}

tasks.shadowJar {
    mergeServiceFiles()
    append("META-INF/spring.handlers")
    append("META-INF/spring.schemas")
    append("META-INF/spring.tooling")
}