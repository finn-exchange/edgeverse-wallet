#!/usr/bin/env bash
adb devices
# adb logcat -c
# adb logcat &
./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dfinn.wallet.balances.BalancesIntegrationTest