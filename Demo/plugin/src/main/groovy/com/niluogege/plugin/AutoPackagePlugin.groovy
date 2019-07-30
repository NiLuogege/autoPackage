package com.niluogege.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class AutoPackagePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.tasks.create('autoPackage').doFirst {
            println("autoPackage doFirst")
        }.doLast {
            println("autoPackage doLast")
        }
    }
}