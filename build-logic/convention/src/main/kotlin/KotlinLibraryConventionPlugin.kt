import com.booboot.vndbandroid.applyK2
import com.booboot.vndbandroid.configureKotlinLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            with(pluginManager) {
                apply("java-library")
                apply(libs.findPlugin("kotlin.jvm").get().get().pluginId)
            }

            applyK2()
            extensions.configure<JavaPluginExtension> {
                configureKotlinLibrary(this@with)
            }

            dependencies {
                add("implementation", kotlin("stdlib"))
                add("testImplementation", kotlin("test"))
            }
        }
    }
}
