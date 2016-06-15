package xyz.errorist.versioneer

import org.junit.contrib.java.lang.system.EnvironmentVariables
import org.junit.Rule
import spock.lang.*


class GitServiceSpec extends Specification {

    @Rule EnvironmentVariables environmentVariables = new EnvironmentVariables()

    def "get current branch"() {
        given:
        def repo = new GitRepo()
        def scm = new GitService(repo.gitTree)
        repo.init()

        when: "I setted my own branch"
        repo.commit('a')
        environmentVariables.set('XYZ_BRANCH', 'foo');

        then:
        assert 'master' == scm.currentBranch()
        assert 'foo' == scm.currentBranch('XYZ_BRANCH')
        assert 'foo' == scm.currentBranch(['XYZ_BRANCH'])

        when:
        repo.branch('bar')

        then:
        assert 'bar' == scm.currentBranch()
        assert 'foo' == scm.currentBranch('XYZ_BRANCH')
        assert 'foo' == scm.currentBranch(['XYZ_BRANCH'])
    }

    def "describe with simple prefix"() {
        given:
        def repo = new GitRepo()
        def scm = new GitService(repo.gitTree)
        def desc1
        def desc2
        repo.init()

        when:
        repo.commit('a')
        desc1 = scm.describe('v')
        desc2 = scm.describe('w')

        then:
        assert desc1.closestTag == null
        assert desc1.distance == null
        assert desc1.dirty == false
        assert desc1.hash == '06ca36c'

        and:
        assert desc2.closestTag == null
        assert desc2.distance == null
        assert desc2.dirty == false
        assert desc2.hash == '06ca36c'

        when:
        repo.tag('v1.2.3')
        desc1 = scm.describe('v')
        desc2 = scm.describe('w')

        then:
        assert desc1.closestTag == 'v1.2.3'
        assert desc1.distance == 0
        assert desc1.dirty == false
        assert desc1.hash == '06ca36c'

        and:
        assert desc2.closestTag == null
        assert desc2.distance == null
        assert desc2.dirty == false
        assert desc2.hash == '06ca36c'

        when:
        repo.commit('b')
        desc1 = scm.describe('v')
        desc2 = scm.describe('w')

        then:
        assert desc1.closestTag == 'v1.2.3'
        assert desc1.distance == 1
        assert desc1.dirty == false
        assert desc1.hash == '0997e80'

        and:
        assert desc2.closestTag == null
        assert desc2.distance == null
        assert desc2.dirty == false
        assert desc2.hash == '0997e80'
    }
}
