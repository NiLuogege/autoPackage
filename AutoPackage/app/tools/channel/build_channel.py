# coding=utf-8
import os
import sys

version = sys.argv[1]  # 版本号
unSignApkName = sys.argv[2]  # 360加固之后没有签名的apk
keyFilePath = sys.argv[3]  # 签名文件全路径
keyAlias = sys.argv[4]  # Alias 名称
ksPass = sys.argv[5]  # KeyStore密码
keyPass = sys.argv[6]  # 签署者的密码，即生成jks时指定alias对应的密码
generateFilePath = sys.argv[7]  # 生成的文件存储路径
channelFilePath = sys.argv[8]  # 渠道文件全路径

channelOutFile = "channels"  # 输出的渠道文件名
buildToolFile = sys.argv[9]  # sdk 编译环境的位置
apkName = "App_" + version + "_sign.apk"  # 签名之后的名称
apkFile = os.path.dirname(os.path.realpath(__file__)) + "/"  # 获取到当前py文件的父目录
jiagu_sign_apk_Path = generateFilePath + "/jiagu_sign/"  # 加固签名后的apk存储路径

if os.path.exists(jiagu_sign_apk_Path):
    print "version is exist!"
else:
    os.mkdir(jiagu_sign_apk_Path)

os.chdir(buildToolFile)

zipResult = os.system(
    "zipalign -v -f 4 " + unSignApkName + " " + jiagu_sign_apk_Path + apkName)
print(zipResult)

if zipResult == 0:
    print("zipalign success")
else:
    print("zipalign failed")
    exit(1)
signPath = "apksigner sign --ks " + keyFilePath + " --ks-key-alias " + keyAlias + " --ks-pass pass:" + ksPass + "  --key-pass pass:" + keyPass + " " + jiagu_sign_apk_Path + apkName
print(signPath)
signResult = os.system(signPath)
print(signResult)

if signResult == 0:
    print("sign success")
else:
    print("sign failed")
    exit(1)

os.chdir(apkFile)
checkResult = os.system(
    "java -jar CheckAndroidSignature.jar " + jiagu_sign_apk_Path + apkName)
print(checkResult)
if checkResult == 0:
    print("sign success")
else:
    print("sign failed")
    exit(1)

channelResult = os.system(
    "java -jar walle.jar batch -f " + channelFilePath + " " + jiagu_sign_apk_Path + apkName + " " + generateFilePath + "/" + channelOutFile)
print(checkResult)

if channelResult == 0:
    print("build channel success")
else:
    print("build channel failed")
    exit(1)
