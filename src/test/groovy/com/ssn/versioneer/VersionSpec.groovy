package com.ssn.versioneer

import com.ssn.versioneer.versions.SemanticVersion
import spock.lang.*


class VersionSpec extends Specification {

    def "semver from string"() {
        expect:
        def result = args as SemanticVersion
        assert result.version == version
        assert result.stage == stage
        assert result.meta == meta
        assert result.full == full
        assert result == full
        assert result.toString() == full
        assert result.equals(full)

        where:
        args               | version | stage  | meta  | full
        ['1']              | '1.0.0' | null   | null  | '1.0.0'
        ['1.2']            | '1.2.0' | null   | null  | '1.2.0'
        ['1.2.3']          | '1.2.3' | null   | null  | '1.2.3'
        ['1-rc.1']         | '1.0.0' | 'rc.1' | null  | '1.0.0-rc.1'
        ['1.2-rc.1']       | '1.2.0' | 'rc.1' | null  | '1.2.0-rc.1'
        ['1.2.3-rc.1']     | '1.2.3' | 'rc.1' | null  | '1.2.3-rc.1'
        ['1+foo']          | '1.0.0' | null   | 'foo' | '1.0.0+foo'
        ['1.2+foo']        | '1.2.0' | null   | 'foo' | '1.2.0+foo'
        ['1.2.3+foo']      | '1.2.3' | null   | 'foo' | '1.2.3+foo'
        ['1-rc.1+foo']     | '1.0.0' | 'rc.1' | 'foo' | '1.0.0-rc.1+foo'
        ['1.2-rc.1+foo']   | '1.2.0' | 'rc.1' | 'foo' | '1.2.0-rc.1+foo'
        ['1.2.3-rc.1+foo'] | '1.2.3' | 'rc.1' | 'foo' | '1.2.3-rc.1+foo'
    }

    def "semver from spec"() {
        expect:
        def result = args as SemanticVersion
        assert result.version == version
        assert result.stage == stage
        assert result.meta == meta
        assert result == full
        assert result.toString() == full
        assert result.equals(full)

        where:
        args                    | version | stage  | meta | full
        [1, null, null, null]   | '1.0.0' | null   | null | '1.0.0'
        [1, 2, null, null]      | '1.2.0' | null   | null | '1.2.0'
        [1, 2, 3, null]         | '1.2.3' | null   | null | '1.2.3'
        [1, null, null, 'rc.1'] | '1.0.0' | 'rc.1' | null | '1.0.0-rc.1'
        [1, 2, null, 'rc.1']    | '1.2.0' | 'rc.1' | null | '1.2.0-rc.1'
        [1, 2, 3, 'rc.1']       | '1.2.3' | 'rc.1' | null | '1.2.3-rc.1'
    }

    def "semver from next"() {
        def result
        expect:
        def origin = [start] as SemanticVersion
        assert (result = origin.next('major')).full == major
        assert (result = origin.next('minor')).full == minor
        assert (result = origin.next('patch')).full == patch
        assert (result = origin.prerelease('alpha.0').next('pre')).full == pre

        where:
        start | major | minor | patch | pre
        '1' | '2.0.0' | '1.1.0' | '1.0.1' | '1.0.0-alpha.1'
        '1.2' | '2.0.0' | '1.3.0' | '1.2.1' | '1.2.0-alpha.1'
        '1.2.3' | '2.0.0' | '1.3.0' | '1.2.4' | '1.2.3-alpha.1'
        '1-rc.1' | '2.0.0' | '1.1.0' | '1.0.1' | '1.0.0-alpha.1'
        '1.2-rc.1' | '2.0.0' | '1.3.0' | '1.2.1' | '1.2.0-alpha.1'
        '1.2.3-rc.1' | '2.0.0' | '1.3.0' | '1.2.4' | '1.2.3-alpha.1'
        '1+foo' | '2.0.0' | '1.1.0' | '1.0.1' | '1.0.0-alpha.1'
        '1.2+foo' | '2.0.0' | '1.3.0' | '1.2.1' | '1.2.0-alpha.1'
        '1.2.3+foo' | '2.0.0' | '1.3.0' | '1.2.4' | '1.2.3-alpha.1'
        '1-rc.1+foo' | '2.0.0' | '1.1.0' | '1.0.1' | '1.0.0-alpha.1'
        '1.2-rc.1+foo' | '2.0.0' | '1.3.0' | '1.2.1' | '1.2.0-alpha.1'
        '1.2.3-rc.1+foo' | '2.0.0' | '1.3.0' | '1.2.4' | '1.2.3-alpha.1'
    }
}
