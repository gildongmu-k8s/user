plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "gildongmu"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation ("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.12.5")
    implementation("org.hibernate:hibernate-validator:8.0.1.Final")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
