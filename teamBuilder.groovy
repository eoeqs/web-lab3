def revisions = []
def warFiles = []

def proc = "git rev-list -n 4 HEAD".execute()
proc.in.eachLine { line ->
    revisions << line
}

revisions = revisions.drop(1)

def projectDir = new File(args[0]).absolutePath

revisions.each { revision ->
    println "Building revision: ${revision}"

    def stashProc = "git stash --include-untracked".execute(null, new File(projectDir))
    stashProc.waitFor()
    if (stashProc.exitValue() != 0) {
        throw new GradleException("Failed to stash changes before checkout: ${stashProc.err.text}")
    }

    def checkoutProc = "git checkout ${revision}".execute(null, new File(projectDir))
    checkoutProc.waitFor()
    if (checkoutProc.exitValue() != 0) {
        throw new GradleException("Failed to checkout revision: ${revision} - ${checkoutProc.err.text}")
    }

    def gradlewCommand = System.getProperty('os.name').toLowerCase().contains('win') ? 'gradlew.bat' : './gradlew'
    def buildProc = "${gradlewCommand} clean build_task".execute(null, new File(projectDir))
    buildProc.waitFor()
    if (buildProc.exitValue() != 0) {
        throw new GradleException("Failed to build revision: ${revision} - ${buildProc.err.text}")
    }

    def warFileDir = file("${projectDir}/build/libs")
    warFiles.addAll(fileTree(dir: warFileDir, include: '**/*.war').files)

    def stashPopProc = "git stash pop".execute(null, new File(projectDir))
    stashPopProc.waitFor()
    if (stashPopProc.exitValue() != 0) {
        println "Warning: Failed to apply stashed changes after building revision: ${revision} - ${stashPopProc.err.text}"
    }
}

if (warFiles.isEmpty()) {
    throw new GradleException("No WAR files found to archive")
}

def zipFile = new File("${args[1]}/team_builds.zip")
ant.zip(destfile: zipFile) {
    warFiles.each { warFile ->
        if (warFile.exists()) {
            ant.zipfileset(src: warFile, prefix: "builds/")
        } else {
            println "Warning: WAR file not found: ${warFile}"
        }
    }
}

println "The packing is complete: ${zipFile.absolutePath}"
