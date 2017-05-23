package com.ssn.versioneer

import com.ssn.versioneer.versions.MavenVersion
import com.ssn.versioneer.versions.RPMVersion
import com.ssn.versioneer.versions.SemanticVersion
import spock.lang.*


class ProjectInfoSpec extends Specification {

    def "test semver"() {
        expect:
        def info = new ProjectInfo('foo', type, serie, closestTag, hash, distance, false)
        assert info.type == type
        assert info.serie == serie
        assert info.closestTag == closestTag
        assert info.hash == hash
        assert info.distance == distance

        and:
        assert info.semver instanceof SemanticVersion
        assert info.semver == expected

        where:
        type      | serie     | closestTag   | hash     | distance | expected

        'release' | '2.0'     | '2.0.1'      | 'abcdef' | 0        | '2.0.1'
        'release' | '2.0'     | '2.0.1'      | 'abcdef' | 1        | '2.0.1+post.1.abcdef'
        'release' | '2.0'     | '2.0.1'      | 'abcdef' | 42       | '2.0.1+post.42.abcdef'
        'release' | '2.0'     | '2.0.1-rc.1' | 'abcdef' | 0        | '2.0.1-rc.1'
        'release' | '2.0'     | '2.0.1-rc.1' | 'abcdef' | 1        | '2.0.1-rc.1+post.1.abcdef'
        'release' | '2.0'     | null         | 'abcdef' | null     | '2.0.0-dev.1+abcdef'

        'feature' | 'foo-2.0' | '2.0.1'      | 'abcdef' | 0        | '2.0.1'
        'feature' | 'foo-2.0' | '2.0.1'      | 'abcdef' | 1        | '2.0.1+post.1.abcdef'
        'feature' | 'foo-2.0' | '2.0.1'      | 'abcdef' | 42       | '2.0.1+post.42.abcdef'
        'feature' | 'foo-2.0' | '2.0.1-rc.1' | 'abcdef' | 0        | '2.0.1-rc.1'
        'feature' | 'foo-2.0' | '2.0.1-rc.1' | 'abcdef' | 1        | '2.0.1-rc.1+post.1.abcdef'
        'feature' | 'foo-2.0' | null         | 'abcdef' | null     | '0.0.1-dev.1+abcdef'

        'develop' | null      | '2.0.1'      | 'abcdef' | 0        | '2.0.1'
        'develop' | null      | '2.0.1'      | 'abcdef' | 1        | '2.0.1+post.1.abcdef'
        'develop' | null      | '2.0.1'      | 'abcdef' | 42       | '2.0.1+post.42.abcdef'
        'develop' | null      | '2.0.1-rc.1' | 'abcdef' | 0        | '2.0.1-rc.1'
        'develop' | null      | '2.0.1-rc.1' | 'abcdef' | 1        | '2.0.1-rc.1+post.1.abcdef'
        'develop' | null      | null         | 'abcdef' | null     | '0.0.1-dev.1+abcdef'

        'master' | null       | '2.0.1'      | 'abcdef' | 0        | '2.0.1'
        'master' | null       | '2.0.1'      | 'abcdef' | 1        | '2.0.1+post.1.abcdef'
        'master' | null       | '2.0.1'      | 'abcdef' | 42       | '2.0.1+post.42.abcdef'
        'master' | null       | '2.0.1-rc.1' | 'abcdef' | 0        | '2.0.1-rc.1'
        'master' | null       | '2.0.1-rc.1' | 'abcdef' | 1        | '2.0.1-rc.1+post.1.abcdef'
        'master' | null       | null         | 'abcdef' | null     | '0.0.1-dev.1+abcdef'
    }

    def "test maven"() {
        expect:
        def info = new ProjectInfo('foo', type, serie, closestTag, hash, distance, false)
        assert info.type == type
        assert info.serie == serie
        assert info.closestTag == closestTag
        assert info.hash == hash
        assert info.distance == distance

        and:
        assert info.maven instanceof MavenVersion
        assert info.maven == expected

        where:
        type      | serie     | closestTag   | hash     | distance | expected

        'release' | '2.0'     | '2.0.1'      | 'abcdef' | 0        | '2.0.1'
        'release' | '2.0'     | '2.0.1'      | 'abcdef' | 1        | '2.1.0-ALPHA-0-SNAPSHOT'
        'release' | '2.0'     | '2.0.1'      | 'abcdef' | 42       | '2.1.0-ALPHA-0-SNAPSHOT'
        'release' | '2.0'     | '2.0.1-rc.1' | 'abcdef' | 0        | '2.0.1-RC-1'
        'release' | '2.0'     | '2.0.1-rc.1' | 'abcdef' | 1        | '2.0.1-RC-2-SNAPSHOT'
        'release' | '2.0'     | null         | 'abcdef' | null     | '2.0.0-ALPHA-0-SNAPSHOT'

        'release' | '2-rc.1'  | null         | 'abcdef' | null     | '2.0.0-RC-1-SNAPSHOT'
        'release' | '2-rc.1'  | '2.0.0-rc.1' | 'abcdef' | null     | '2.0.0-RC-1'

        'feature' | 'foo-2.0' | '2.0.1'      | 'abcdef' | 0        | '0.0-FOO-2-0-SNAPSHOT'
        'feature' | 'foo-2.0' | '2.0.1'      | 'abcdef' | 1        | '0.0-FOO-2-0-SNAPSHOT'
        'feature' | 'foo-2.0' | '2.0.1'      | 'abcdef' | 42       | '0.0-FOO-2-0-SNAPSHOT'
        'feature' | 'foo-2.0' | '2.0.1-rc.1' | 'abcdef' | 0        | '0.0-FOO-2-0-SNAPSHOT'
        'feature' | 'foo-2.0' | '2.0.1-rc.1' | 'abcdef' | 1        | '0.0-FOO-2-0-SNAPSHOT'
        'feature' | 'foo-2.0' | null         | 'abcdef' | null     | '0.0-FOO-2-0-SNAPSHOT'

        'develop' | null      | '2.0.1'      | 'abcdef' | 0        | '0.0-DEVELOP-ABCDEF'
        'develop' | null      | '2.0.1'      | 'abcdef' | 1        | '0.0-DEVELOP-ABCDEF'
        'develop' | null      | '2.0.1'      | 'abcdef' | 42       | '0.0-DEVELOP-ABCDEF'
        'develop' | null      | '2.0.1-rc.1' | 'abcdef' | 0        | '0.0-DEVELOP-ABCDEF'
        'develop' | null      | '2.0.1-rc.1' | 'abcdef' | 1        | '0.0-DEVELOP-ABCDEF'
        'develop' | null      | null         | 'abcdef' | null     | '0.0-DEVELOP-ABCDEF'

        'master' | null       | '2.0.1'      | 'abcdef' | 0        | '2.0.1'
        'master' | null       | '2.0.1'      | 'abcdef' | 1        | '2.1.0-ALPHA-0-SNAPSHOT'
        'master' | null       | '2.0.1'      | 'abcdef' | 42       | '2.1.0-ALPHA-0-SNAPSHOT'
        'master' | null       | '2.0.1-rc.1' | 'abcdef' | 0        | '2.0.1-RC-1'
        'master' | null       | '2.0.1-rc.1' | 'abcdef' | 1        | '2.0.1-RC-2-SNAPSHOT'
        'master' | null       | null         | 'abcdef' | null     | '0.0.1-ALPHA-0-SNAPSHOT'
    }

