package com.edgeverse.wallet.feature_nft_impl.presentation.nft.list.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_nft_impl.presentation.nft.list.NftListFragment

@Subcomponent(
    modules = [
        NftListModule::class
    ]
)
@ScreenScope
interface NftListComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
        ): NftListComponent
    }

    fun inject(fragment: NftListFragment)
}
