package xyz.errorist.versioneer

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
        if (description.contains('-g')) {
            def (a, b, c) = description.split('-', 3)
            closestTag = a
            distance = b as int
            hash = c[1..-1]
        } else {
            closestTag = null
            distance = null
            hash = description
        }
        new Description(dirty, closestTag, distance, hash)
    }
}
