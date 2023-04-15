import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
}

group = "ru.apexman"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${properties["springBootVersion"]}")
//    implementation("org.springframework.boot:spring-boot-starter-security:${properties["springBootVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-web:${properties["springBootVersion"]}")
//    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.4")
//    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer:3.1.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${properties["jacksonVersion"]}")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:${properties["jacksonVersion"]}")
//    implementation("org.flywaydb:flyway-core:${properties["flywayVersion"]}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${properties["jetBrainsKotlinVersion"]}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${properties["jetBrainsKotlinVersion"]}")
//    implementation("io.jsonwebtoken:jjwt:${properties["jsonWebTokenVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-webflux:${properties["springBootVersion"]}")
    implementation("commons-codec:commons-codec:${properties["commonsCodecVersion"]}")
    implementation("org.springdoc:springdoc-openapi-ui:${properties["springDocVersion"]}")
    implementation("org.springdoc:springdoc-openapi-kotlin:${properties["springDocVersion"]}")
    implementation("org.zeroturnaround:zt-zip:${properties["zeroTurnAroundVersion"]}")
    implementation("org.apache.commons:commons-lang3:${properties["apacheCommonLangVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
//    runtimeOnly("org.postgresql:postgresql:${properties["postgresVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-test:${properties["springBootVersion"]}")
    implementation("org.springframework.security:spring-security-test:${properties["springVersion"]}")
    implementation("org.mockito.kotlin:mockito-kotlin:${properties["mockitoKotlinVersion"]}")
    implementation("org.junit.jupiter:junit-jupiter-engine:${properties["junitVersion"]}")
    implementation("org.junit.jupiter:junit-jupiter-api:${properties["junitVersion"]}")
//    testImplementation("io.zonky.test:embedded-database-spring-test:2.1.2")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:${properties["springBootVersion"]}")
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
        showStandardStreams = true
    }
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "ru.apexman.localautotest.MainApplication")
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
