package xyz.errorist.versioneer

class VersionProxy {

    private VersioneerExtension ext

    VersionProxy(VersioneerExtension ext) {
        this.ext = ext
    }

    String toString() {
        switch(ext.flavor) {
            case 'maven':
                return ext.info.maven
            case 'rpm':
                return ext.info.rpm
            default:
                return ext.info.semver
        }
    }

    Boolean equals(String version) {
        version == toString()
    }
}
