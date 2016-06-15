package xyz.errorist.versioneer

class Command {

    static execute(cmd, envs = null, workdir = null) {
        def stdout = new StringBuilder()
        def stderr = new StringBuilder()
        def exit = cmd.execute(envs, workdir).with {
            consumeProcessOutput(stdout, stderr)
            waitForOrKill(1000)
            exitValue()
        }
        [exit: exit, stdout: stdout, stderr: stderr] as Response
    }

    class Response {
        Integer exit
        String stdout
        String stderr

        String toString() {
            "[exit: $exit, stdout: $stdout, stderr: $stderr]"
        }
    }
}
