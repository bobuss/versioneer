package xyz.errorist.versioneer

import xyz.errorist.versioneer.versions.*
import java.util.regex.Matcher
import org.slf4j.Logger
import org.slf4j.LoggerFactory


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

    private final Logger log = LoggerFactory.getLogger('versioneer')

    ProjectInfo(String branch,
                BranchInfo branchInfo,
                Description description,
                String strategy,
                Integer build) {
        this.branch = branch
        this.type = branchInfo.type
        this.serie = branchInfo.serie
        this.closestTag = description.closestTag
        this.hash = description.hash
        this.distance = description.distance
        this.dirty = description.dirty
        this.strategy = strategy
        this.build = build
    }

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
            version = "${serie.replace(/[\.x]+$/, '')}"
            if (serie.indexOf('-') > 0) {
                def mmp = serie.substring(0, serie.indexOf('-'))
                /* TODO ProjectInfo doesn't know branch/tag naming patterns. */
                log.info("Prerelease in branch name may result in clumsy versions.\nConsider to rename your branch `release/${mmp}` and tag it `v${version}`.")
                version += "+pre.${hash}"
            } else {
                version += "-dev.1+${hash}"
            }
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
