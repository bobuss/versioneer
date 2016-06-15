package xyz.errorist.versioneer

import org.gradle.api.Project

class VersioneerExtension {

    private Project project
    private ProjectInfo info
    private SCMService cached_scm

    public List envs = []
    public Closure branchParser

    VersioneerExtension(Project project) {
        this.project = project
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
        if (description.closestTag) {
            description.closestTag -= prefix ?: ''
        }

        new ProjectInfo(branch: branch,
                        type: branchInfo.type,
                        serie: branchInfo.serie,
                        closestTag: description.closestTag,
                        hash: description.hash,
                        distance: description.distance,
                        dirty: description.dirty)
    }

    SCMService getScm() {
        cached_scm = cached_scm ?: new GitService(project)
    }

    void setScm(SCMService scm) {
        cached_scm = scm
    }
}
