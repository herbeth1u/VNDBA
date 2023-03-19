import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * Apply Kotlin Symbol Processing (KSP) plugin
 * @see [KSP](https://github.com/google/ksp)
 *
 */
open class KspConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            with(pluginManager) {
                apply(libs.findPlugin("ksp").get().get().pluginId)
            }
        }
    }
}
