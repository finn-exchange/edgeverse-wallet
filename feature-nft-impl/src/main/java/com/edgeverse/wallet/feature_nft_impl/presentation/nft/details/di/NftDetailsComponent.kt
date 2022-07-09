package com.edgeverse.wallet.feature_nft_impl.presentation.nft.details.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_nft_impl.presentation.nft.details.NftDetailsFragment

@Subcomponent(
    modules = [
        NfDetailsModule::class
    ]
)
@ScreenScope
interface NftDetailsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance nftId: String
        ): NftDetailsComponent
    }

    fun inject(fragment: NftDetailsFragment)
}
