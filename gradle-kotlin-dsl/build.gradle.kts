defaultTasks("build")

tasks.wrapper {
    description = "Regenerates the Gradle Wrapper files"
    gradleVersion = "7.6.4"
    distributionUrl = "http://services.gradle.org/distributions/gradle-${gradleVersion}-all.zip"
}
