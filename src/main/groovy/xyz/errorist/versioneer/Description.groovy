package xyz.errorist.versioneer
import java.util.regex.Matcher


class Description {
    String closestTag
    Integer distance
    String hash
    Boolean dirty

    Description(dirty, closestTag, distance, hash) {
        this.dirty = dirty
        this.closestTag  = closestTag
        this.distance = distance
        this.hash = hash
    }

    static Description parse(String description) {
        def closestTag
        def distance
        def hash
        def dirty

        if (description == null) {
            return new Description()
        }

        dirty = description.endsWith('-dirty')
        if (dirty) {
            description = description[0..-7]
        }

        switch(description) {
            case ~/(?<tag>.+)-(?<distance>\d+)-g(?<hash>[0-9a-f]+)/:
                closestTag = Matcher.lastMatcher.group('tag')
                distance = Matcher.lastMatcher.group('distance') as int
                hash = Matcher.lastMatcher.group('hash')
                break
            default:
                hash = description
        }

        new Description(dirty, closestTag, distance, hash)
    }
}
