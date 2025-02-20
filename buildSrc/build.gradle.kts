
plugins {
    `java-library`
}
val tokenName = project.properties["tokenName"] as String?
val tokenPassword = project.properties["tokenPassword"] as String?

repositories {
    maven {
        url = uri("https://nexus-ci.delta.sbrf.ru/repository/maven-proxy-lib-internal/")
        credentials {
            username = tokenName
            password = tokenPassword
        }
        isAllowInsecureProtocol = true
    }
    maven {
        url = uri("https://nexus-ci.delta.sbrf.ru/repository/public/")
        credentials {
            username = tokenName
            password = tokenPassword
        }
        isAllowInsecureProtocol = true
    }
    maven {
        credentials {
            username = tokenName
            password = tokenPassword
        }
        url = uri("https://nexus-ci.delta.sbrf.ru/repository/maven-lib-int/")

    }
}

dependencies {
    implementation("ru.sbt.meta:meta-gradle-plugin:1.4.0")
    implementation("sbp.eip.metamodel:eip-metamodel-scanner-gradle-plugin:3.1.14-jdk11") {
        exclude("com.fasterxml.jackson.core", "jackson-databind")
    }
    implementation("sbp.eip.metamodel:eip-metamodel-core:3.1.14-jdk11") {
        exclude("com.fasterxml.jackson.core", "jackson-databind")
    }
}