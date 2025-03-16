plugins {
    java
}

group = "io.github.sunset-of-dev"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

repositories {
    mavenCentral()
}

val pgVersion = "42.7.5"
val junitVersion = "5.12.1"

dependencies {
    implementation("org.postgresql:postgresql:{pgVersion}")

    testImplementation(platform("org.junit:junit-bom:{junitVersion}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
}