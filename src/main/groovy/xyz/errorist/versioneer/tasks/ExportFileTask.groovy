package xyz.errorist.versioneer.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ExportFileTask extends DefaultTask {

    /**
     * File to write the information info.
     * Defaults to <code>new File(project.buildDir, 'versioneer.properties')</code>.
     */
    File file = new File(project.buildDir, 'versioneer.properties')

    /**
     * Prefix to apply.
     * Defauts to <code>VERSIONEER_</code>
     */
    String prefix = 'VERSIONEER_'

    ExportFileTask() {
       description = 'Export inferred information into file'
       group = 'Versioneer'
    }

    File setFile(String path) {
        file = new File(path)
    }

    @TaskAction
    def run() {
        def clean = { txt ->
            switch(txt) {
                case null:
                    return ''
                case Boolean:
                    return txt ? 0 : 1
            }
            txt
        }

        file.parentFile.with {
            exists() ?: mkdirs()
        }
        project.versioneer.info.with {
            file << "${prefix}BUILD=${clean(build)}\n"
            file << "${prefix}TYPE=${clean(type)}\n"
            file << "${prefix}SERIE=${clean(serie)}\n"
            file << "${prefix}BRANCH=${clean(branch)}\n"
            file << "${prefix}CLOSEST_TAG=${clean(closestTag)}\n"
            file << "${prefix}DISTANCE=${clean(distance)}\n"
            file << "${prefix}HASH=${clean(hash)}\n"

            maven.with {
                file << "${prefix}MAVEN=${clean(full)}\n"
                file << "${prefix}MAVEN_VERSION=${clean(version)}\n"
                file << "${prefix}MAVEN_QUALIFIER=${clean(qualifier)}\n"
                file << "${prefix}MAVEN_SNAPSHOT=${clean(snapshot)}\n"
            }

            semver.with {
                file << "${prefix}SEMVER=${clean(full)}\n"
                file << "${prefix}SEMVER_VERSION=${clean(version)}\n"
                file << "${prefix}SEMVER_PRERELEASE=${clean(prerelease)}\n"
                file << "${prefix}SEMVER_META=${clean(meta)}\n"
            }

            rpm.with {
                file << "${prefix}RPM=${clean(full)}\n"
                file << "${prefix}RPM_VERSION=${clean(version)}\n"
                file << "${prefix}RPM_BUILD=${clean(build)}\n"
            }
        }
    }
}
