import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.4.1"
val junitJupiterVersion = "5.9.1"

val mainVerticleName = "com.talkhub.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  // https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client
  implementation("org.mariadb.jdbc:mariadb-java-client:3.0.8")
  // https://mvnrepository.com/artifact/com.zaxxer/HikariCP
  implementation("com.zaxxer:HikariCP:4.0.3")
  // https://mvnrepository.com/artifact/io.lettuce/lettuce-core
  implementation("io.lettuce:lettuce-core:6.2.0.RELEASE")
  // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
  implementation("org.slf4j:slf4j-api:2.0.2")
  // https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12
  implementation("org.slf4j:slf4j-log4j12:2.0.2")
  // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
  implementation("com.squareup.okhttp3:okhttp:4.10.0")
  // https://mvnrepository.com/artifact/com.google.code.gson/gson
  implementation("com.google.code.gson:gson:2.9.1")
  // https://mvnrepository.com/artifact/io.vertx/vertx-web
  implementation("io.vertx:vertx-web:4.4.1")
  // https://mvnrepository.com/artifact/io.vertx/vertx-core
  implementation("io.vertx:vertx-core:4.3.3")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
