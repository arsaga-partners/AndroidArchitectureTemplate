package script

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler


object ModuleExtension {

    /**
     * モジュール一覧から特定のレイヤーのものだけをフィルタリングして返す関数
     */
    internal fun byLayerModuleList(layerType: ModuleStructure.LayerType): List<ProjectModule.Type> =
        ProjectModule.Type
            .values().toList()
            .filter { it.name.contains(layerType.name) }

    internal fun findModuleType(project: Project): ProjectModule.Type? {
        val projectName = convertEnumName(project)
        return ProjectModule.Type.values()
            .find { projectName == it.name }
    }

    internal fun eachAllModule(
        excludedModuleSet: Set<ProjectModule.Type>,
        importAction: (ProjectModule.Type) -> Unit
    ) {
        ProjectModule.Type.values().toSet()
            .minus(excludedModuleSet).forEach {
                importAction(it)
            }
    }

    internal fun implDomainModule(
        dependencyHandler: DependencyHandler,
        moduleType: ProjectModule.Type
    ) {
        if (isUnnecessaryImplModule(moduleType)) return
        val needLayerModuleList = filteringNeedLayer(moduleType)
        ProjectModule.Type.values()
            .filter { it.domainType == moduleType.domainType && it.layerType in needLayerModuleList }
            .forEach {
                dependencyHandler.impl(it)
            }
        sameLayerChildModule(moduleType).forEach {
            dependencyHandler.api(it)
        }
        sameLayerCoreModule(moduleType).forEach {
            dependencyHandler.api(it)
        }
    }

    /**
     * appとドメインがcoreのモジュールはレイヤー構造が変わるので弾く
     */
    private fun isUnnecessaryImplModule(
        moduleType: ProjectModule.Type
    ): Boolean = moduleType.domainType in listOf(null)

    /**
     * 渡されたモジュールのレイヤーに必要とされるレイヤーのリストをアーキテクチャ構成図にしたがって返す
     * appとgatewayはドメインごとの区切りがないので不要になる
     */
    private fun filteringNeedLayer(
        moduleType: ProjectModule.Type
    ): List<ModuleStructure.LayerType> = when (moduleType.layerType) {
        ModuleStructure.LayerType.view ->
            listOf(
                ModuleStructure.LayerType.layout,
                ModuleStructure.LayerType.viewModel,
                ModuleStructure.LayerType.useCase,
                ModuleStructure.LayerType.entity
            )
        ModuleStructure.LayerType.layout ->
            listOf(ModuleStructure.LayerType.entity)
        ModuleStructure.LayerType.viewModel ->
            listOf(
                ModuleStructure.LayerType.repository,
                ModuleStructure.LayerType.useCase,
                ModuleStructure.LayerType.entity
            )
        ModuleStructure.LayerType.repository ->
            listOf(
                ModuleStructure.LayerType.useCase,
                ModuleStructure.LayerType.entity
            )
        ModuleStructure.LayerType.useCase ->
            listOf(ModuleStructure.LayerType.entity)
        else -> listOf()
    }

    private fun sameLayerCoreModule(
        moduleType: ProjectModule.Type
    ): List<ProjectModule.Type> = ProjectModule.Type.values()
        .filter {
            it.name.endsWith("_core") &&
                    moduleType != it &&
                    moduleType.name.startsWith(it.name.substringBefore("_core"))
        }

    private fun sameLayerChildModule(
        moduleType: ProjectModule.Type
    ): List<ProjectModule.Type> = ProjectModule.Type.values()
        .filter {
            moduleType != it &&
                    it.name.startsWith(moduleType.name)
        }

    private fun convertEnumName(project: Project): String = project.path
        .replace(":", "_")

    fun convertModulePath(moduleType: ProjectModule.Type): String = moduleType.name
        .replace("_", ":")

    internal fun DependencyHandler.api(moduleType: ProjectModule.Type) {
        convertModulePath(moduleType).let { modulePath ->
            println("structure:api => $modulePath")
            add("api", project(mapOf("path" to modulePath)))
        }
    }

    internal fun DependencyHandler.impl(moduleType: ProjectModule.Type) {
        convertModulePath(moduleType).let { modulePath ->
            println("structure:impl => $modulePath")
            add("implementation", project(mapOf("path" to modulePath)))
        }
    }
}
