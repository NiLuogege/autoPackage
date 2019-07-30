import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AutoPackageTask extends DefaultTask {

    AutoPackageTask() {
        super
//        dependsOn "assemble"
    }

    @TaskAction
    def AutoPackageAction() {

        //检查参数
        checkProperties()

        File file = new File("")
        def appPath = file.getAbsolutePath()//app文件夹 路径
        println "app路径= ${appPath}"
        //app版本号
        def versionName = project.getProperty("VERSION_NAME")
        //生成的文件存储路径
        def generateFilePath = project.getProperty("GENERATE_FILE_PATH") + "/" + project.getProperty("VERSION_NAME")
        def jiaguInputAPKpath = project.getProperty("JIAGU_INPUT_APK_PATH")//加固apk 输入路径
        def jiaguOutputPath = """${generateFilePath}/jiagu_nosign"""//加固apk 输出路径
        def jiaguNoSignApkName = "app-release_10_jiagu.apk"//加固没签名的apk名称
        def keyFilePath = project.getProperty("KEY_FILE_PATH")//签名文件全路径
        def keyAlias = project.getProperty("KEY_ALIAS")
        def ksPass = project.getProperty("KS_PASS")//KeyStore密码
        def keyPass = project.getProperty("KEY_PASS")//签署者的密码，即生成jks时指定alias对应的密码
        def channelFilePath = project.getProperty("CHANNEL_FILE_PATH")//渠道文件全路径


        def generateFilePathDir = new File(generateFilePath)
        if (!generateFilePathDir.exists() || !generateFilePathDir.isDirectory()) {
            generateFilePathDir.mkdirs()
        }

        def jiaguOutputPathDir = new File(jiaguOutputPath)
        if (!jiaguOutputPathDir.exists() || !jiaguOutputPathDir.isDirectory()) {
            jiaguOutputPathDir.mkdirs()
        }


        def login = """${appPath}/tools/jiagu/java/bin/java -jar ${
            appPath
        }/tools/jiagu/jiagu.jar -login 18729440250 lc835350313"""// Create the String
        def loginProc = login.execute()                 // Call *execute* on the string
        loginProc.waitFor()                               // Wait for the command to finish

        if (loginProc.exitValue() == 0) {
            println "jiagu-->登录成功"

            def jiagu = """${appPath}/tools/jiagu/java/bin/java -jar ${
                appPath
            }/tools/jiagu/jiagu.jar -jiagu ${jiaguInputAPKpath} ${jiaguOutputPath}"""
// Create the String
            def jiaguProc = jiagu.execute()

            println "stdout: ${jiaguProc.in.text}"
            // *out* from the external program is *in* for groovy
            jiaguProc.waitFor()

            if (jiaguProc.exitValue() == 0) {
                println "jiagu-->jiagu成功"

                def channel = """python ${appPath}/tools/channel/build_channel.py ${versionName} ${
                    jiaguOutputPath
                }/${jiaguNoSignApkName} ${keyFilePath} ${keyAlias} ${ksPass} ${keyPass} ${
                    generateFilePath
                } ${channelFilePath}"""// Create the String
                def channelProc = channel.execute()

                println "stdout: ${channelProc.in.text}"
                channelProc.waitFor()

                if (channelProc.exitValue() == 0) {
                    println "channel-->打渠道包成功"

                } else {
                    println "channel-->打渠道包失败**${channelProc.err.text}**"
                }

            } else {
                println "jiagu-->jiagu出错**${loginProc.err.text}**"
            }

        } else {
            println "jiagu-->登录执行出错**${loginProc.err.text}**"
        }


    }

    def checkProperties() {
        if (!project.hasProperty("VERSION_NAME")) {
            throw new RuntimeException("请在 gradle.properties文件中配置 VERSION_NAME")
        }

        if (!project.hasProperty("GENERATE_FILE_PATH")) {
            throw new RuntimeException("请在 gradle.properties文件中配置 GENERATE_FILE_PATH")
        }

        if (!project.hasProperty("JIAGU_INPUT_APK_PATH")) {
            throw new RuntimeException("请在 gradle.properties文件中配置 JIAGU_INPUT_APK_PATH")
        }

        if (!project.hasProperty("KEY_FILE_PATH")) {
            throw new RuntimeException("请在 gradle.properties文件中配置 KEY_FILE_PATH")
        }

        if (!project.hasProperty("KEY_ALIAS")) {
            throw new RuntimeException("请在 gradle.properties文件中配置 KEY_ALIAS")
        }

        if (!project.hasProperty("KS_PASS")) {
            throw new RuntimeException("请在 gradle.properties文件中配置 KS_PASS")
        }

        if (!project.hasProperty("KEY_PASS")) {
            throw new RuntimeException("请在 gradle.properties文件中配置 KEY_PASS")
        }

        if (!project.hasProperty("CHANNEL_FILE_PATH")) {
            throw new RuntimeException("请在 gradle.properties文件中配置 CHANNEL_FILE_PATH")
        }
    }
}