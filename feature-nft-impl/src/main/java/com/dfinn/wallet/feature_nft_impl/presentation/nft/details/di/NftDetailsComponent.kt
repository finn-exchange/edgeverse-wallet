package com.dfinn.wallet.feature_nft_impl.presentation.nft.details.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.dfinn.wallet.common.di.scope.ScreenScope
import com.dfinn.wallet.feature_nft_impl.presentation.nft.details.NftDetailsFragment

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
