package xyz.errorist.versioneer.versions
import java.util.regex.Matcher


class SemanticVersion {

    private Integer major = 0
    private Integer minor = 0
    private Integer patch = 0
    private String prerelease
    private String meta

    private static final STAGE = 1
    private static final DEV = 2
    private static final POST = 4

    SemanticVersion(Integer major, Integer minor, Integer patch, String prerelease) {
        this.major = major ?: 0
        this.minor = minor ?: 0
        this.patch = patch ?: 0
        this.prerelease = prerelease
    }

    SemanticVersion(String version) {
        switch(version) {
            case ~/(?<version>.+)-(?<prerelease>.+)\+(?<meta>.+)/:
                version = Matcher.lastMatcher.group('version')
                prerelease = Matcher.lastMatcher.group('prerelease')
                meta = Matcher.lastMatcher.group('meta')
                break
            case ~/(?<version>.+)\+(?<meta>.+)/:
                version = Matcher.lastMatcher.group('version')
                meta = Matcher.lastMatcher.group('meta')
                break
            case ~/(?<version>.+)-(?<prerelease>.+)/:
                version = Matcher.lastMatcher.group('version')
                prerelease = Matcher.lastMatcher.group('prerelease')
                break
        }

        switch(version) {
            case ~/(?<major>\d+)\.(?<minor>\d+)\.(?<patch>\d+)/:
                major = Matcher.lastMatcher.group('major') as int
                minor = Matcher.lastMatcher.group('minor') as int
                patch = Matcher.lastMatcher.group('patch') as int
                break
            case ~/(?<major>\d+)\.(?<minor>\d+)/:
                major = Matcher.lastMatcher.group('major') as int
                minor = Matcher.lastMatcher.group('minor') as int
                break
            case ~/(?<major>\d+)/:
                major = Matcher.lastMatcher.group('major') as int
                break
            default:
                throw new Exception("Cannot parse version")
        }
    }

    /**
     * Returns the version part
     *
     * For example, given 1.2.3-alpha.1.dev.2+post.3.foo,
     * result is 1.2.3
     */
    String getVersion() {
        "${major}.${minor}.${patch}"
    }

    /**
     * Returns the stage part
     *
     * For example, given 1.2.3-alpha.1.dev.2+post.3.foo,
     * result is alpha.1
     */
    String getStage() {
        switch(prerelease) {
            case ~/(?<stage>(alpha|beta|rc)\.\d+)(?<dev>dev\.\d+)?/:
                return Matcher.lastMatcher.group('stage')
        }
    }

    /**
     * Returns the dev part
     *
     * For example, given 1.2.3-alpha.1.dev.2+post.3.foo,
     * result is dev.2
     */
    String getDev() {
        switch(prerelease) {
            case ~/(?<stage>(alpha|beta|rc)\.\d+)?(?<dev>dev\.\d+)/:
                return Matcher.lastMatcher.group('dev')
        }
    }

    /**
     * Returns the post part
     *
     * For example, given 1.2.3-alpha.1.dev.2+post.3.foo,
     * result is post.3
     */
    String getPost() {
        switch(meta) {
            case ~/(?<post>post\.\d+).*/:
                return Matcher.lastMatcher.group('post')
        }
    }

    /**
     * Returns the clean part
     *
     * For example, given 1.2.3-alpha.1.dev.2+post.3.foo,
     * result is 1.2.3-alpha.1.dev.2
     */
    String getClean() {
        def clean = version
        if (prerelease) {
            clean += "-$prerelease"
        }
        clean
    }

    /**
     * Returns the clean part
     *
     * For example, given 1.2.3-alpha.1.dev.2+post.3.foo,
     * result is 1.2.3-alpha.1.dev.2+post.3.foo
     */
    String getFull() {
        def full = clean
        if (meta) {
            full += "+$meta"
        }
        full
    }

    Boolean equals(String other) {
        full == other
    }

    String toString() {
        full
    }

    /**
     * Returns a maven friendly version
     */
    MavenVersion toMaven(String strategy) {
        if (is(STAGE | POST)) {
            def version = next('pre').clean
            return new MavenVersion(version, true)
        } else if (is(POST)) {
            def version = next(strategy).prerelease('alpha.0').clean
            return new MavenVersion(version, true)
        } else if (is(DEV | STAGE)) {
            def version = next('pre').clean
            return new MavenVersion(version, true)
        } else if (is(DEV)) {
            def version = prerelease('alpha.0').clean
            return new MavenVersion(version, true)
        }
        new MavenVersion(clean)
    }

    /**
     * Returns a rpm friendly version
     */
    RPMVersion toRPM(Integer build) {
        new RPMVersion(full, build)
    }

    /**
     * Check if version is dev, stage, pre...
     */
    private Boolean is(Integer lookup) {
        def response = true
        if (STAGE & lookup) {
            response &= stage ? true : false
        }
        if (DEV & lookup) {
            response &= dev ? true : false
        }
        if (POST & lookup) {
            response &= post ? true : false
        }
        response
    }

    /**
     * Returns next logical version
     *
     * For example, given 1.2.3-alpha.1.dev.2+post.3.foo then:
     *   - next major is 2.0.0
     *   - next minor is 1.3.0
     *   - next patch is 1.2.4
     *   - next pre is 1.2.3-alpha.1.dev.3
     */
    SemanticVersion next(String strategy) {
        if (strategy == 'major') {
            return new SemanticVersion(major + 1, 0, 0, null)
        } else if (strategy == 'minor') {
            return new SemanticVersion(major, minor + 1, 0, null)
        } else if (strategy == 'patch') {
            return new SemanticVersion(major, minor, patch + 1, null)
        } else if (strategy == 'pre') {
            def prerelease = prerelease.replaceAll(/(\d+)$/, { (it[0] as int) + 1 })
            return new SemanticVersion(major, minor, patch, prerelease)
        }
        throw new Exception("Unkown strategy $strategy")
    }

    SemanticVersion prerelease(String prerelease) {
        return new SemanticVersion(major, minor, patch, prerelease)
    }
}
