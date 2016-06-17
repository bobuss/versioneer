package xyz.errorist.versioneer.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class PrintInfoTask extends DefaultTask {

    PrintInfoTask() {
       description = 'Print inferred information'
       group = 'Versioneer'
    }

    @TaskAction
    def run() {
        project.versioneer.info.with {
            println "versioneer.info.build = ${build}"
            println "versioneer.info.type = ${type}"
            println "versioneer.info.serie = ${serie}"
            println "versioneer.info.branch = ${branch}"
            println "versioneer.info.closestTag = ${closestTag}"
            println "versioneer.info.distance = ${distance}"
            println "versioneer.info.hash = ${hash}"

            maven.with {
                println "versioneer.info.maven = ${full}"
                println "versioneer.info.maven.version = ${version}"
                println "versioneer.info.maven.qualifier = ${qualifier}"
                println "versioneer.info.maven.snapshot = ${snapshot}"
            }

            semver.with {
                println "versioneer.info.semver = ${full}"
                println "versioneer.info.semver.version = ${version}"
                println "versioneer.info.semver.prerelease = ${prerelease}"
                println "versioneer.info.semver.meta = ${meta}"
            }

            rpm.with {
                println "versioneer.info.rpm = ${full}"
                println "versioneer.info.rpm.version = ${version}"
                println "versioneer.info.rpm.build = ${build}"
            }
        }
    }
}
