pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex(
                    "com\\.android.*"
                )
                includeGroupByRegex(
                    "com\\.google.*"
                )
                includeGroupByRegex(
                    "androidx.*"
                )
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(
        RepositoriesMode.FAIL_ON_PROJECT_REPOS
    )
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name =
    "Mindly"
include(
    ":app"
)
include(
    ":feature"
)
include(
    ":feature:common"
)
include(
    ":feature:theme"
)
include(
    ":feature:home"
)
include(
    ":data"
)
include(
    ":feature:chat"
)
include(
    ":api"
)
include(
    ":feature:settings"
)
include(
    ":config"
)
include(
    ":feature:collect"
)
include(
    ":feature:shareReceiver"
)
