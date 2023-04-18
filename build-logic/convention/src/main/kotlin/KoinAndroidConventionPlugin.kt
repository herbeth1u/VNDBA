import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class KoinAndroidConventionPlugin : KspConventionPlugin() {
    override fun apply(target: Project) {
        super.apply(target)
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("implementation", libs.findLibrary("koin.android").get())
                add("implementation", libs.findLibrary("koin.android.compose").get())
            }
        }
    }
}
