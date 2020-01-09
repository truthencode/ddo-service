// Welcome to a clean start

plugins {
    id("org.shipkit.java") version "2.2.6"
}

// shipkit config with gradle 6
tasks.withType(org.shipkit.gradle.notes.AbstractReleaseNotesTask::class.java) {
    this.publicationRepository = "https://dl.bintray.com/truthencode/ddo-service"
}

// rootProject.apply { from(rootProject.file("gradle/shipkit")) }