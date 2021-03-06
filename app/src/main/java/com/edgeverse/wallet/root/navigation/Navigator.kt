package com.edgeverse.wallet.root.navigation

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.edgeverse.wallet.R
import com.edgeverse.wallet.common.navigation.DelayedNavigation
import com.edgeverse.wallet.common.utils.postToUiThread
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.AddAccountPayload
import com.edgeverse.wallet.feature_account_api.presenatation.account.add.ImportAccountPayload
import com.edgeverse.wallet.feature_account_impl.presentation.AccountRouter
import com.edgeverse.wallet.feature_account_impl.presentation.account.create.CreateAccountFragment
import com.edgeverse.wallet.feature_account_impl.presentation.account.details.AccountDetailsFragment
import com.edgeverse.wallet.feature_account_impl.presentation.account.list.AccountChosenNavDirection
import com.edgeverse.wallet.feature_account_impl.presentation.account.list.AccountListFragment
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.ExportPayload
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmFragment
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.json.confirm.ExportJsonConfirmPayload
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.json.password.ExportJsonPasswordFragment
import com.edgeverse.wallet.feature_account_impl.presentation.exporting.seed.ExportSeedFragment
import com.edgeverse.wallet.feature_account_impl.presentation.importing.ImportAccountFragment
import com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.backup.BackupMnemonicFragment
import com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.backup.BackupMnemonicPayload
import com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.confirm.ConfirmMnemonicFragment
import com.edgeverse.wallet.feature_account_impl.presentation.mnemonic.confirm.ConfirmMnemonicPayload
import com.edgeverse.wallet.feature_account_impl.presentation.node.details.NodeDetailsFragment
import com.edgeverse.wallet.feature_account_impl.presentation.pincode.PinCodeAction
import com.edgeverse.wallet.feature_account_impl.presentation.pincode.PincodeFragment
import com.edgeverse.wallet.feature_account_impl.presentation.pincode.ToolbarConfiguration
import com.edgeverse.wallet.feature_assets.presentation.AssetPayload
import com.edgeverse.wallet.feature_assets.presentation.WalletRouter
import com.edgeverse.wallet.feature_assets.presentation.balance.detail.BalanceDetailFragment
import com.edgeverse.wallet.feature_assets.presentation.model.OperationParcelizeModel
import com.edgeverse.wallet.feature_assets.presentation.receive.ReceiveFragment
import com.edgeverse.wallet.feature_assets.presentation.send.TransferDraft
import com.edgeverse.wallet.feature_assets.presentation.send.amount.SelectSendFragment
import com.edgeverse.wallet.feature_assets.presentation.send.confirm.ConfirmSendFragment
import com.edgeverse.wallet.feature_assets.presentation.transaction.detail.extrinsic.ExtrinsicDetailFragment
import com.edgeverse.wallet.feature_assets.presentation.transaction.detail.reward.RewardDetailFragment
import com.edgeverse.wallet.feature_assets.presentation.transaction.detail.transfer.TransferDetailFragment
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.CrowdloanRouter
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.confirm.ConfirmContributeFragment
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.confirm.parcel.ConfirmContributePayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.BonusPayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.CustomContributeFragment
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.model.CustomContributePayload
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.terms.MoonbeamCrowdloanTermsFragment
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.CrowdloanContributeFragment
import com.edgeverse.wallet.feature_crowdloan_impl.presentation.contribute.select.parcel.ContributePayload
import com.edgeverse.wallet.feature_onboarding_impl.OnboardingRouter
import com.edgeverse.wallet.feature_onboarding_impl.presentation.welcome.WelcomeFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.StakingRouter
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.ConfirmPayoutFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.confirm.model.ConfirmPayoutPayload
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.detail.PayoutDetailsFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.payouts.model.PendingPayoutParcelable
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.confirm.ConfirmBondMoreFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.confirm.ConfirmBondMorePayload
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.select.SelectBondMoreFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.bond.select.SelectBondMorePayload
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.controller.confirm.ConfirmSetControllerFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.controller.confirm.ConfirmSetControllerPayload
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.main.model.StakingStoryModel
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.confirm.ConfirmRebondFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rebond.confirm.ConfirmRebondPayload
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.redeem.RedeemFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.redeem.RedeemPayload
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rewardDestination.confirm.ConfirmRewardDestinationFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.rewardDestination.confirm.parcel.ConfirmRewardDestinationPayload
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.confirm.ConfirmUnbondFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.staking.unbond.confirm.ConfirmUnbondPayload
import com.edgeverse.wallet.feature_staking_impl.presentation.story.StoryFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.details.ValidatorDetailsFragment
import com.edgeverse.wallet.feature_staking_impl.presentation.validators.parcel.ValidatorDetailsParcelModel
import com.edgeverse.wallet.root.presentation.RootRouter
import com.edgeverse.wallet.splash.SplashRouter
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.Flow

