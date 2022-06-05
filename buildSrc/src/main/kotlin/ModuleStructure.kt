import org.gradle.api.Project
import script.ModuleExtension
import script.ModuleExtension.api
import script.ModuleExtension.impl

/**
 * 各build.gradle.ktsから呼び出す関数
 */
fun Project.moduleStructure() {
    ModuleExtension.findModuleType(this)
        ?.let { ModuleStructure.implModuleByLayerType(this, it) }
}

object ModuleStructure {

    /**
     * アプリ内で扱うドメイン(概念)一覧
     */
    enum class DomainType {
        // 共通モジュール
        core,

        // 認証モジュール
        auth,
        auth_facebook,
    }

    /**
     * モジュールの各レイヤーに特徴的な名前の種類
     */
    enum class LayerType {
        /**
         * Androidのリソースフォルダ
         */
        res,

        /**
         * ナビゲーション・端末依存解決について書くレイヤー
         * 主にActivity, Fragment, View, Navigation, アニメーション関連のクラスを置く
         * 依存関係にまつわる主な処理はServiceにあるNavigatorを継承したクラスの作成、
         * ViewModelのコンストラクタに渡すことと、ViewModelに作ったViewEventのListenerとViewを紐づけること
         * 他の依存はデータバインディングで解決していく
         * ForegroundServiceもここで作成していく
         */
        view,

        /**
         * レイアウト・アニメーションについて記述するレイヤー
         * JetpackComposeのプレビューを高速化するために作成した
         * KotlinとComposeとentityのみに依存しているため、
         * Navigationや状態の受け取りはViewレイヤーで行うものとする
         */
        layout,

        /**
         * AACのViewModelを継承するクラスを置くモジュール
         * 1.repositoryクラスとserviceクラス紐付け
         * 2.複数画面で共有されるインスタンスとしてライフタイムへの対応
         * 3.AndroidViewのリスナーからserviceの各メソッドを叩く繋ぎ込み処理
         * 4.serviceから直接アクセスさせないxmlリソースへのアクセス
         * などを行う
         */
        viewModel,

        /**
         * データの実体インスタンスを保持・管理するクラスを置くモジュール
         * gatewayモジュールの関数を順番/並列に叩く処理の管理も行う
         * 基本的にクラス内にデータを保持するがドメイン(モジュール)をまたぐデータ型の場合は
         * coreモジュールのシングルトン内にプロパティを作りその値をカスタムgetterで渡すようにする
         * 共有するデータが多い(Like問題)ことが見込まれる場合はシングルトンではなくDBの採用を検討する
         * WorkManagerやServiceのクラスもここで作成する
         */
        repository,

        /**
         * IO処理を記述するモジュール
         * API、DB、Preference、SaaSなどへのデータ取得処理を記述する
         * 上位のgatewayでは各IOを順番に同期的に呼び出す必要がある時のメソッドを記述する
         * 並列化やキャッシュコントロールはここでは書かない
         */
        gateway,

        //これより下はAndroidSDKに依存しないロジックを記述するためユニットテストを書きやすい
        /**
         * ユーザー視点での機能を表現するクラスを置くモジュール
         * コルーチン(Flowなどの拡張も含む)とentityモジュールだけで実現できるロジックを書く
         * viewModelと分けている理由はテスタビリティのため
         */
        useCase,

        /**
         * データの型とenumとAndroidに依存しないUtilロジックのクラスを置くモジュール
         * 1つのデータだけで完結するシンプルなクエリメソッドはこのモジュールのクラスにメンバメソッドか拡張関数として書く
         */
        entity
    }

    /**
     * 各モジュールがどのモジュールをインポートするのかを定義するスイッチ文
     * appモジュールは全モジュールをimplするようにしている
     */
    internal fun implModuleByLayerType(
        project: Project,
        moduleType: ProjectModule.Type
    ) = project.dependencies.apply {
        when (moduleType) {
            // 親モジュール
            ProjectModule.Type._app -> {
                val rootModuleSet = ProjectModule.Type.values()
                    .filter { it.layerType == null && it.domainType == null }
                    .toSet()
                ModuleExtension.eachAllModule(rootModuleSet) {
                    impl(it)
                }
            }
            ProjectModule.Type._dataStore_repository -> {
                ModuleExtension.byLayerModuleList(LayerType.repository).forEach {
                    if (it != ProjectModule.Type._dataStore_repository) api(it)
                }
            }
            // coreモジュール
            ProjectModule.Type._presentation_view_core -> {
                api(ProjectModule.Type._presentation_viewModel_core)
                api(ProjectModule.Type._presentation_layout_core)
                api(ProjectModule.Type._extension_view)
            }
            ProjectModule.Type._presentation_layout_core -> {
                api(ProjectModule.Type._extension_compose)
                api(ProjectModule.Type._domain_entity_core)
            }
            ProjectModule.Type._presentation_viewModel_core -> {
                api(ProjectModule.Type._domain_useCase_core)
                api(ProjectModule.Type._extension_viewModel)
            }
            ProjectModule.Type._dataStore_repository_core -> {
                api(ProjectModule.Type._dataStore_gateway_local)
                api(ProjectModule.Type._dataStore_gateway_sdk)
                api(ProjectModule.Type._dataStore_gateway_server)
                api(ProjectModule.Type._domain_useCase_core)
                api(ProjectModule.Type._extension_repositoryFlow)
                api(ProjectModule.Type._extension_gateway)
            }
            // gatewayモジュール
            ProjectModule.Type._dataStore_gateway_sdk -> {
            }
            ProjectModule.Type._dataStore_gateway_local -> {
                impl(ProjectModule.Type._domain_entity_core)
            }
            ProjectModule.Type._dataStore_gateway_server -> {
                impl(ProjectModule.Type._extension_gateway)
                ModuleExtension.byLayerModuleList(LayerType.entity).forEach {
                    api(it)
                }
            }
            ProjectModule.Type._domain_entity_core -> {
                api(ProjectModule.Type._extension_repository)
            }
            // 依存モジュールを持たないもの
            ProjectModule.Type._extension_view,
            ProjectModule.Type._extension_compose,
            ProjectModule.Type._extension_viewModel,
            ProjectModule.Type._extension_repository,
            ProjectModule.Type._extension_repositoryFlow,
            ProjectModule.Type._extension_gateway -> {
            }
            else -> {
                ModuleExtension.implDomainModule(this, moduleType)
            }
        }
    }
}