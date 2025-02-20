pluginManagement {
    repositories {
        maven {
            url = uri("https://nexus-ci.delta.sbrf.ru/repository/public/")
            isAllowInsecureProtocol = true
            val tokenName: String by settings
            val tokenPassword: String by settings
            credentials {
                username = tokenName
                password = tokenPassword
            }
        }
    }
}
