package com.zzz.remoteplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 创建Android libary 的moudule
 * 将Module里的内容删除，只保留build.gradle文件和src/main目录，同时移除build.gradle文件里的内容
 * 建立Gradle插件目录
 * 修改build.gradle文件
 * 创建groovy文件和properties文件
 * 发布打包插件
 *
 * 使用插件 （项目根目录gralde加入仓库地址 并 classpath进来，在app目录进行apply）
 */
class ThirdPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("this is ThirdPlugin")
    }
}