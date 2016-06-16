package xyz.errorist.versioneer

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import org.junit.contrib.java.lang.system.EnvironmentVariables
import org.junit.Rule
import spock.lang.*


class ProjectSpec extends Specification {

    @Rule EnvironmentVariables globalEnvs = new EnvironmentVariables()

    def "feature branch"() {
        def repo = new GitRepo()
        def project = ProjectBuilder.builder()
                                    .withProjectDir(repo.gitTree)
                                    .build()
        repo.init()
        project.plugins.apply('xyz.errorist.versioneer')
        project.versioneer.with {
            envs = []
        }

        when: "I start to work on a feature"
        repo.branch('feature/abc-123')
        repo.commit('a')
        project.versioneer.reset()

        then:
        assert project.version == '0.0.1-dev.1+06ca36c'
        assert project.versioneer.info.semver == '0.0.1-dev.1+06ca36c'
        assert project.versioneer.info.maven == '0.0-ABC-123-SNAPSHOT'
        assert project.versioneer.info.rpm == '0.0.1-0.1.dev.1.06ca36c'

        when:
        project.versioneer.flavor = 'semver'

        then:
        assert project.version == '0.0.1-dev.1+06ca36c'

        when:
        project.versioneer.flavor = 'maven'

        then:
        assert project.version == '0.0-ABC-123-SNAPSHOT'

        when:
        project.versioneer.flavor = 'rpm'

        then:
        assert project.version == '0.0.1-0.1.dev.1.06ca36c'
    }

    def "release branch"() {
        def repo = new GitRepo()
        def project = ProjectBuilder.builder()
                                    .withProjectDir(repo.gitTree)
                                    .build()
        repo.init()
        project.plugins.apply('xyz.errorist.versioneer')
        project.versioneer.with {
            envs = []
        }

        when: "I start to work on a release"
        repo.branch('release/2.0')
        repo.commit('a')
        repo.tag('v2.0.1')
        project.versioneer.reset()

        then:
        assert project.version == '2.0.1'
        assert project.versioneer.info.semver == '2.0.1'

        when: "I've committed without a tag"
        repo.commit('b')
        project.versioneer.reset()

        then:
        assert project.version == '2.0.1+post.1.0997e80'
        assert project.versioneer.info.semver == '2.0.1+post.1.0997e80'
        assert project.versioneer.info.maven == '2.1.0-ALPHA-0-SNAPSHOT'
        assert project.versioneer.info.rpm == '2.0.1-1.post.1.0997e80'
    }

    def "detached release branch"() {
        def repo = new GitRepo()
        def project = ProjectBuilder.builder()
                                    .withProjectDir(repo.gitTree)
                                    .build()
        repo.init()
        project.plugins.apply('xyz.errorist.versioneer')
        project.versioneer.with {
            envs = ['XYZ_BRANCH']
        }

        when: "I setted my own branch"
        repo.commit('a')
        globalEnvs.set('XYZ_BRANCH', 'release/2.0')
        project.versioneer.reset()

        then:
        assert System.env['XYZ_BRANCH'] == 'release/2.0'
        assert project.version == '2.0.0-dev.1+06ca36c'
        assert project.versioneer.info.type == 'release'
        assert project.versioneer.info.serie == '2.0'
        assert project.versioneer.info.hash == '06ca36c'
        assert project.versioneer.info.semver == '2.0.0-dev.1+06ca36c'

        when: "I've committed in a detached branch"
        repo.commit('b')
        project.versioneer.reset()

        then:
        assert project.version == '2.0.0-dev.1+0997e80'
        assert project.versioneer.info.hash == '0997e80'
        assert project.versioneer.info.semver == '2.0.0-dev.1+0997e80'

        when: "I've tagged my detached branch"
        repo.tag('v2.0.1')
        project.versioneer.reset()

        then:
        assert project.version == '2.0.1'
        assert project.versioneer.info.semver == '2.0.1'

        when:
        repo.commit('c')
        project.versioneer.reset()

        then:
        assert project.version == '2.0.1+post.1.76ebe3b'
        assert project.versioneer.info.hash == '76ebe3b'
        assert project.versioneer.info.semver == '2.0.1+post.1.76ebe3b'
        assert project.versioneer.info.maven == '2.1.0-ALPHA-0-SNAPSHOT'
        assert project.versioneer.info.rpm == '2.0.1-1.post.1.76ebe3b'
    }
}
