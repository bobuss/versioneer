package xyz.errorist.versioneer.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class PrintTask extends DefaultTask {
    @TaskAction
    def run() {
        println project.versioneer.full
    }
}
