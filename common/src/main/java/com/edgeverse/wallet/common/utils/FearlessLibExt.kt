package com.edgeverse.wallet.common.utils

import com.edgeverse.wallet.common.data.network.runtime.binding.bindNullableNumberConstant
import com.edgeverse.wallet.common.data.network.runtime.binding.bindNumberConstant
import com.edgeverse.wallet.core.model.Node
import io.emeraldpay.polkaj.scale.ScaleCodecReader
import io.emeraldpay.polkaj.scale.ScaleCodecWriter
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.Mnemonic
import jp.co.soramitsu.fearless_utils.encrypt.seed.SeedFactory
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.extensions.toHexString
import jp.co.soramitsu.fearless_utils.hash.Hasher.blake2b256
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.Extrinsic
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.GenericCall
import jp.co.soramitsu.fearless_utils.runtime.definitions.types.generics.SignedExtras
import jp.co.soramitsu.fearless_utils.runtime.metadata.RuntimeMetadata
import jp.co.soramitsu.fearless_utils.runtime.metadata.module
import jp.co.soramitsu.fearless_utils.runtime.metadata.module.Constant
import jp.co.soramitsu.fearless_utils.runtime.metadata.module.MetadataFunction
import jp.co.soramitsu.fearless_utils.runtime.metadata.module.Module
import jp.co.soramitsu.fearless_utils.runtime.metadata.module.StorageEntry
import jp.co.soramitsu.fearless_utils.runtime.metadata.moduleOrNull
import jp.co.soramitsu.fearless_utils.runtime.metadata.splitKey
import jp.co.soramitsu.fearless_utils.scale.EncodableStruct
import jp.co.soramitsu.fearless_utils.scale.Schema
import jp.co.soramitsu.fearless_utils.scale.dataType.DataType
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder.addressPrefix
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder.toAccountId
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder.toAddress
import java.io.ByteArrayOutputStream
import java.math.BigInteger

val BIP32JunctionDecoder.DEFAULT_DERIVATION_PATH: String
    get() = "//44//60//0/0/0"

val SS58Encoder.DEFAULT_PREFIX: Short
    get() = 42.toShort()

fun BIP32JunctionDecoder.default() = decode(DEFAULT_DERIVATION_PATH)

fun StorageEntry.defaultInHex() = default.toHexString(withPrefix = true)

fun ByteArray.toAddress(networkType: Node.NetworkType) = toAddress(networkType.runtimeConfiguration.addressByte)

fun String.removeHexPrefix() = removePrefix("0x")

fun <T> DataType<T>.fromHex(hex: String): T {
    val codecReader = ScaleCodecReader(hex.fromHex())

    return read(codecReader)
}

fun <T> DataType<T>.toHex(value: T): String {
    return toByteArray(value).toHexString(withPrefix = true)
}

fun <T> DataType<T>.toByteArray(value: T): ByteArray {
    val stream = ByteArrayOutputStream()
    val writer = ScaleCodecWriter(stream)

    write(writer, value)

    return stream.toByteArray()
}

typealias StructBuilderWithContext<S> = S.(EncodableStruct<S>) -> Unit

operator fun <S : Schema<S>> S.invoke(block: StructBuilderWithContext<S>? = null): EncodableStruct<S> {
    val struct = EncodableStruct(this)

    block?.invoke(this, struct)

    return struct
}

fun <S : Schema<S>> EncodableStruct<S>.hash(): String {
    return schema.toByteArray(this).blake2b256().toHexString(withPrefix = true)
}

fun String.extrinsicHash(): String {
    return fromHex().blake2b256().toHexString(withPrefix = true)
}

fun String.toHexAccountId(): String = toAccountId().toHexString()

fun Extrinsic.DecodedInstance.tip(): BigInteger? = signature?.signedExtras?.get(SignedExtras.TIP) as? BigInteger

fun Module.constant(name: String) = constantOrNull(name) ?: throw NoSuchElementException()

fun Module.numberConstant(name: String, runtimeSnapshot: RuntimeSnapshot) = bindNumberConstant(constant(name), runtimeSnapshot)
fun Module.optionalNumberConstant(name: String, runtimeSnapshot: RuntimeSnapshot) = bindNullableNumberConstant(constant(name), runtimeSnapshot)

fun Constant.asNumber(runtimeSnapshot: RuntimeSnapshot) = bindNumberConstant(this, runtimeSnapshot)

fun Module.constantOrNull(name: String) = constants[name]

fun RuntimeMetadata.staking() = module(Modules.STAKING)

fun RuntimeMetadata.system() = module(Modules.SYSTEM)

fun RuntimeMetadata.balances() = module(Modules.BALANCES)

fun RuntimeMetadata.assets() = module(Modules.ASSETS)
fun RuntimeMetadata.tokens() = module(Modules.TOKENS)
fun RuntimeMetadata.tokensOrNull() = moduleOrNull(Modules.TOKENS)
fun RuntimeMetadata.currencies() = module(Modules.CURRENCIES)
fun RuntimeMetadata.currenciesOrNull() = moduleOrNull(Modules.CURRENCIES)
fun RuntimeMetadata.crowdloan() = module(Modules.CROWDLOAN)
fun RuntimeMetadata.uniques() = module(Modules.UNIQUES)

fun RuntimeMetadata.babe() = module(Modules.BABE)
fun RuntimeMetadata.babeOrNull() = moduleOrNull(Modules.BABE)

fun RuntimeMetadata.timestampOrNull() = moduleOrNull(Modules.TIMESTAMP)

fun RuntimeMetadata.slots() = module(Modules.SLOTS)

fun RuntimeMetadata.session() = module(Modules.SESSION)

fun RuntimeMetadata.firstExistingModule(vararg options: String): String {
    return options.first(::hasModule)
}

fun StorageEntry.splitKeyToComponents(runtime: RuntimeSnapshot, key: String): ComponentHolder {
    return ComponentHolder(splitKey(runtime, key))
}

fun String.networkType() = Node.NetworkType.findByAddressByte(addressPrefix())!!

fun RuntimeMetadata.hasModule(name: String) = moduleOrNull(name) != null

fun SeedFactory.createSeed32(length: Mnemonic.Length, password: String?) = cropSeedTo32Bytes(createSeed(length, password))

fun SeedFactory.deriveSeed32(mnemonicWords: String, password: String?) = cropSeedTo32Bytes(deriveSeed(mnemonicWords, password))

private fun cropSeedTo32Bytes(seedResult: SeedFactory.Result): SeedFactory.Result {
    return SeedFactory.Result(seed = seedResult.seed.copyOfRange(0, 32), seedResult.mnemonic)
}

fun GenericCall.Instance.oneOf(vararg functionCandidates: MetadataFunction): Boolean = functionCandidates.any { function == it }
fun GenericCall.Instance.instanceOf(functionCandidate: MetadataFunction): Boolean = function == functionCandidate

object Modules {
    const val STAKING = "Staking"
    const val BALANCES = "Balances"
    const val SYSTEM = "System"
    const val CROWDLOAN = "Crowdloan"
    const val BABE = "Babe"
    const val TIMESTAMP = "Timestamp"
    const val SLOTS = "Slots"
    const val SESSION = "Session"

    const val ASSETS = "Assets"
    const val TOKENS = "Tokens"
    const val CURRENCIES = "Currencies"

    const val UNIQUES = "Uniques"
}
