plugins {
    application
    java
}

group = "io.github.sunset-of-dev"
version = "0.0.1-SNAPSHOT"

application {
    mainClass.set("io.github.xfnt.Application")
}

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
val mockitoVersion = "5.16.1"

dependencies {
    implementation("org.postgresql:postgresql:$pgVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
    from({
        configurations.runtimeClasspath.get().filter { it.exists() }.map { zipTree(it) }
    })
}