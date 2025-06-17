#!/usr/bin/env groovy
import java.util.regex.Matcher
import java.util.regex.Pattern

start(args)

def start(String[] args) {
    if (args.length != 2) {
        usage()
    } else {
        setVersion(args[0], args[1])
    }
}

def usage() {
    println "Usage: ./setVersion.groovy from-version to-version"
}

def setVersion(String fromVersion, String toVersion) {
    replaceInFile('jreleaser.yml', "(.*version: )(${fromVersion})", toVersion)
    replaceInFile('build.gradle', "(    version = ')(${fromVersion})(')", toVersion)
    replaceInFile('README.md', "(documentationGeneratorVersion = )(${fromVersion})", toVersion)
    replaceInFile('demos/demo-data-dictionary/gradle.properties', "(documentationGeneratorVersion = )(${fromVersion})", toVersion)
    replaceInFile('demos/demo-data-lineage/gradle.properties', "(documentationGeneratorVersion = )(${fromVersion})", toVersion)
    replaceInFile('demos/demo-erdiagram/gradle.properties', "(documentationGeneratorVersion = )(${fromVersion})", toVersion)
    replaceInFile('demos/demo-erdiagram/pom.xml', "(.*<documentation-generator.version>)(${fromVersion})(</documentation-generator.version>)", toVersion)
    replaceInFile('demos/demo-sqlscript/gradle.properties', "(documentationGeneratorVersion = )(${fromVersion})", toVersion)
    replaceInFile('documentation-generator-maven-plugin/pom.xml', "(.*<version>)(${fromVersion})(</version>)", toVersion)
    replaceInFile('documentation-generator-maven-plugin/jreleaser.yml', "(.*version: )(${fromVersion})", toVersion)
    replaceInFile('documentation-generator-maven-plugin/README.md', "(.*<version>)(${fromVersion})(</version>)", toVersion)
    replaceInFile('documentation-generator-gradle/README.md', "(documentationGeneratorVersion = )(${fromVersion})", toVersion)
}

def replaceInFile(String filename, String regex, String toVersion) {
    File file = new File(filename)
    File tmpFile = new File(filename + ".tmp")
    if (tmpFile.exists()) {
        tmpFile.delete()
    }
    Pattern pattern = Pattern.compile(regex)
    file.text.eachLine { line ->
        Matcher lineMatcher = pattern.matcher(line)
        if (lineMatcher.matches()) {
            if (lineMatcher.groupCount() < 3) {
                line = line.replaceAll(pattern, "\$1${toVersion}".toString())
            } else {
                line = line.replaceAll(pattern, "\$1${toVersion}\$3".toString())
            }
        }
        tmpFile.append("$line\n");
    }
    file.delete()
    tmpFile.renameTo(file)
}
