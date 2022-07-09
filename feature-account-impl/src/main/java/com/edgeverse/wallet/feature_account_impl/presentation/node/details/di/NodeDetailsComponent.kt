package com.edgeverse.wallet.feature_account_impl.presentation.node.details.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_account_impl.presentation.node.details.NodeDetailsFragment

@Subcomponent(
    modules = [
        NodeDetailsModule::class
    ]
)
@ScreenScope
interface NodeDetailsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance nodeId: Int,
        ): NodeDetailsComponent
    }

    fun inject(fragment: NodeDetailsFragment)
}
