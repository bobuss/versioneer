package xyz.errorist.versioneer

import org.gradle.api.Project
import org.gradle.api.Plugin
import xyz.errorist.versioneer.tasks.*

class VersioneerPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create('versioneer', VersioneerExtension, project)
        project.task('version', type: PrintTask)
        project.task('versioneer', type: PrintInfoTask)
        project.task('versioneerExport', type: ExportFileTask)
    }
}
