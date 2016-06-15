package xyz.errorist.versioneer

import xyz.errorist.versioneer.versions.*
import java.util.regex.Matcher


class ProjectInfo {
    String branch
    String type
    String serie
    String closestTag
    String hash
    Integer distance
    Boolean dirty

    String strategy = 'minor'
    Integer build = 1

    ProjectInfo(String branch,
                String type,
                String serie,
                String closestTag,
                String hash,
                Integer distance,
                Boolean dirty) {

        this.branch = branch
        this.type = type
        this.serie = serie
        this.closestTag = closestTag
        this.hash = hash
        this.distance = distance
        this.dirty = dirty
    }

    SemanticVersion getSemver() {
        def version
        if (closestTag) {
            version = closestTag
            version += (distance > 0) ? "+post.${distance}.${hash}" : ""
        } else if (type == 'release') {
            version = "${serie.replace(/[\.x]+$/, '')}-dev.1+${hash}"
        } else if (type == 'feature') {
            version = "0.0.1-dev.1+${hash}"
        } else {
            version = "0.0.1-dev.1+${hash}"
        }
        new SemanticVersion(version)
    }

    MavenVersion getMaven() {
        if (type in ['release', 'master']) {
            return semver.toMaven(strategy)
        } else if (type == 'feature') {
            return new MavenVersion("0.0-${serie}", true)
        } else {
            return new MavenVersion("0.0-${type}-${hash}")
        }
    }

    RPMVersion getRpm() {
        semver.toRPM(build)
    }
}
