import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.spring") version "1.7.0"
}

group = "dev.d1s"
version = "0.2.0-beta.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

val holeKtVersion: String by project
val picnicVersion: String by project
val teabagsVersion: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    implementation("com.github.hole-project.hole-kt:spring-boot-starter-hole-kt:$holeKtVersion")
    implementation("com.jakewharton.picnic:picnic:$picnicVersion")
    implementation("dev.d1s.teabags:teabag-stdlib:$teabagsVersion")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events.addAll(
            setOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
            )
        )
    }
}

sourceSets.getByName("test") {
    java.srcDir("./test")
}