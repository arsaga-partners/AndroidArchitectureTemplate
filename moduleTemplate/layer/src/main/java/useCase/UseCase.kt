package jp.arsaga.domain.useCase.{DomainPath}

import jp.arsaga.domain.useCase.core.BaseUseCase

class {DomainUpperCamel}UseCase<NavCallback>(
    override val dependency: Dependency<NavCallback>
) : BaseUseCase<{DomainUpperCamel}UseCase.Dependency<NavCallback>>  {

    data class Dependency<NavCallback>(
        override val navigator: Navigator<NavCallback>,
        override val command: Command<NavCallback>,
        override val query: Query
    ) : BaseUseCase.Dependency

    interface Navigator<NavCallback>

    interface Command<NavCallback>

    interface Query
}