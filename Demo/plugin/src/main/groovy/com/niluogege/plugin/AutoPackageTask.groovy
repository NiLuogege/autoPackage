import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AutoPackageTask extends DefaultTask{

    AutoPackageTask() {
        super
//        dependsOn "assemble"
    }

    @TaskAction
    def AutoPackageAction(){
        println("AutoPackageAction lalall")
    }
}