Gradle versioneer plugin
========================

This plugin allow you to define the version of your projet from git references.

These flavors are SemVer_, Maven_ version and RPM_ version.

Configuration::

    versioneer {
      envs = [] // get branch name from these environment variables
      branchParser = { branch -> ['type', 'serie']} // custom parsing branch
      prefix = 'v' // release tag must be prefixed by this string
      strategy = 'minor' // default strategy when upgrading version is involved
      build = 1 // build number given by CI... mostly used by RPM Version
      flavor = 'semver' // which flavor must be exposed into project.version?
    }

Branch naming
-------------

Their are 3 types of branchs:

- release branch, which are found by this regex ``release[/-](?<serie>.+)``
- feature branch, which are found by this regex ``feature[/-](?<serie>.+)``
- the others

.. _SemVer: http://semver.org
.. _Maven: https://docs.oracle.com/middleware/1212/core/MAVEN/maven_version.htm
.. _RPM: https://fedoraproject.org/wiki/Packaging:NamingGuidelines
