#!/bin/bash

# CHANGE THESE FOR YOUR APP
app_package="com.darvin.usbmavicblehidandroidthing"
dir_app_name="USBMavicBLEHIDAndroidThing"
MAIN_ACTIVITY="MainActivity"

ADB="adb" # how you execute adb
ADB_SH="$ADB shell" # this script assumes using `adb root`. for `adb su` see `Caveats`

path_sysapp="/system/priv-app" # assuming the app is priviledged
apk_host="./app/build/outputs/apk/debug/app-debug.apk"
apk_name=$dir_app_name".apk"
apk_target_dir="$path_sysapp/$dir_app_name"
apk_target_sys="$apk_target_dir/$apk_name"

# # Delete previous APK
# rm -f $apk_host

# # Compile the APK: you can adapt this for production build, flavors, etc.
# ./gradlew assembleDebug || exit -1 # exit on failure

# Install APK: using adb root
$ADB root 2> /dev/null
$ADB remount # mount system
$ADB push $apk_host $apk_target_sys
$ADB push _etc_permissions_privapp-permissions-com.darvin.usbmavicblehidandroidthing.xml  /etc/permissions/privapp-permissions-com.darvin.usbmavicblehidandroidthing.xml 
# Give permissions
$ADB_SH "chmod 755 $apk_target_dir"
$ADB_SH "chmod 644 $apk_target_sys"

#Unmount system
$ADB_SH "mount -o remount,ro /"

# Stop the app
$ADB shell "am force-stop $app_package"


$ADB shell pm grant $app_package com.google.android.things.permission.MANAGE_BLUETOOTH
$ADB shell pm grant $app_package android.permission.BLUETOOTH_PRIVILEGED
$ADB shell pm grant $app_package android.permission.BLUETOOTH_ADMIN
$ADB shell pm grant $app_package android.permission.BLUETOOTH


# Re execute the app
$ADB shell "am start -n \"$app_package/$app_package.$MAIN_ACTIVITY\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"
