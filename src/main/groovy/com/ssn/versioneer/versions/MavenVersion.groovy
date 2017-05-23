package com.ssn.versioneer.versions
import java.util.regex.Matcher


class MavenVersion {

    private String version
    private String qualifier
    private Boolean snapshot

    MavenVersion(String version, Boolean snapshot = null) {
        switch(version) {
            case ~/(?<version>\d+(\.\d+){0,2})-(?<qualifier>.+)/:
                version = Matcher.lastMatcher.group('version')
                qualifier = Matcher.lastMatcher.group('qualifier')
        }
        if (qualifier) {
            qualifier = qualifier.replaceAll(/[\.\/]/, '-').toUpperCase()
        }

        this.version = version
        this.qualifier = qualifier
        this.snapshot = snapshot
    }

    String getVersion() {
        version
    }

    String getQualifier() {
        qualifier
    }

    String getFull() {
        def full = version
        if (qualifier) {
            full += "-$qualifier"
        }
        if (snapshot) {
            full += "-SNAPSHOT"
        }
        full
    }

    Boolean equals(String other) {
        full == other
    }

    String toString() {
        full
    }
}
