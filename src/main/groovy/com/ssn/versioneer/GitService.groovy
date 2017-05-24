package com.ssn.versioneer

import groovy.util.logging.Slf4j
import org.gradle.api.Project


@Slf4j
class GitService extends SCMService {

    private File gitTree

    GitService(Project project) {
        this.gitTree = project.projectDir
    }

    GitService(File projectDir) {
        this.gitTree = projectDir
    }

    /**
     * Tells if repository has been isInitialized
     */
    Boolean isInitialized() {
        def file = new File(gitTree, '.git')
        file.exists()
    }

    /**
     * Returns the current branch
     */
    String currentBranch() {
        if (!isInitialized()) {
            throw new GitCommandException("Not a git repository")
        }

        def executor = { cmd ->
            execute(cmd).with {
                def branch = stdout.trim()
                if (exit) {
                    this.log.error "${cmd} failed [${exit}]: ${stderr}"
                } else if (branch == 'HEAD') {
                    this.log.error "${cmd} returned HEAD"
                    return null
                } else {
                    return branch ?: null
                }
            }
        }
        def branch = [
            { executor(['git', 'rev-parse', '--abbrev-ref', 'HEAD']) },
            { executor(['git', 'name-rev', '--name-only', 'HEAD']) }
        ].findResult { closure -> closure() }
        if (branch == null) {
            throw new GitCommandException("branch as no name")
        }
        branch
    }

    String currentBranch(String env) {
        def branch = System.env[env]
        branch ?: currentBranch()
    }

    String currentBranch(List envs) {
        def branch = envs.findResult { key -> System.env[key] }
        branch ?: currentBranch()
    }

    /**
     * Describe current commit
     */
    Description describe(String prefix) {
        if (!isInitialized()) {
            throw new GitCommandException("Not a git repository")
        }

        def cmd = ['git', 'describe', '--tags', '--always', '--long']
        if (prefix) {
            cmd << '--match' << "${prefix}*"
        }

        execute(cmd).with {
            if (exit) {
                throw new GitCommandException("undescridable: ${stderr}")
            }
            else if (stdout == '') {
                throw new GitCommandException("empty describe")
            }
            else {
                return Description.parse(stdout.trim())
            }
        }
    }

    private execute(cmd) {
        Command.execute(cmd, null, gitTree)
    }
}
