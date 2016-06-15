package xyz.errorist.versioneer

import org.gradle.api.Project


class GitRepo {

    private File gitTree
    private Integer fileNumber = 0

    GitRepo() {
        this.gitTree = File.createTempDir('git', '') as File
    }

    GitRepo(File gitTree) {
        this.gitTree = gitTree
    }

    GitRepo(Project project) {
        this.gitTree = project.projectDir
    }

    void init() {
        execute(['git', 'init'])
    }

    String commit(String txt, String filename = null) {
        filename = filename ?: "commit-${fileNumber++}.txt"
        new File(gitTree, filename).text = "${txt}"
        execute(['git', 'add', filename])
        execute(['git', 'commit', '-m', "commited $filename changes"])
        filename
    }

    void branch(String name) {
        execute(['git', 'checkout', '-b', name])
    }

    void tag(String name) {
        execute(["git", "tag", "-a", name, "-m", name])
    }

    void close() {
        execute(["rm", "-rf", "commit-*.txt"])
        execute(["rm", "-rf", ".git"])
    }

    private execute(cmd) {
        def envs = [
            'GIT_AUTHOR_NAME="Sammy Cobol"',
            'GIT_AUTHOR_EMAIL="<sammy.cobol@example.com>"',
            'GIT_AUTHOR_DATE="Sat, 24 Nov 1973 19:01:02 +0200"',
            'GIT_COMMITTER_NAME="Fred Foobar"',
            'GIT_COMMITTER_EMAIL="<fred.foobar@example.com>"',
            'GIT_COMMITTER_DATE="Sat, 24 Nov 1973 19:11:22 +0200"'
        ]
        Command.execute(cmd, envs, gitTree).with {
            if (exit) {
                throw new Exception("error $exit: $stderr")
            }
            stdout
        }
    }
}
