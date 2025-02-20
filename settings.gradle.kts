pluginManagement {
    repositories {
        val tokenName: String by settings
        val tokenPassword: String by settings
        maven {
            url = uri("https://nexus-ci.delta.sbrf.ru/repository/public/")
            credentials {
                username = tokenName
                password = tokenPassword
            }
            isAllowInsecureProtocol = true
        }

        maven {
            url = uri("https://nexus-ci.delta.sbrf.ru/repository/maven-lib-int/")
            credentials {
                username = tokenName
                password = tokenPassword
            }
            isAllowInsecureProtocol = true
        }
    }
}

rootProject.name = "sber-business-api-sdk"