@Parcelize
class NavComponentDelayedNavigation(val globalActionId: Int, val extras: Bundle? = null) : DelayedNavigation

class Navigator(
    private val navigationHolder: NavigationHolder,
) :
    SplashRouter,
    OnboardingRouter,
    AccountRouter,
    WalletRouter,
    RootRouter,
    StakingRouter,
    CrowdloanRouter {

    private val navController: NavController?
        get() = navigationHolder.navController

    override fun openAddFirstAccount() {
        navController?.navigate(R.id.action_splash_to_onboarding, WelcomeFragment.bundle(false))
    }

    override fun openInitialCheckPincode() {
        val action = PinCodeAction.Check(NavComponentDelayedNavigation(R.id.action_open_main), ToolbarConfiguration())
        val bundle = PincodeFragment.getPinCodeBundle(action)
        navController?.navigate(R.id.action_splash_to_pin, bundle)
    }

    override fun openCreateAccount(addAccountPayload: AddAccountPayload.MetaAccount) {
        navController?.navigate(R.id.action_welcomeFragment_to_createAccountFragment, CreateAccountFragment.getBundle(addAccountPayload))
    }

    override fun openMain() {
        navController?.navigate(R.id.action_open_main)
    }

    override fun openAfterPinCode(delayedNavigation: DelayedNavigation) {
        require(delayedNavigation is NavComponentDelayedNavigation)

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.pincodeFragment, true)
            .setEnterAnim(R.anim.fragment_open_enter)
            .setExitAnim(R.anim.fragment_open_exit)
            .setPopEnterAnim(R.anim.fragment_close_enter)
            .setPopExitAnim(R.anim.fragment_close_exit)
            .build()

        navController?.navigate(delayedNavigation.globalActionId, delayedNavigation.extras, navOptions)
    }

    override fun openCreatePincode() {
        val bundle = buildCreatePinBundle()

        when (navController?.currentDestination?.id) {
            R.id.splashFragment -> navController?.navigate(R.id.action_splash_to_pin, bundle)
            R.id.importAccountFragment -> navController?.navigate(R.id.action_importAccountFragment_to_pincodeFragment, bundle)
            R.id.confirmMnemonicFragment -> navController?.navigate(R.id.action_confirmMnemonicFragment_to_pincodeFragment, bundle)
        }
    }

    override fun openConfirmMnemonicOnCreate(confirmMnemonicPayload: ConfirmMnemonicPayload) {
        val bundle = ConfirmMnemonicFragment.getBundle(confirmMnemonicPayload)

        navController?.navigate(
            R.id.action_backupMnemonicFragment_to_confirmMnemonicFragment,
            bundle
        )
    }

    override fun openImportAccountScreen(payload: ImportAccountPayload) {
        val destination = when (val currentDestinationId = navController?.currentDestination?.id) {
            R.id.welcomeFragment -> R.id.action_welcomeFragment_to_import_nav_graph
            R.id.accountDetailsFragment -> R.id.action_accountDetailsFragment_to_import_nav_graph
            else -> throw IllegalArgumentException("Unknown current destination to open import account screen: $currentDestinationId")
        }

        navController?.navigate(destination, ImportAccountFragment.getBundle(payload))
    }

    override fun openMnemonicScreen(accountName: String?, addAccountPayload: AddAccountPayload) {
        val destination = when (val currentDestinationId = navController?.currentDestination?.id) {
            R.id.welcomeFragment -> R.id.action_welcomeFragment_to_mnemonic_nav_graph
            R.id.createAccountFragment -> R.id.action_createAccountFragment_to_mnemonic_nav_graph
            R.id.accountDetailsFragment -> R.id.action_accountDetailsFragment_to_mnemonic_nav_graph
            else -> throw IllegalArgumentException("Unknown current destination to open mnemonic screen: $currentDestinationId")
        }

        val payload = BackupMnemonicPayload.Create(accountName, addAccountPayload)
        navController?.navigate(destination, BackupMnemonicFragment.getBundle(payload))
    }

    override fun openSetupStaking() {
        navController?.navigate(R.id.action_mainFragment_to_setupStakingFragment)
    }

    override fun openStartChangeValidators() {
        navController?.navigate(R.id.openStartChangeValidatorsFragment)
    }

    override fun openStory(story: StakingStoryModel) {
        navController?.navigate(R.id.open_staking_story, StoryFragment.getBundle(story))
    }

    override fun openPayouts() {
        navController?.navigate(R.id.action_mainFragment_to_payoutsListFragment)
    }

    override fun openPayoutDetails(payout: PendingPayoutParcelable) {
        navController?.navigate(R.id.action_payoutsListFragment_to_payoutDetailsFragment, PayoutDetailsFragment.getBundle(payout))
    }

    override fun openConfirmPayout(payload: ConfirmPayoutPayload) {
        navController?.navigate(R.id.action_open_confirm_payout, ConfirmPayoutFragment.getBundle(payload))
    }

    override fun openBondMore() {
        navController?.navigate(R.id.action_open_selectBondMoreFragment, SelectBondMoreFragment.getBundle(SelectBondMorePayload()))
    }

    override fun openConfirmBondMore(payload: ConfirmBondMorePayload) {
        navController?.navigate(R.id.action_selectBondMoreFragment_to_confirmBondMoreFragment, ConfirmBondMoreFragment.getBundle(payload))
    }

    override fun openSelectUnbond() {
        navController?.navigate(R.id.action_mainFragment_to_selectUnbondFragment)
    }

    override fun openConfirmUnbond(payload: ConfirmUnbondPayload) {
        navController?.navigate(R.id.action_selectUnbondFragment_to_confirmUnbondFragment, ConfirmUnbondFragment.getBundle(payload))
    }

    override fun openRedeem() {
        navController?.navigate(R.id.action_open_redeemFragment, RedeemFragment.getBundle(RedeemPayload()))
    }

    override fun openConfirmRebond(payload: ConfirmRebondPayload) {
        navController?.navigate(R.id.action_open_confirm_rebond, ConfirmRebondFragment.getBundle(payload))
    }

    override fun openContribute(payload: ContributePayload) {
        val bundle = CrowdloanContributeFragment.getBundle(payload)

        when (navController?.currentDestination?.id) {
            R.id.mainFragment -> navController?.navigate(R.id.action_mainFragment_to_crowdloanContributeFragment, bundle)
            R.id.moonbeamCrowdloanTermsFragment -> navController?.navigate(R.id.action_moonbeamCrowdloanTermsFragment_to_crowdloanContributeFragment, bundle)
        }
    }

    override val customBonusFlow: Flow<BonusPayload?>
        get() = navController!!.currentBackStackEntry!!.savedStateHandle
            .getLiveData<BonusPayload?>(CrowdloanContributeFragment.KEY_BONUS_LIVE_DATA)
            .asFlow()

    override val latestCustomBonus: BonusPayload?
        get() = navController!!.currentBackStackEntry!!.savedStateHandle
            .get(CrowdloanContributeFragment.KEY_BONUS_LIVE_DATA)

    override fun openCustomContribute(payload: CustomContributePayload) {
        navController?.navigate(R.id.action_crowdloanContributeFragment_to_customContributeFragment, CustomContributeFragment.getBundle(payload))
    }

    override fun setCustomBonus(payload: BonusPayload) {
        navController!!.previousBackStackEntry!!.savedStateHandle.set(CrowdloanContributeFragment.KEY_BONUS_LIVE_DATA, payload)
    }

    override fun openConfirmContribute(payload: ConfirmContributePayload) {
        navController?.navigate(R.id.action_crowdloanContributeFragment_to_confirmContributeFragment, ConfirmContributeFragment.getBundle(payload))
    }

    override fun back() {
        navigationHolder.executeBack()
    }

    override fun openCustomRebond() {
        navController?.navigate(R.id.action_mainFragment_to_customRebondFragment)
    }

    override fun openCurrentValidators() {
        navController?.navigate(R.id.action_mainFragment_to_currentValidatorsFragment)
    }

    override fun returnToCurrentValidators() {
        navController?.navigate(R.id.action_confirmStakingFragment_back_to_currentValidatorsFragment)
    }

    override fun openChangeRewardDestination() {
        navController?.navigate(R.id.action_mainFragment_to_selectRewardDestinationFragment)
    }

    override fun openConfirmRewardDestination(payload: ConfirmRewardDestinationPayload) {
        navController?.navigate(
            R.id.action_selectRewardDestinationFragment_to_confirmRewardDestinationFragment,
            ConfirmRewardDestinationFragment.getBundle(payload)
        )
    }

    override val currentStackEntryLifecycle: Lifecycle
        get() = navController!!.currentBackStackEntry!!.lifecycle

    override fun openControllerAccount() {
        navController?.navigate(R.id.action_stakingBalanceFragment_to_setControllerAccountFragment)
    }

    override fun openConfirmSetController(payload: ConfirmSetControllerPayload) {
        navController?.navigate(
            R.id.action_stakingSetControllerAccountFragment_to_confirmSetControllerAccountFragment,
            ConfirmSetControllerFragment.getBundle(payload)
        )
    }

    override fun openRecommendedValidators() {
        navController?.navigate(R.id.action_startChangeValidatorsFragment_to_recommendedValidatorsFragment)
    }

    override fun openSelectCustomValidators() {
        navController?.navigate(R.id.action_startChangeValidatorsFragment_to_selectCustomValidatorsFragment)
    }

    override fun openCustomValidatorsSettings() {
        navController?.navigate(R.id.action_selectCustomValidatorsFragment_to_settingsCustomValidatorsFragment)
    }

    override fun openSearchCustomValidators() {
        navController?.navigate(R.id.action_selectCustomValidatorsFragment_to_searchCustomValidatorsFragment)
    }

    override fun openReviewCustomValidators() {
        navController?.navigate(R.id.action_selectCustomValidatorsFragment_to_reviewCustomValidatorsFragment)
    }

    override fun openConfirmStaking() {
        navController?.navigate(R.id.openConfirmStakingFragment)
    }

    override fun openConfirmNominations() {
        navController?.navigate(R.id.action_confirmStakingFragment_to_confirmNominationsFragment)
    }

    override fun returnToMain() {
        navController?.navigate(R.id.back_to_main)
    }

    override fun openMoonbeamFlow(payload: ContributePayload) {
        navController?.navigate(R.id.action_mainFragment_to_moonbeamCrowdloanTermsFragment, MoonbeamCrowdloanTermsFragment.getBundle(payload))
    }

    override fun openValidatorDetails(validatorDetails: ValidatorDetailsParcelModel) {
        navController?.navigate(R.id.open_validator_details, ValidatorDetailsFragment.getBundle(validatorDetails))
    }

    override fun openFilter() {
        navController?.navigate(R.id.action_mainFragment_to_filterFragment)
    }

    override fun openSend(assetPayload: AssetPayload, initialRecipientAddress: String?) {
        val extras = SelectSendFragment.getBundle(assetPayload, initialRecipientAddress)

        navController?.navigate(R.id.action_open_send, extras)
    }

    override fun openConfirmTransfer(transferDraft: TransferDraft) {
        val bundle = ConfirmSendFragment.getBundle(transferDraft)

        navController?.navigate(R.id.action_chooseAmountFragment_to_confirmTransferFragment, bundle)
    }

    override fun finishSendFlow() {
        navController?.navigate(R.id.finish_send_flow)
    }

    override fun openTransferDetail(transaction: OperationParcelizeModel.Transfer) {
        val bundle = TransferDetailFragment.getBundle(transaction)

        navController?.navigate(R.id.open_transfer_detail, bundle)
    }

    override fun openRewardDetail(reward: OperationParcelizeModel.Reward) {
        val bundle = RewardDetailFragment.getBundle(reward)

        navController?.navigate(R.id.open_reward_detail, bundle)
    }

    override fun openExtrinsicDetail(extrinsic: OperationParcelizeModel.Extrinsic) {
        val bundle = ExtrinsicDetailFragment.getBundle(extrinsic)

        navController?.navigate(R.id.open_extrinsic_detail, bundle)
    }

    override fun openAccounts(accountChosenNavDirection: AccountChosenNavDirection) {
        navController?.navigate(R.id.action_open_accounts, AccountListFragment.getBundle(accountChosenNavDirection))
    }

    override fun openNodes() {
        navController?.navigate(R.id.action_mainFragment_to_nodesFragment)
    }

    override fun openLanguages() {
        navController?.navigate(R.id.action_mainFragment_to_languagesFragment)
    }

    override fun openChangeAccount() {
        openAccounts(AccountChosenNavDirection.BACK)
    }

    override fun openReceive(assetPayload: AssetPayload) {
        navController?.navigate(R.id.action_open_receive, ReceiveFragment.getBundle(assetPayload))
    }

    override fun openAssetFilters() {
        navController?.navigate(R.id.action_mainFragment_to_assetFiltersFragment)
    }

    override fun openNfts() {
        navController?.navigate(R.id.action_mainFragment_to_nfts_nav_graph)
    }

    override fun returnToWallet() {
        // to achieve smooth animation
        postToUiThread {
            navController?.navigate(R.id.action_return_to_wallet)
        }
    }

    override fun openAccountDetails(metaAccountId: Long) {
        val extras = AccountDetailsFragment.getBundle(metaAccountId)

        navController?.navigate(R.id.action_open_account_details, extras)
    }

    override fun openEditAccounts() {
        navController?.navigate(R.id.action_accountsFragment_to_editAccountsFragment)
    }

    override fun backToMainScreen() {
        navController?.navigate(R.id.action_editAccountsFragment_to_mainFragment)
    }

    override fun openNodeDetails(nodeId: Int) {
        navController?.navigate(R.id.action_nodesFragment_to_nodeDetailsFragment, NodeDetailsFragment.getBundle(nodeId))
    }

    override fun openAssetDetails(assetPayload: AssetPayload) {
        val bundle = BalanceDetailFragment.getBundle(assetPayload)

        navController?.navigate(R.id.action_mainFragment_to_balanceDetailFragment, bundle)
    }

    override fun openAddNode() {
        navController?.navigate(R.id.action_nodesFragment_to_addNodeFragment)
    }

    override fun openAddAccount(payload: AddAccountPayload) {
        navController?.navigate(R.id.action_open_onboarding, WelcomeFragment.bundle(payload))
    }

    override fun openUserContributions() {
        navController?.navigate(R.id.action_mainFragment_to_userContributionsFragment)
    }

    override fun exportMnemonicAction(exportPayload: ExportPayload): DelayedNavigation {
        val payload = BackupMnemonicPayload.Confirm(exportPayload.chainId, exportPayload.metaId)
        val extras = BackupMnemonicFragment.getBundle(payload)

        return NavComponentDelayedNavigation(R.id.action_open_mnemonic_nav_graph, extras)
    }

    override fun exportSeedAction(exportPayload: ExportPayload): DelayedNavigation {
        val extras = ExportSeedFragment.getBundle(exportPayload)

        return NavComponentDelayedNavigation(R.id.action_export_seed, extras)
    }

    override fun exportJsonPasswordAction(exportPayload: ExportPayload): DelayedNavigation {
        val extras = ExportJsonPasswordFragment.getBundle(exportPayload)

        return NavComponentDelayedNavigation(R.id.action_export_json, extras)
    }

    override fun openExportJsonConfirm(payload: ExportJsonConfirmPayload) {
        val extras = ExportJsonConfirmFragment.getBundle(payload)

        navController?.navigate(R.id.action_exportJsonPasswordFragment_to_exportJsonConfirmFragment, extras)
    }

    override fun finishExportFlow() {
        navController?.navigate(R.id.finish_export_flow)
    }

    override fun openChangePinCode() {
        val action = PinCodeAction.Change
        val bundle = PincodeFragment.getPinCodeBundle(action)
        navController?.navigate(R.id.action_mainFragment_to_pinCodeFragment, bundle)
    }

    override fun withPinCodeCheckRequired(
        delayedNavigation: DelayedNavigation,
        createMode: Boolean,
        pinCodeTitleRes: Int?,
    ) {
        val action = if (createMode) {
            PinCodeAction.Create(delayedNavigation)
        } else {
            PinCodeAction.Check(delayedNavigation, ToolbarConfiguration(pinCodeTitleRes, true))
        }

        val extras = PincodeFragment.getPinCodeBundle(action)

        navController?.navigate(R.id.open_pincode_check, extras)
    }

    private fun buildCreatePinBundle(): Bundle {
        val delayedNavigation = NavComponentDelayedNavigation(R.id.action_open_main)
        val action = PinCodeAction.Create(delayedNavigation)
        return PincodeFragment.getPinCodeBundle(action)
    }
}
