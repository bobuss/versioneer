package com.ssn.versioneer.versions

import spock.lang.*


class SemanticVersionSpec extends Specification {

    def "test semver"() {
        expect:
        def semver = new SemanticVersion(serie)

        and:
        assert semver instanceof SemanticVersion
        assert semver.major == major
        assert semver.minor == minor
        assert semver.patch == patch
        assert semver.prerelease == prerelease
        assert semver.meta == meta
        assert semver.stage == stage
        assert semver.dev == dev

        where:
        serie              | major | minor | patch | prerelease   | meta  | stage  | dev
        '1'                | 1     | 0     | 0     | null         | null  | null   | null
        '2.1'              | 2     | 1     | 0     | null         | null  | null   | null
        '3.2.1'            | 3     | 2     | 1     | null         | null  | null   | null
        '1-foo'            | 1     | 0     | 0     | 'foo'        | null  | null   | null
        '2.1-foo'          | 2     | 1     | 0     | 'foo'        | null  | null   | null
        '3.2.1-foo'        | 3     | 2     | 1     | 'foo'        | null  | null   | null
        '1+bar'            | 1     | 0     | 0     | null         | 'bar' | null   | null
        '2.1+bar'          | 2     | 1     | 0     | null         | 'bar' | null   | null
        '3.2.1+bar'        | 3     | 2     | 1     | null         | 'bar' | null   | null
        '1-foo+bar'        | 1     | 0     | 0     | 'foo'        | 'bar' | null   | null
        '2.1-foo+bar'      | 2     | 1     | 0     | 'foo'        | 'bar' | null   | null
        '3.2.1-foo+bar'    | 3     | 2     | 1     | 'foo'        | 'bar' | null   | null
        '1-rc.1'           | 1     | 0     | 0     | 'rc.1'       | null  | 'rc.1' | null
        '2.1-rc.1'         | 2     | 1     | 0     | 'rc.1'       | null  | 'rc.1' | null
        '3.2.1-rc.1'       | 3     | 2     | 1     | 'rc.1'       | null  | 'rc.1' | null
        '1-dev.1'          | 1     | 0     | 0     | 'dev.1'      | null  | null   | 'dev.1'
        '2.1-dev.1'        | 2     | 1     | 0     | 'dev.1'      | null  | null   | 'dev.1'
        '3.2.1-dev.1'      | 3     | 2     | 1     | 'dev.1'      | null  | null   | 'dev.1'
        '1-rc.1.dev.1'     | 1     | 0     | 0     | 'rc.1.dev.1' | null  | 'rc.1' | 'dev.1'
        '2.1-rc.1.dev.1'   | 2     | 1     | 0     | 'rc.1.dev.1' | null  | 'rc.1' | 'dev.1'
        '3.2.1-rc.1.dev.1' | 3     | 2     | 1     | 'rc.1.dev.1' | null  | 'rc.1' | 'dev.1'
    }
}
