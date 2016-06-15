package xyz.errorist.versioneer.versions
import java.util.regex.Matcher


class RPMVersion {
    private String version
    private String release
    private Integer build

    RPMVersion(String version, Integer build) {
        switch(version) {
            case ~/(?<version>\d+(\.\d+){0,2})-(?<release>.+)/:
                version = Matcher.lastMatcher.group('version')
                release = Matcher.lastMatcher.group('release')
            case ~/(?<version>\d+(\.\d+){0,2})\+(?<release>.+)/:
                version = Matcher.lastMatcher.group('version')
                release = Matcher.lastMatcher.group('release')
        }
        if (release) {
            release = release.replaceAll(/\+/, '.')
        }
        this.version = version
        this.release = release
        this.build = build
    }

    String getVersion() {
        version
    }

    String getBuild() {
        switch(release) {
            case ~/(alpha|beta|rc|dev)\.\d+.*/:
                return "0.${build}.${release}"
            case null:
                return "${build}"
            default:
                return "${build}.${release}"
        }
    }

    String getFull() {
        "${version}-${getBuild()}"
    }

    Boolean equals(String other) {
        full == other
    }

    String toString() {
        full
    }
}