    def "test rpm"() {
        expect:
        def info = new ProjectInfo('foo', type, serie, closestTag, hash, distance, false)
        assert info.type == type
        assert info.serie == serie
        assert info.closestTag == closestTag
        assert info.hash == hash
        assert info.distance == distance

        and:
        assert info.rpm instanceof RPMVersion
        assert info.rpm == expected

        where:
        type      | serie     | closestTag   | hash     | distance | expected

        'release' | '2.0'     | '2.0.1'      | 'abcdef' | 0        | '2.0.1-1'
        'release' | '2.0'     | '2.0.1'      | 'abcdef' | 1        | '2.0.1-1.post.1.abcdef'
        'release' | '2.0'     | '2.0.1'      | 'abcdef' | 42       | '2.0.1-1.post.42.abcdef'
        'release' | '2.0'     | '2.0.1-rc.1' | 'abcdef' | 0        | '2.0.1-0.1.rc.1'
        'release' | '2.0'     | '2.0.1-rc.1' | 'abcdef' | 1        | '2.0.1-0.1.rc.1.post.1.abcdef'
        'release' | '2.0'     | null         | 'abcdef' | null     | '2.0.0-0.1.dev.1.abcdef'

        'release' | '2-rc.1'  | null         | 'abcdef' | null     | '2.0.0-0.1.rc.1.pre.abcdef'
        'release' | '2-rc.1'  | '2.0.0-rc.1' | 'abcdef' | null     | '2.0.0-0.1.rc.1'
        'release' | '2-rc.1'  | '2.0.0-rc.1' | 'abcdef' | 1        | '2.0.0-0.1.rc.1.post.1.abcdef'

        'feature' | 'foo-2.0' | '2.0.1'      | 'abcdef' | 0        | '2.0.1-1'
        'feature' | 'foo-2.0' | '2.0.1'      | 'abcdef' | 1        | '2.0.1-1.post.1.abcdef'
        'feature' | 'foo-2.0' | '2.0.1'      | 'abcdef' | 42       | '2.0.1-1.post.42.abcdef'
        'feature' | 'foo-2.0' | '2.0.1-rc.1' | 'abcdef' | 0        | '2.0.1-0.1.rc.1'
        'feature' | 'foo-2.0' | '2.0.1-rc.1' | 'abcdef' | 1        | '2.0.1-0.1.rc.1.post.1.abcdef'
        'feature' | 'foo-2.0' | null         | 'abcdef' | null     | '0.0.1-0.1.dev.1.abcdef'

        'develop' | null      | '2.0.1'      | 'abcdef' | 0        | '2.0.1-1'
        'develop' | null      | '2.0.1'      | 'abcdef' | 1        | '2.0.1-1.post.1.abcdef'
        'develop' | null      | '2.0.1'      | 'abcdef' | 42       | '2.0.1-1.post.42.abcdef'
        'develop' | null      | '2.0.1-rc.1' | 'abcdef' | 0        | '2.0.1-0.1.rc.1'
        'develop' | null      | '2.0.1-rc.1' | 'abcdef' | 1        | '2.0.1-0.1.rc.1.post.1.abcdef'
        'develop' | null      | null         | 'abcdef' | null     | '0.0.1-0.1.dev.1.abcdef'

        'master' | null       | '2.0.1'      | 'abcdef' | 0        | '2.0.1-1'
        'master' | null       | '2.0.1'      | 'abcdef' | 1        | '2.0.1-1.post.1.abcdef'
        'master' | null       | '2.0.1'      | 'abcdef' | 42       | '2.0.1-1.post.42.abcdef'
        'master' | null       | '2.0.1-rc.1' | 'abcdef' | 0        | '2.0.1-0.1.rc.1'
        'master' | null       | '2.0.1-rc.1' | 'abcdef' | 1        | '2.0.1-0.1.rc.1.post.1.abcdef'
        'master' | null       | null         | 'abcdef' | null     | '0.0.1-0.1.dev.1.abcdef'
    }
}
