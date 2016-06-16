package xyz.errorist.versioneer

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import spock.lang.*
import xyz.errorist.versioneer.tasks.*


class TasksSpec extends Specification {
    def "apply print task"() {
        given:
        Project project = ProjectBuilder.builder().build()
        when:
        project.pluginManager.apply 'xyz.errorist.versioneer'
        then:
        assert project.tasks.version instanceof PrintTask
    }
}
