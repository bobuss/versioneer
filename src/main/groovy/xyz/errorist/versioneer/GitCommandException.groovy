package xyz.errorist.versioneer


class GitCommandException extends RuntimeException {
    GitCommandException(String message) {
        super(message)
    }
}
