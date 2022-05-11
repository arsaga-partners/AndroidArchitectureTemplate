package jp.arsaga.presentation.viewModel.{DomainPath}

import android.app.Application
import androidx.lifecycle.viewModelScope
import jp.arsaga.dataStore.repository.{DomainPath}.{DomainUpperCamel}CommandImpl
import jp.arsaga.dataStore.repository.{DomainPath}.{DomainUpperCamel}QueryImpl
import jp.arsaga.domain.useCase.{DomainPath}.{DomainUpperCamel}UseCase
import jp.arsaga.domain.useCase.{DomainPath}.{DomainUpperCamel}UseCase.*
import jp.arsaga.domain.useCase.core.ActivityCallback
import jp.arsaga.presentation.viewModel.core.extension.BaseViewModel

class {DomainUpperCamel}ViewModel(
    application: Application,
    navigator: Navigator<ActivityCallback>
) : BaseViewModel<Dependency<ActivityCallback>>(application) {
    override val useCase = {DomainUpperCamel}UseCase(
        Dependency(
            navigator,
            {DomainUpperCamel}CommandImpl(application, viewModelScope),
            {DomainUpperCamel}QueryImpl(application, viewModelScope)
        )
    )
}