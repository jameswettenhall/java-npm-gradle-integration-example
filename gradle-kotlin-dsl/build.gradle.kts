defaultTasks("build")

tasks.wrapper {
    description = "Regenerates the Gradle Wrapper files"
    gradleVersion = "8.6"
    distributionUrl = "http://services.gradle.org/distributions/gradle-${gradleVersion}-all.zip"
}
