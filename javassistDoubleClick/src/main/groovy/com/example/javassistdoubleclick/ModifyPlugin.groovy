package com.example.javassistdoubleclick

import org.gradle.api.Plugin
import org.gradle.api.Project
class ModifyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        print("==START==")
        project.android.registerTransform(new MyTransform(project))
    }
}