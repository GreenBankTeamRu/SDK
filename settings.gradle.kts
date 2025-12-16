rootProject.name = "SberBusinessApi_SDK"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
include("sber-authorization")
include("sber-instantpayment")
include("build-src")
include("sber-h2h")

rootProject.name = "sber-business-api-sdk"
