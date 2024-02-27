buildscript {

    val springBootVersion by extra("3.2.2.RELEASE")

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}

plugins {
    java
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "eu.xword.labs.gc"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")

    runtimeOnly(project(path = ":npm-app", configuration = "npmResources"))
}

tasks.test {
    useJUnitPlatform()
    testLogging({
        events("passed", "skipped", "failed")
    })
}
