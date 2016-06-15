package xyz.errorist.versioneer

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

    String currentBranch() {
        def executor = { cmd ->
            def response = execute(cmd)
            def result = null
            if (response.exit) {
                log.error "${cmd} failed: ${response.stderr}"
            } else if (response.stdout == 'HEAD') {
                log.error "${cmd} returned HEAD"
            } else {
                result = response.stdout.trim()
            }
            result
        }
        def res = [
            { executor(['git', 'rev-parse', '--abbrev-ref', 'HEAD']) },
            { executor(['git', 'name-rev', '--name-only', 'HEAD']) }
        ].findResult { closure -> closure() }
    }

    String currentBranch(String env) {
        def branch = System.env[env]
        branch ?: currentBranch()
    }

    String currentBranch(String[] envs) {
        def branch = envs.findResult { key -> System.env[key] }
        branch ?: currentBranch()
    }

    Description describe(String prefix = null) {
        def cmd = ['git', 'describe', '--tags', '--always', '--long']
        if (prefix) {
            cmd << '--match' << "${prefix}*"
        }

        execute(cmd).with {
            if (exit) {
                throw new RuntimeException("undescridable: ${response.stderr}")
            } else {
                return Description.parse(stdout.trim())
            }
        }
    }

    private execute(cmd) {
        Command.execute(cmd, null, gitTree)
    }
}
