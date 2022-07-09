package com.edgeverse.wallet.feature_staking_impl.presentation.story.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import com.edgeverse.wallet.common.di.scope.ScreenScope
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.model.StakingStoryModel
import com.edgeverse.wallet.feature_staking_impl.presentation.story.StoryFragment

@Subcomponent(
    modules = [
        StoryModule::class
    ]
)
@ScreenScope
interface StoryComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance story: StakingStoryModel
        ): StoryComponent
    }

    fun inject(fragment: StoryFragment)
}
