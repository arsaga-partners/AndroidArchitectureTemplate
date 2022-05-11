package jp.arsaga.dataStore.repository.{DomainPath}

import android.content.Context
import jp.arsaga.domain.useCase.{DomainPath}.{DomainUpperCamel}UseCase
import jp.arsaga.domain.useCase.core.ActivityCallback
import kotlinx.coroutines.CoroutineScope

class {DomainUpperCamel}CommandImpl(
    private val context: Context,
    private val coroutineScope: CoroutineScope
) : {DomainUpperCamel}UseCase.Command<ActivityCallback> {

}