package com.edgeverse.wallet.feature_crowdloan_impl.data.network.api.bifrost

class BifrostReferralCheckRequest(code: String) {
    val query = """
        {
            getAccountByInvitationCode(code: "$code") {
                account
            }
        }
    """.trimIndent()
}
