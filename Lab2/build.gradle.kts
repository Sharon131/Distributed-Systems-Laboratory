plugins {
    id("java")
    id("com.palantir.docker-run") version "0.26.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.rabbitmq:amqp-client:5.11.0")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("org.slf4j:slf4j-simple:1.7.30")
}

tasks {
    dockerRun {
        name = "rabbitmq"
        image = "rabbitmq:3-management-alpine"
        ports("5672:5672", "15672:15672", "4369:4369", "25672:25672")
        clean = true
//        networks("rabbitmq_go_net")
    }
}
