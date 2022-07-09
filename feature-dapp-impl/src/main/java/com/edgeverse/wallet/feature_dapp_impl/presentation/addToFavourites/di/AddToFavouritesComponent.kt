package com.edgeverse.wallet.feature_dapp_impl.presentation.addToFavourites.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_dapp_impl.presentation.addToFavourites.AddToFavouritesFragment
import com.edgeverse.wallet.feature_dapp_impl.presentation.addToFavourites.AddToFavouritesPayload

@Subcomponent(
    modules = [
        AddToFavouritesModule::class
    ]
)
@ScreenScope
interface AddToFavouritesComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payload: AddToFavouritesPayload,
        ): AddToFavouritesComponent
    }

    fun inject(fragment: AddToFavouritesFragment)
}
