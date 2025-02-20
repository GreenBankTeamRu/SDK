import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    `java-library`
    idea
    jacoco
    id("org.springframework.boot") version "2.5.15"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.sonarqube") version "3.2.0"
    id("ru.sbt.meta.meta-gradle-plugin")
    id("org.openapi.generator") version "5.2.0"
    id("org.asciidoctor.jvm.convert") version "2.4.0"
    id("ru.sber.dcbqa.dcb-test-plugin") version "4.10.1"
    id("eip-metamodel-scanner")
    `maven-publish`
}

//нужны для meta
val nexusLogin = project.properties["nexusLogin"] as String?
val nexusPassword = project.properties["nexusPassword"] as String?

val tokenName = project.properties["tokenName"] as String?
val tokenPassword = project.properties["tokenPassword"] as String?

val standinPluginVersion: String by project

repositories {
    repositories {
        maven {
            url = uri("https://nexus-ci.delta.sbrf.ru/repository/public/")
            credentials {
                username = tokenName
                password = tokenPassword
            }
        }
        maven {
            url = uri("https://nexus-ci.delta.sbrf.ru/repository/maven-lib-int/")
            credentials {
                username = tokenName
                password = tokenPassword
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

jacoco {
    toolVersion = "0.8.7"
}

dependencies {
    // зависимости Spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    // Liquibase для обновления БД
    implementation("org.liquibase:liquibase-core")
    // jcache
    implementation("org.hibernate:hibernate-jcache:5.4.29.Final")
    // logstash для отправки логов в системный журнал в виде JSON
    implementation("net.logstash.logback:logstash-logback-encoder:6.3")
    // mapstruct для перекладки между моделями
    implementation("org.mapstruct:mapstruct:1.4.2.Final")
    // платформенный плагин для поддержки StandIn
    implementation("sbp.integration.orm:sbp-hibernate-standin:$standinPluginVersion")
    // нужна для SI-плагина
    implementation("jakarta.persistence:jakarta.persistence-api:3.0.0")
    // библиотека для автоматизации health-check
    // https://confluence.sberbank.ru/display/RND/Healthcheck+in+microservices
    implementation("ru.sbrf.sbbol.starters:http-healthcheck-starter:4.1")


    // интеграция с фабрикой данных
    implementation("sbp.integration.datafabric:datafabric-replication-hibernate:4.0.27")
    implementation("sbp.integration.datafabric:datafabric-replication-core:4.0.27")

    // реализация кэша
    runtimeOnly("org.ehcache:ehcache:3.9.2")
    // postgres для prod-сборки
    runtimeOnly("org.postgresql:postgresql:42.2.19")

    // тесты для Spring Boot
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("com.vaadin.external.google", "android-json")
        exclude("com.h2database", "h2")
    }
    // заглушка для тестирования репликации между БД
    testImplementation("sbp.integration.orm:orm-tests-common:$standinPluginVersion") {
        exclude("com.vaadin.external.google", "android-json")
        exclude("com.h2database", "h2")
    }
    // Для корректной обработки покрытия по api тестам
    testImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")

    // OpenApi Client
    testImplementation("io.springfox:springfox-swagger2:2.8.0")
    testImplementation("io.springfox:springfox-swagger-ui:2.8.0")
    testImplementation("org.openapitools:jackson-databind-nullable:0.1.0")

    // фреймворк rest assured для тестов
    testImplementation("io.rest-assured:rest-assured:4.2.1")
    // фреймворк testcontainers для поднятия контейнеров (например, с базой данных) на тестах
    testImplementation("org.testcontainers:postgresql:1.17.3")

    // генерация случайно заполненных pojo
    testImplementation("org.jeasy:easy-random-core:4.3.0")

    // обработчик аннотаций для работы mapstruct
    annotationProcessor("org.mapstruct:mapstruct-processor:1.4.2.Final")
    // обработчик аннотаций для обработки пользовательских конфигураций спринга (@ConfigurationProperties)
    annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")

    dependencyLocking {
        lockAllConfigurations()
        lockFile.set(file("${rootDir}/gradle/dependency-locks/gradle-${project.name}.lockfile"))
    }
}

tasks.register<Test>("generateVectorTest") {
    useJUnitPlatform()
    filter { includeTestsMatching("**changevector.generate.*") }
    jvmArgs  ("--add-opens", "java.base/java.time=ALL-UNNAMED")
}
tasks.register<Test>("applyVectorTest") {
    useJUnitPlatform()
    filter { includeTestsMatching("**changevector.apply.*") }
    jvmArgs  ("--add-opens", "java.base/java.time=ALL-UNNAMED")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    exclude("**/changevector/**")
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = TestExceptionFormat.FULL
    }
    systemProperty("file.encoding", "UTF-8")
}

idea {
    module {
        outputDir = file("${projectDir}/build/classes/java/main")
        testOutputDir = file("${projectDir}/build/classes/java/test")
    }
}

//TODO: Настроить пути для исключений, которые не попадут в анализа покрытия тестами кода проекта.
val coverageExclusions = listOf(
    // Классы с конфигурациями
    "**/*Application*",
    //POJO
    "**/model/**",
    "**/transport/**",
    "**/deserializer/**",
    "**/monitoring/**",
    "**/enums/**",
    "**/entity/**",
    "**/dto/**",
    //Классы с контроллерами и вызовами сервисов без логики, в которых происходит только вызов соответствующего сервиса
    "**/*Invoker*",
    "**/*Controller*",
    "**/*Adapter*",
    //Классы с заглушками для локальной разработки
    "**/*Stub*",
    //Сериализаторы/десериализаторы
    "**/identifier/*Deserializer*",
    "**/identifier/*Serializer*",
    "**/handler/*Handler*",
    //Классы с exception
    "**/exception/**",
    //Инфраструктура
    "**/security/CryptoProvider*",
    "**/security/CryptoService*",
    "**/processor/*Processor*",
    "**/swagger/**",
    "**/infrastructure/**",
    "**/*Aspect*",
    "**/*Config*"
)

tasks {
    register("sonarCoverage", DefaultTask::class) {
        group = "verification"
        dependsOn(jacocoTestReport)
        finalizedBy(sonarqube)
    }
}

tasks {
    val buildAdminGuideDocs by registering(org.asciidoctor.gradle.jvm.AsciidoctorTask::class) {
        setSourceDir(file("asciidoc/admin-guide"))
        setOutputDir(file("${buildDir}/docs/admin-guide"))
        baseDirFollowsSourceDir()
        sources(delegateClosureOf<PatternSet> {
            include("index.adoc")
        })
    }

    val buildDevGuideDocs by registering(org.asciidoctor.gradle.jvm.AsciidoctorTask::class) {
        setSourceDir(file("asciidoc/dev-guide"))
        setOutputDir(file("${buildDir}/docs/dev-guide"))
        baseDirFollowsSourceDir()
        sources(delegateClosureOf<PatternSet> {
            include("index.adoc")
        })
        attributes(mapOf("source-highlighter" to "coderay"))
    }

    build {
        dependsOn(buildAdminGuideDocs)
        dependsOn(buildDevGuideDocs)
    }
}

val resolveAndLockAll by tasks.registering {
    doFirst {
        require(gradle.startParameter.isWriteDependencyLocks)
    }
    doLast {
        configurations.filter {
            it.isCanBeResolved
        }.forEach { it.resolve() }
    }
}

val copyOpenApiSpec by tasks.registering(Copy::class) {
    from("openapi")
    into("${rootProject.buildDir}/resources/main/openapi")
}

val compileJava by tasks.getting(JavaCompile::class) {
    options.encoding = "UTF-8"
    dependsOn(copyOpenApiSpec)
}

// отключаем генерацию *-plain.jar
val jar: Task by tasks.getting {
    enabled = false
}

meta {
    nexusUrl = null
    nexusUser = nexusLogin
    nexusPassword = this@Build_gradle.nexusPassword
    componentId = "${project.properties["metamodel.componentCode"]}"
    ext {
        set("url", "https://meta.sigma.sbrf.ru")
        set("openApiSpecs", listOf("openapi/openapi.yml"))
        set("analyzeJava", false)
        set("failBuildOnError", true)
    }
}

val scanMetamodel: Task by tasks.getting

val reverseAndPublish: Task by tasks.getting {
    dependsOn(scanMetamodel)
}

metamodel {
    componentCode = "${project.properties["metamodel.componentCode"]}"

// Необязательные параметры. Для примера указаны умолчания
    setFormattedOutput(true)
//    schemaVersion = "1.0"
//    formattedOutput = false
//    internalPath = "."
//    interruptOnError = false
//    outputDirectory = null  //- если null - проставится корень jar
}

modelJpa {
    fileName = "${project.properties["metamodel.modelName"]}.ldm.xml"
    modelName = "${project.properties["metamodel.modelName"]}"
    modelVersion = "${project.properties["metamodel.modelVersion"]}"
    packages = "ru"

// Необязательные параметры. Для примера указаны умолчания
//    excludePackages = null
//    additionalTypes = null
//    excludeTypes = null
}

sonarqube {
    properties {
        property("sonar.coverage.jacoco.xmlReportPaths", "${rootProject.buildDir}/coverage/jacoco/jacocoTestReport.xml")
        property("sonar.coverage.exclusions", coverageExclusions.joinToString())
        property(
                "sonar.cpd.exclusions", """
                    src/main/java/ru/sberbank/pprb/sbbol/draft/entity/**,
                    src/main/java/ru/sberbank/pprb/sbbol/draft/dto/**
                """.trimIndent()
        )

    }
}

val openApiGenerate: Task by tasks.getting

val compileTestJava: Task by tasks.getting {
    dependsOn(openApiGenerate)
}

openApiGenerate {
    generatorName.set("java")
    inputSpec.set("$rootDir/openapi/openapi.yml")
    outputDir.set("$buildDir/generated/openapiclient")
    apiPackage.set("ru.sberbank.pprb.sbbol.draft.api")
    invokerPackage.set("ru.sberbank.pprb.sbbol.draft.invoker")
    modelPackage.set("ru.sberbank.pprb.sbbol.draft.model")
    library.set("rest-assured")
    configOptions.set(
            mapOf(
                "dateLibrary" to "java8",
                "serializationLibrary" to "jackson"
            )
    )
}


java.sourceSets["test"].java {
    srcDir("$buildDir/generated/openapiclient/src/main/java")
}

tasks.register("newpatch") {
    description = "Создание sql патча"
    var currentVersion = ""

    doFirst {
        if (!project.hasProperty("patchname")) {
            throw IllegalArgumentException("Property patchname not provided")
        }
        val patchname = project.property("patchname")
        currentVersion = if (project.hasProperty("releaseversion")) {
            project.property("releaseversion").toString()
        } else {
            try {
                //От текущей версии вида major.manor.hotfix_build откидываем последнюю группу с build
                //например от 01.000.00_123-SNAPSHOT получаем версию вида 01.000.00
                //для минора например от 01.000.00_123-SNAPSHOT получаем версию вида 01.000.00 и т.д.
                project.property("version").toString().split("\\_")[0]
            } catch (e: Exception) {
                throw GradleException("Unable to parse version parameter value $version from gradle.properties", e)
            }
        }
        val path = "${projectDir}/src/main/resources/db/changelog"
        val patchDirectory = "sql/${currentVersion}"
        val patchPath = "${path}/${patchDirectory}"

        val folder = File(patchPath)
        if (!folder.exists()) {
            folder.mkdirs()
        }

        val patchTs = System.currentTimeMillis()

        val updateFileName = "${patchTs}_${patchname}.sql"
        val updateFilePath = "${patchPath}/${updateFileName}"

        val patchText = "-- liquibase formatted sql"
        File(updateFilePath).writeText(patchText, Charsets.UTF_8)
        println("File ${updateFileName} was created")

        val changelog = "changelog.yaml"
        val changelogFilePath = "${patchPath}/${changelog}"
        val mainChangeLogFilePath = "${path}/${changelog}"
        val file = File(changelogFilePath)
        var changelogText =
"""  - include:
      file: ${updateFileName}
      relativeToChangelogFile: true
"""
        if (file.length().toInt() == 0) {
            var mainChangelogText =
"""  - include:
      file: ${patchDirectory}/${changelog}
      relativeToChangelogFile: true
"""
            val mainChangeLogFile = File(mainChangeLogFilePath)
            if (mainChangeLogFile.length().toInt() == 0) {
                mainChangeLogFile.appendText("databaseChangeLog:\n${mainChangelogText}", Charsets.UTF_8)
            } else {
                if (!mainChangeLogFile.readText(Charsets.UTF_8).endsWith("\n")) {
                    mainChangelogText = "\n" + mainChangelogText
                }
                mainChangeLogFile.appendText(mainChangelogText, Charsets.UTF_8)
            }
            file.appendText("databaseChangeLog:\n${changelogText}", Charsets.UTF_8)
        } else {
            if (!file.readText(Charsets.UTF_8).endsWith("\n")) {
                changelogText = "\n" + changelogText
            }
            file.appendText(changelogText, Charsets.UTF_8)
        }
    }
}



dcbTestPlugin {
    // FIXME COVERAGE: Прописать идентификатор вашего приложения для корректной выгрузки покрытий в портал http://10.53.120.222:8080/coverage
    // Для получения идентификатора обратиться к команде Сценаристы (AUTOTEST5 https://confluence.sberbank.ru/pages/viewpage.action?pageId=768508170).
    projectKey.set("sber-business-api-sdk")
    jacocoCoverage {
        excludePackages.addAll(coverageExclusions)
    }
}
