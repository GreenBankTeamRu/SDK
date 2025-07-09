rootProject.name = "SberBusinessApi_SDK"

pluginManagement {
    repositories {
        mavenCentral()
        val tokenName: String by settings
        val tokenPassword: String by settings
    }
}
include("sber-authorization")
include("sber-instantpayment")
include("build-src")
include("sber-h2h")

rootProject.name = "sber-business-api-sdk"
