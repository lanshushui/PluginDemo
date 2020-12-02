package com.zzz.transformplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class TransformPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // 获取Android扩展
        def android = project.getProperties().get("android")
        // 注册Transform，其实就是添加了Task
        android.registerTransform(new ToastTransform(project))

        // 这里只是随便定义一个Task而已，和Transform无关
        project.task('JustTask') {
            doLast {
                println('InjectTransform task')
            }
        }

    }
}