package script

import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

object ScaffoldExtension {

    /**
     * 各モジュールがどのレイヤーにいるのか分類するプロパティ
     */
    private val moduleLayerCategory = mapOf(
        "_presentation" to listOf(
            ModuleStructure.LayerType.view,
            ModuleStructure.LayerType.layout,
            ModuleStructure.LayerType.viewModel,
        ),
        "_dataStore" to listOf(
            ModuleStructure.LayerType.repository,
        ),
        "_domain" to listOf(
            ModuleStructure.LayerType.useCase,
            ModuleStructure.LayerType.entity,
        )
    ).entries

    /**
     * settings.gradle.ktsから登録済みのモジュール名一覧を取得する関数(最初からあるもの以外は全て自動生成されている想定)
     */
    fun settingModuleNameList(projectPath: String) = projectPath
        .run(::FileReader).buffered().use { reader ->
            reader.lines()?.filter { it.contains("include") }
                ?.map { it.removeSurrounding("include(\"", "\")") }
                ?.toList()
        }

    /**
     * 新たに追加されたモジュールをsettings.gradle.ktsに追記する関数
     */
    fun updateSettingModule(
        projectPath: String,
        increasedModuleNameList: List<String>
    ) {
        FileOutputStream(File(projectPath), true).bufferedWriter().use { writer ->
            increasedModuleNameList.forEach {
                writer.append("include(\"$it\")")
                writer.newLine()
            }
        }
    }

    /**
     * モジュール間の関係性を表現するenumにモジュールを追加する関数
     * core以外は全て自動生成されている想定で洗い替え方式で記入している
     * ("//autoGen"のコメントをフラグにして変更管理している)
     */
    fun updateProjectModuleType(
        projectModuleTypeDirectoryPath: String
    ) {
        val newProjectModuleContents = projectModuleTypeDirectoryPath
            .run(::FileReader).buffered().use { reader ->
                reader.readLines().toMutableList().also { codeList ->
                    codeList.removeIf { it.contains("CreateType.AUTO") }
                    codeList.indexOfFirst { it.contains(";") }
                        .run { codeList.addAll(this, needProjectModuleList()) }
                }
            }
        PrintWriter(File(projectModuleTypeDirectoryPath)).use { writer ->
            writer.print(newProjectModuleContents.joinToString("\n"))
        }
    }

    /**
     * enumから算出された必要なモジュール一覧とsettings.gradle.ktsのモジュール一覧を比べて
     * 足りていないモジュールのリストを算出する関数
     */
    fun missingModuleNameList(
        settingGradleModuleNameList: List<String>
    ): List<String> = needModuleNameList()
        .map { it.name.replace("_", ":") }
        .filter { it !in settingGradleModuleNameList }

    /**
     * 新しいモジュールを作成する関数を返す関数
     * 呼び出し元でcoroutineで並列実行するために直接実行するのではなく実行内容を返すように作ってある
     */
    fun generateNewModule(
        projectPath: String,
        increasedModuleNameList: List<String>
    ): List<() -> List<() -> Unit>> = allCopy(
        projectPath,
        getTemplateDirectoryPathList(projectPath),
        getAllNewDirectoryPathList(projectPath, increasedModuleNameList)
    )

    /**
     * 増やすべきモジュールのパス(String)のリストを元にディレクトリを作成し、
     * Path型に変換してリストで返す関数
     */
    private fun getAllNewDirectoryPathList(
        projectPath: String,
        increasedModuleNameList: List<String>
    ): List<Path> = increasedModuleNameList.mapNotNull { moduleName ->
        runCatching {
            projectPath.plus(moduleName.replace(":", "/"))
                .run(Paths::get)
                ?.run(Files::createDirectories)
        }.getOrNull()
    }

    /**
     * テンプレートの元になるファイルが入っているディレクトリを返す関数
     */
    private fun getTemplateDirectoryPathList(projectPath: String): File = projectPath
        .plus("/moduleTemplate")
        .run(::File)

    /**
     * テンプレートフォルダの中を再帰で走査して各テンプレートファイルをコピーする関数を実行する関する
     */
    private fun allCopy(
        projectPath: String,
        templateDirectory: File,
        exportPathList: List<Path>
    ): List<() -> List<() -> Unit>> = templateDirectory.walkTopDown().map { from ->
        {
            if (from.parent.contains("/moduleTemplate/base")) {
                baseSourceCodeCreate(projectPath, from, exportPathList)
            } else {
                layerSourceCodeCreate(projectPath, from, exportPathList)
            }
        }
    }.toList()

    /**
     * 全階層で共通のファイルをテンプレートからコピーする関数
     */
    private fun baseSourceCodeCreate(
        projectPath: String,
        from: File,
        exportPathList: List<Path>
    ): List<() -> Unit> = exportPathList.map { to ->
        {
            val moduleName = getModuleName(to, projectPath)
            from.absolutePath
                .replace(
                    "moduleTemplate/base",
                    moduleName
                ).run(::File)
                .run { sourceCodeFilePathAdapter(this, moduleName) }
                .run {
                    createNewFile(from, this) { reader ->
                        reader.readLines().map {
                            if (!it.contains("jp.arsaga")) it
                            else moduleName
                                .replace("/", ".")
                                .run { it.replace("jp.arsaga", "jp.arsaga$this") }
                        }
                    }
                }
        }
    }

