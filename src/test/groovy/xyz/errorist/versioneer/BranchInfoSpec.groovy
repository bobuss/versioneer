package xyz.errorist.versioneer

import spock.lang.*


class BranchInfoSpec extends Specification {
    def "default parsing"() {
        expect:
        def result = BranchInfo.parse(branch)
        assert result.type == type
        assert result.serie == serie

        where:
        branch        | type     | serie
        'master'      | 'master' | null
        'release/foo' | 'release' | 'foo'
        'release-foo' | 'release' | 'foo'
        'feature/foo' | 'feature' | 'foo'
        'feature-foo' | 'feature' | 'foo'
    }
    def "overwritten parsing"() {
        expect:
        def result = BranchInfo.parse(branch, { branch -> ["foo", "bar"] })
        assert result.type == type
        assert result.serie == serie

        where:
        branch        | type  | serie
        'master'      | 'foo' | 'bar'
        'release/foo' | 'foo' | 'bar'
        'release-foo' | 'foo' | 'bar'
        'feature/foo' | 'foo' | 'bar'
        'feature-foo' | 'foo' | 'bar'
    }
    def "almost parsing"() {
        expect:
        def result = BranchInfo.parse(branch, { branch -> ["foo/bar"] })
        assert result.type == type
        assert result.serie == serie

        where:
        branch        | type      | serie
        'master'      | 'foo/bar' | 'master'
        'release/foo' | 'foo/bar' | 'release/foo'
        'release-foo' | 'foo/bar' | 'release-foo'
        'feature/foo' | 'foo/bar' | 'feature/foo'
        'feature-foo' | 'foo/bar' | 'feature-foo'
    }
    def "type parsing"() {
        expect:
        def result = BranchInfo.parse(branch, { "baz" })
        assert result.type == type
        assert result.serie == serie

        where:
        branch        | type  | serie
        'master'      | 'baz' | 'master'
        'release/foo' | 'baz' | 'release/foo'
        'release-foo' | 'baz' | 'release-foo'
        'feature/foo' | 'baz' | 'feature/foo'
        'feature-foo' | 'baz' | 'feature-foo'
    }

    def "broken parsing"() {
        when:
        BranchInfo.parse('foo', { response })

        then:
        thrown type

        where:
        response | type
        false    | ParserException
        true     | ParserException
        42       | ParserException
    }
}
