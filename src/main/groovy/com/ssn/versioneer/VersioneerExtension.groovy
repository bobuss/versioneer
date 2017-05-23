package com.ssn.versioneer

import org.gradle.api.Project

class VersioneerExtension {

    private Project project
    private ProjectInfo info
    private SCMService cached_scm

    /**
     * List of environment vars when detached branch
     */
    public List envs = []
    public Closure branchParser

    /**
     * Prefix of release tag
     */
    public String prefix = 'v'

    /**
     * When upgrading to a new version, which strategy to choose?
     */
    public String strategy = 'minor'

    /**
     * Build number, should given by CI, mostly used by RPM version
     */
    public Integer build = 1

    /**
     * Which version must be used for project.version?
     * one of semver, maven or rpm
     */
    public String flavor = 'semver'

    VersioneerExtension(Project project) {
        this.project = project

        project.rootProject.allprojects {
			version = new VersionProxy(this)
		}
    }

    void reset() {
        info = null
    }

    ProjectInfo getInfo() {
        info = info ?: computeInfo()
    }

    ProjectInfo computeInfo() {
        def branch = scm.currentBranch(this.envs)
        def branchInfo = BranchInfo.parse(branch, branchParser)
        def description

        switch(branchInfo.type) {
            case 'release':
                def prefix = "${prefix ?: ''}${branchInfo.serie ?: ''}"
                description = scm.describe(prefix)
                break
            default:
                description = scm.describe(prefix)
        }

        // baaad, but I won't give a fuck
        if (description?.closestTag) {
            description.closestTag -= prefix ?: ''
        }

        new ProjectInfo(branch, branchInfo, description, strategy, build)
    }

    SCMService getScm() {
        cached_scm = cached_scm ?: new GitService(project)
    }

    void setScm(SCMService scm) {
        cached_scm = scm
    }
}
