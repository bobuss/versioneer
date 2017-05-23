package com.ssn.versioneer.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import com.ssn.versioneer.GitCommandException

class PrintTask extends DefaultTask {

    PrintTask() {
       description = 'Print inferred version'
       group = 'Versioneer'
    }

    @TaskAction
    def run() {
        try {
            println project.version
        } catch (GitCommandException e) {
            throw new GradleException('Is it a git repository?', e)
        }
    }
}
