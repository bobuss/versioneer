package com.ssn.versioneer

import com.ssn.versioneer.tasks.ExportFileTask
import com.ssn.versioneer.tasks.PrintInfoTask
import com.ssn.versioneer.tasks.PrintTask
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.GradleException
import spock.lang.*
import org.springframework.boot.test.OutputCapture
import org.junit.Rule


class TasksSpec extends Specification {

    @Rule OutputCapture capture = new OutputCapture()

    def "Apply tasks"() {
        setup:
        def repo = new GitRepo()
        def project = ProjectBuilder.builder()
                                    .withProjectDir(repo.gitTree)
                                    .build()

        when: "apply version task"
        project.pluginManager.apply 'com.ssn.versioneer'

        then:
        assert project.tasks.version instanceof PrintTask
        assert project.tasks.versioneerExport instanceof ExportFileTask
        assert project.tasks.versioneer instanceof PrintInfoTask
    }

    def "Repository is not initialized"() {
        setup:
        def repo = new GitRepo()
        def project = ProjectBuilder.builder()
                                    .withProjectDir(repo.gitTree)
                                    .build()

        when: "apply version task"
        project.pluginManager.apply 'com.ssn.versioneer'
        project.tasks.version.execute()

        then:
        thrown GradleException
    }

    def "There is no commit"() {
        setup:
        def repo = new GitRepo()
        def project = ProjectBuilder.builder()
                                    .withProjectDir(repo.gitTree)
                                    .build()

        when: "apply version task"
        repo.init()
        project.pluginManager.apply 'com.ssn.versioneer'
        project.tasks.version.execute()
        project.tasks.version.execute()

        then:
        thrown GradleException
    }

    def "A first commit"() {
        setup:
        def repo = new GitRepo()
        def project = ProjectBuilder.builder()
                                    .withProjectDir(repo.gitTree)
                                    .build()

        when:
        repo.init()
        repo.commit('a')
        project.pluginManager.apply 'com.ssn.versioneer'
        project.tasks.version.execute()

        then:
        assert capture.toString() == '0.0.1-dev.1+06ca36c\n'
    }

    def "Branch it, tag it"() {
        setup:
        def repo = new GitRepo()
        def project = ProjectBuilder.builder()
                                    .withProjectDir(repo.gitTree)
                                    .build()

        when:
        repo.init()
        repo.branch('release/2.0')
        repo.commit('a')
        repo.tag('v2.0.1-beta.1')
        project.pluginManager.apply 'com.ssn.versioneer'
        project.tasks.version.execute()

        then:
        assert capture.toString() == '2.0.1-beta.1\n'
    }

    def "Show whole informations"() {
        setup:
        def repo = new GitRepo()
        def project = ProjectBuilder.builder()
                                    .withProjectDir(repo.gitTree)
                                    .build()

        when:
        repo.init()
        repo.branch('release/2.0')
        repo.commit('a')
        repo.tag('v2.0.1-beta.1')
        project.pluginManager.apply 'com.ssn.versioneer'
        project.tasks.versioneer.execute()

        then:
        def result = capture.toString().split('\n')
        assert result.contains('versioneer.info.build = 1')
        assert result.contains('versioneer.info.type = release')
        assert result.contains('versioneer.info.serie = 2.0')
        assert result.contains('versioneer.info.branch = release/2.0')
        assert result.contains('versioneer.info.closestTag = 2.0.1-beta.1')
        assert result.contains('versioneer.info.distance = 0')
        assert result.contains('versioneer.info.hash = 06ca36c')
        assert result.contains('versioneer.info.maven = 2.0.1-BETA-1')
        assert result.contains('versioneer.info.maven.version = 2.0.1')
        assert result.contains('versioneer.info.maven.qualifier = BETA-1')
        assert result.contains('versioneer.info.maven.snapshot = null')
        assert result.contains('versioneer.info.semver = 2.0.1-beta.1')
        assert result.contains('versioneer.info.semver.version = 2.0.1')
        assert result.contains('versioneer.info.semver.prerelease = beta.1')
        assert result.contains('versioneer.info.semver.meta = null')
        assert result.contains('versioneer.info.rpm = 2.0.1-0.1.beta.1')
        assert result.contains('versioneer.info.rpm.version = 2.0.1')
        assert result.contains('versioneer.info.rpm.build = 0.1.beta.1')
    }

    def "Export data"() {
        setup:
        def repo = new GitRepo()
        def project = ProjectBuilder.builder()
                                    .withProjectDir(repo.gitTree)
                                    .build()

        when:
        repo.init()
        repo.branch('release/2.0')
        repo.commit('a')
        repo.tag('v2.0.1-beta.1')
        project.pluginManager.apply 'com.ssn.versioneer'
        project.tasks.versioneerExport.execute()

        then:
        def result = project.tasks.versioneerExport.file.text.split('\n')

        assert result.contains('VERSIONEER_BUILD=1')
        assert result.contains('VERSIONEER_TYPE=release')
        assert result.contains('VERSIONEER_SERIE=2.0')
        assert result.contains('VERSIONEER_BRANCH=release/2.0')
        assert result.contains('VERSIONEER_CLOSEST_TAG=2.0.1-beta.1')
        assert result.contains('VERSIONEER_DISTANCE=0')
        assert result.contains('VERSIONEER_HASH=06ca36c')
        assert result.contains('VERSIONEER_MAVEN=2.0.1-BETA-1')
        assert result.contains('VERSIONEER_MAVEN_VERSION=2.0.1')
        assert result.contains('VERSIONEER_MAVEN_QUALIFIER=BETA-1')
        assert result.contains('VERSIONEER_MAVEN_SNAPSHOT=')
        assert result.contains('VERSIONEER_SEMVER=2.0.1-beta.1')
        assert result.contains('VERSIONEER_SEMVER_VERSION=2.0.1')
        assert result.contains('VERSIONEER_SEMVER_PRERELEASE=beta.1')
        assert result.contains('VERSIONEER_SEMVER_META=')
        assert result.contains('VERSIONEER_RPM=2.0.1-0.1.beta.1')
        assert result.contains('VERSIONEER_RPM_VERSION=2.0.1')
        assert result.contains('VERSIONEER_RPM_BUILD=0.1.beta.1')
    }
}
