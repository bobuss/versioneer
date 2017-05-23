package com.ssn.versioneer

import com.ssn.versioneer.tasks.ExportFileTask
import com.ssn.versioneer.tasks.PrintInfoTask
import com.ssn.versioneer.tasks.PrintTask
import org.gradle.api.Project
import org.gradle.api.Plugin

class VersioneerPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create('versioneer', VersioneerExtension, project)
        project.task('version', type: PrintTask)
        project.task('versioneer', type: PrintInfoTask)
        project.task('versioneerExport', type: ExportFileTask)
    }
}
