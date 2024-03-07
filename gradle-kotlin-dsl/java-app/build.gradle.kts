plugins {
    java
    id("org.springframework.boot") version "3.2.3"
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
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")

    runtimeOnly(project(path = ":npm-app", configuration = "npmResources"))
}

tasks.test {
    useJUnitPlatform()
    testLogging({
        events("passed", "skipped", "failed")
    })
}