    /**
     * 各階層ごとに必要なファイルをテンプレートからコピーする関数を
     * 全ドメインで実行する関数
     */
    private fun layerSourceCodeCreate(
        projectPath: String,
        from: File,
        exportPathList: List<Path>
    ): List<() -> Unit> =
        if (from.isDirectory) listOf()
        else {
            val layerNameList = ModuleStructure.LayerType.values().map { it.name }
            val layerName = from.parent.toString()
                .split("/")
                .findLast { it in layerNameList }
            if (layerName == null) listOf()
            else exportPathList.mapNotNull { to ->
                if (!to.toString().contains("/$layerName/")) null
                else {
                    {
                        val moduleName = getModuleName(to, projectPath)
                        val featureName = from.path.toString()
                            .substringAfterLast(layerName)
                            .substringBeforeLast("/")
                        from.absolutePath
                            .replace(
                                "/moduleTemplate/layer",
                                moduleName
                            ).let {
                                createLayerClass(from, it, moduleName, featureName, layerName)
                            }
                    }
                }
            }
        }

    /**
     * 各階層で必要なファイルをコピーする関数
     */
    private fun createLayerClass(
        from: File,
        toPath: String,
        moduleName: String,
        featureName: String,
        layerName: String
    ) {
        val domainName = sliceLastSlashAfter(moduleName)
        convertLayerClassTemplatePath(toPath, domainName).run(::File)
            .run { sourceCodeFilePathAdapter(this, moduleName + featureName) }
            .run {
                createNewFile(from, this) {
                    decorateLayerClassTemplateFile(it, moduleName, layerName, domainName)
                }
            }
    }

    /**
     * テンプレートフォルダのレイヤー毎に分類されているファイルのパスを
     * 実際に各ドメインで使える形式に置換する関数
     */
    private fun convertLayerClassTemplatePath(absolutePath: String, domainName: String): String =
        absolutePath
            .replace(
                """src/main/java/.+/""".toRegex(),
                "src/main/java/"
            ).let {
                val templateFileName = it.substringAfterLast("/")
                it.replace(
                    templateFileName,
                    domainName.capitalize().plus(templateFileName)
                )
            }

    /**
     * 各階層ごとのテンプレートファイル内の埋め込み文字を置換する関数
     */
    private fun decorateLayerClassTemplateFile(
        inputStreamReader: InputStreamReader,
        moduleName: String,
        layerName: String,
        domainName: String
    ): List<String> = inputStreamReader.readLines().map {
        val pathString = moduleName
            .run { substring(indexOf(layerName)) }
            .replace("/", ".")
            .removePrefix(layerName.plus("."))
        it.replace("{Small}", pathString)
            .replace("{Medium}", domainName)
            .replace("{Large}", domainName.capitalize())
    }

    /**
     * コピー先のパスからモジュールの名前を算出する関数
     */
    private fun getModuleName(to: Path, projectPath: String): String = to
        .toAbsolutePath().toString().removePrefix(projectPath)

    /**
     * パスから最後の「/」以降の文字列を算出する関数
     */
    private fun sliceLastSlashAfter(path: String): String = path
        .substring(path.indexOfLast { it == '/' }).substring(1)

    /**
     * 「/java」パス配下のファイルをコピーする時のみ
     * 共通文言とモジュール名とを付加してパス被りを防ぐ関数
     */
    private fun sourceCodeFilePathAdapter(
        file: File,
        moduleName: String
    ): File =
        if (!file.parent.endsWith("/java")) file
        else File(file.path.replace("/java", "/java/jp/arsaga/$moduleName")).also {
            Files.createDirectories(Paths.get(it.parent))
        }

    /**
     * ファイルもしくはディレクトリを出力し、その結果をログに出す関数
     */
    private fun createNewFile(
        from: File,
        to: File,
        editContent: (InputStreamReader) -> List<String>
    ) {
        if (from.isFile) {
            runCatching { Files.copy(decorateTemplateFile(from.path, editContent), to.toPath()) }
                .onSuccess { println("success createNewFile::${to.toPath()}") }
                .onFailure { println("failure createNewFile::${to.toPath()}") }
        } else {
            runCatching { Files.createDirectories(to.toPath()) }
                .onSuccess { println("success createNewDirectory::${to.toPath()}") }
                .onFailure { println("failure createNewDirectory::${to.toPath()}") }
        }
    }

    /**
     * 受け取ったパスのファイルを引数のラムダに基づいて編集してからInputStreamで返す関数
     */
    private fun decorateTemplateFile(
        path: String,
        editContent: (InputStreamReader) -> List<String>
    ): InputStream = path
        .run(::FileInputStream)
        .run(::InputStreamReader)
        .use { editContent(it) }
        .joinToString("\n")
        .byteInputStream()

    /**
     * プロジェクトのモジュール一覧のenumクラスに実装する
     * enumの実装コードそのものを文字列として出力する関数
     */
    private fun needProjectModuleList(): List<String> = needModuleNameList().map {
        StringBuilder()
            .append("        ")
            .append(it.name)
            .append("(")
            .append(it.layerType.javaClass.name + "." + it.layerType.name)
            .append(", ")
            .append(it.domainType.javaClass.name + "." + it.domainType.name)
            .append(", ")
            .append("CreateType.AUTO")
            .append("),")
            .toString()
            .replace("$", ".")
    }

    /**
     * enumの組み合わせから必要なモジュールを算出する関数
     */
    private fun needModuleNameList(): List<ProjectModule.Entity> =
        ModuleStructure.DomainType.values().flatMap { domain ->
            ModuleStructure.LayerType.values().mapNotNull { layer ->
                moduleLayerCategory.find { it.value.contains(layer) }?.let {
                    StringBuilder()
                        .append(it.key)
                        .append("_")
                        .append(layer.name)
                        .append("_")
                        .append(domain.name)
                        .toString()
                }?.let {
                    ProjectModule.Entity(it, layer, domain)
                }
            }
        }
}