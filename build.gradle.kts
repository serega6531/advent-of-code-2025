plugins {
    kotlin("jvm") version "2.2.21"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

dependencies {
    implementation("com.google.ortools:ortools-java:9.14.6206")
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}
