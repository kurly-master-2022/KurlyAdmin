import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "master.kurly"
version = "1.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11
val bootJar: BootJar by tasks

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Spring JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // MySQL driver
    implementation("mysql:mysql-connector-java")
    // AWS cloudwatch
    implementation("com.amazonaws:aws-java-sdk-cloudwatch:1.12.283")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

fun addProcfile(dir: String){
    println("Creating a Procfile at $dir")
    File(dir, "Procfile").appendText(
        "${project.name}: java -jar -Dserver.port=5000 ${bootJar.archiveFileName.get()}"
    )
}

task("ebPackage", Copy::class) {
    val ebDeployDir = "${project.projectDir}/ebpackage"
    doFirst {
        println("Packaging the app for eb.")
        delete(ebDeployDir)
    }
    from(bootJar)
    into(ebDeployDir)
    doLast {
        addProcfile(ebDeployDir)
        println("Packaging done.")
    }
}

bootJar.finalizedBy("ebPackage")
