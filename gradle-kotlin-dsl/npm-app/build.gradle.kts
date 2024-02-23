import com.github.gradle.node.NodeExtension
import com.github.gradle.node.npm.task.NpmTask

buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("com.github.node-gradle:gradle-node-plugin:7.0.2")
    }
}

plugins {
    base
    id("com.github.node-gradle.node") version "7.0.2" // gradle-node-plugin
}

node {
    /* gradle-node-plugin configuration
       https://github.com/node-gradle/gradle-node-plugin/blob/master/docs/usage.md

       Task name pattern:
       ./gradlew npm_<command> Executes an NPM command.
    */

    // Version of node to use.
    version.set("20.11.1")

    // Version of npm to use.
    npmVersion.set("10.4.0")

    // If true, it will download node using above parameters.
    // If false, it will try to use globally installed node.
    download.set(true)
}

tasks.named<NpmTask>("npm_run_build") {
    // make sure the build task is executed only when appropriate files change
    inputs.files(fileTree("public"))
    inputs.files(fileTree("src"))

    // "node_modules" appeared not reliable for dependency change detection (the task was rerun without changes)
    // though "package.json" and "package-lock.json" should be enough anyway
    inputs.file("package.json")
    inputs.file("package-lock.json")

    outputs.dir("build")
}

// pack output of the build into JAR file
val packageNpmApp by tasks.registering(Jar::class) {
    dependsOn("npm_run_build")
    baseName = "npm-app"
    extension = "jar"
    destinationDir = file("${projectDir}/build_packageNpmApp")
    from("build") {
        // optional path under which output will be visible in Java classpath, e.g. static resources path
        into("static")
    }
}

// declare a dedicated scope for publishing the packaged JAR
val npmResources by configurations.creating

configurations.named("default").get().extendsFrom(npmResources)

// expose the artifact created by the packaging task
artifacts {
    add(npmResources.name, packageNpmApp.get().archivePath) {
        builtBy(packageNpmApp)
        type = "jar"
    }
}

tasks.assemble {
    dependsOn(packageNpmApp)
}

val testsExecutedMarkerName: String = "${projectDir}/.tests.executed"

val test by tasks.registering(NpmTask::class) {
    dependsOn("assemble")

    // force Jest test runner to execute tests once and finish the process instead of starting watch mode
    environment.set(mapOf("CI" to "true"))

    args.set(listOf("run", "test"))

    inputs.files(fileTree("src"))
    inputs.file("package.json")
    inputs.file("package-lock.json")

    // allows easy triggering re-tests
    doLast {
        File(testsExecutedMarkerName).appendText("delete this file to force re-execution JavaScript tests")
    }
    outputs.file(testsExecutedMarkerName)
}

tasks.check {
    dependsOn(test)
}

tasks.clean {
    delete(packageNpmApp.get().archivePath)
    delete(testsExecutedMarkerName)
}
