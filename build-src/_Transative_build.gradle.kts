import org.gradle.api.tasks.bundling.Jar

plugins {
    java
    id("io.freefair.lombok") version "8.6"
    `maven-publish`
}

val versionSDK = "release-1.0.0-SNAPSHOT"
group = "ru.sberbank.sbbol.sberbusinessapi"
version = versionSDK


dependencies {]
    implementation(project(":sber-authorization"))
}

// Настройка Delombok задачи
val delombokSourcesDir = "${layout.buildDirectory.get()}/delombokSources"
tasks.named<io.freefair.gradle.plugins.lombok.tasks.Delombok>("delombok") {
    input.setFrom(files("src/main/java"))
    target.set(file(delombokSourcesDir))
}

// Задача для создания sources JAR
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(delombokSourcesDir)
    from("LICENSE")
}

// Создание задачи для сборки sber-instantpayment
val instantPaymentJar by tasks.registering(Jar::class) {
    archiveBaseName.set("sdk-instantpayment")
    archiveVersion.set(versionSDK)

    // Включаем собственные классы из модулей sber-authorization и sber-instantpayment
    from(project(":sber-authorization").sourceSets["main"].output)
    from(project(":sber-instantpayment").sourceSets["main"].output)
    from("LICENSE")

    manifest {
        attributes(
            "Implementation-Title" to "${project.name}-instantpayment",
            "Implementation-Version" to versionSDK,
            "Class-Path" to configurations.runtimeClasspath.get().joinToString(separator = " ") { it.toURI().toString() }
        )
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Создание задачи для сборки sber-h2h
val h2hJar by tasks.registering(Jar::class) {
    archiveBaseName.set("sdk-h2h")
    archiveVersion.set(versionSDK)

    // Включаем собственные классы из модулей sber-authorization и sber-h2h
    from(project(":sber-authorization").sourceSets["main"].output)
    from(project(":sber-h2h").sourceSets["main"].output)
    from("LICENSE")

    manifest {
        attributes(
            "Implementation-Title" to "${project.name}-h2h",
            "Implementation-Version" to versionSDK,
            "Class-Path" to configurations.runtimeClasspath.get().joinToString(separator = " ") { it.toURI().toString() }
        )
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Делаем зависимость между задачами
tasks.named("compileJava") {
    dependsOn("delombok")
}

// Настройка Checkstyle
tasks.withType<Checkstyle>().configureEach {
    sourceSets["main"].java.setSrcDirs(listOf("src/main/java"))
}

// Добавляем сборку sourcesJar в build задачу
tasks.named("build").configure {
    dependsOn(sourcesJar)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "ru.sberbank.sbbol.sberbusinessapi"
            artifactId = "sdk-instantpayment"
            version = versionSDK

            // Публикуем основной JAR
            artifact(instantPaymentJar.get())

            // Публикуем sources JAR
            artifact(sourcesJar.get()) {
                classifier = "sources"
            }

            // Публикуем POM-файл с метаданными
            pom {
                name.set("Sberbank Instant Payment SDK")
                description.set("SDK для работы с моментальными платежами Сбербанка")
                url.set("https://github.com/ваш-репозиторий")
                licenses {
                    license {
                        name.set("Лицензия") // Укажите название лицензии
                        url.set("https://opensource.org/licenses/ваша-лицензия") // Укажите URL лицензии
                    }
                }
                developers {
                    developer {
                        id.set("ваш-id")
                        name.set("Ваше Имя")
                        email.set("ваш-email@example.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/ваш-репозиторий.git")
                    developerConnection.set("scm:git:ssh://github.com/ваш-репозиторий.git")
                    url.set("https://github.com/ваш-репозиторий")
                }
            }
        }
    }

    repositories {
        maven {
            // Укажите URL вашего Maven-репозитория
            url = uri("https://ваш-репозиторий/maven2")
            credentials {
                username = project.findProperty("mavenUser") as String? ?: ""
                password = project.findProperty("mavenPassword") as String? ?: ""
            }
        }
    }
}