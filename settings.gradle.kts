// settings (Kotlin)


fun includeProject(projectDirName: String, projectName: String) {
    logger.info("I am in include project function")
    val baseDir = File(settingsDir, projectDirName)
    val projectDir = File(baseDir, projectName)
    val buildFileName = "${projectName}.scala-platform.gradle.kts"

    assert(projectDir.isDirectory())
    assert(File(projectDir, buildFileName).isFile())

    include(projectName)
    logger.info("Including Project $projectName in $projectDir with using build file $buildFileName")
    project(":${projectName}").projectDir = projectDir
    project(":${projectName}").buildFileName = buildFileName
}

// at some point in the future, see if we can safely make this property optional so there is no build warning if it is not specified or create a sensible default
val projectFolderDelimiter :String by settings
val projectFolders: List<String>
    get() =
        settings.extra["projectFolders"]?.toString()?.split(projectFolderDelimiter) ?: listOf()

logger.info("checking ${projectFolders.size} project folders")
projectFolders.forEach { dirName ->
    val subdir = File(rootDir, dirName)
    logger.info("checking for projects in subDir: ${subdir.name}")
    subdir.walkTopDown().maxDepth(1).forEach { dir ->
        val fileNames = listOf(dir.name, "build")
        var found = false
        for (f in fileNames) {
            val buildFileName = File(dir, "$f.gradle.kts")
            logger.info("looking in ${dir.name} for buildFile $buildFileName")
            if (buildFileName.exists()) {
                logger.info("FOUND: $buildFileName")
                includeProject(dirName, dir.name)
                found = true
                break
            }
        }
        if (!found) logger.warn("No build file found in did not exist in ${dir.name}")
    }
}