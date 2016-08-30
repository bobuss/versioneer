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

There are 3 types of branchs:

- release branch, which are found by this regex ``release[/-](?<serie>.+)``
- feature branch, which are found by this regex ``feature[/-](?<serie>.+)``
- the others

.. note::

    Release branches should be in the form::

      release/MAJOR[.MINOR[.PATCH]]

    And their tags should be in the form::

      vMAJOR[.MINOR[.PATCH]]-PRERELEASE

    This kind of branch naming is allowed but may result in clumsy versions::

      release/MAJOR[.MINOR[.PATCH]]-PRERELEASE

Examples
--------

with git history::

  * e585849 - (HEAD -> release/0.x, tag: v0.0.1)
  * 2d5214c -
  * 07cc598 -

it will be parsed as:

+----------------------------+-------------+
| versioneer.info.branch     | release/0.x |
+----------------------------+-------------+
| versioneer.info.type       | release     |
+----------------------------+-------------+
| versioneer.info.serie      | 0.x         |
+----------------------------+-------------+
| versioneer.info.closestTag | 0.0.1       |
+----------------------------+-------------+
| versioneer.info.distance   | 0           |
+----------------------------+-------------+
| versioneer.info.hash       | e585849     |
+----------------------------+-------------+
| versioneer.info.semver     | 0.0.1       |
+----------------------------+-------------+
| versioneer.info.maven      | 0.0.1       |
+----------------------------+-------------+
| versioneer.info.rpm        | 0.0.1-1     |
+----------------------------+-------------+

with git history::

  * e754a0b - (HEAD -> release/0.x)
  * e585849 - (tag: v0.0.1)
  * 2d5214c -
  * 07cc598 -

it will be parsed as:

+----------------------------+--------------------------+
| versioneer.info.branch     | release/0.x              |
+----------------------------+--------------------------+
| versioneer.info.type       | release                  |
+----------------------------+--------------------------+
| versioneer.info.serie      | 0.x                      |
+----------------------------+--------------------------+
| versioneer.info.closestTag | 0.0.1                    |
+----------------------------+--------------------------+
| versioneer.info.distance   | 1                        |
+----------------------------+--------------------------+
| versioneer.info.hash       | e754a0b                  |
+----------------------------+--------------------------+
| versioneer.info.semver     | 0.0.1+post.1.e754a0b     |
+----------------------------+--------------------------+
| versioneer.info.maven      | 0.1.0-ALPHA-0-SNAPSHOT   |
+----------------------------+--------------------------+
| versioneer.info.rpm        | 0.0.1-1.post.1.e754a0b   |
+----------------------------+--------------------------+


.. _SemVer: http://semver.org
.. _Maven: https://docs.oracle.com/middleware/1212/core/MAVEN/maven_version.htm
.. _RPM: https://fedoraproject.org/wiki/Packaging:NamingGuidelines
