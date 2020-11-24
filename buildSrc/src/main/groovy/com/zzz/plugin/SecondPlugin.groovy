package com.zzz.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 第二种Plugin编写
 * 创建(只能是)buildSrc 的moudule，选择java or kotlin module 只保留build.gradle文件和src/main目录
 * 如果有 ，需要删除setting.grale 的 include ':buildSrc'
 * 手动删除和重新创建 groovy ,resources目录 以及对应子文件
 * 创建插件的groovy文件和包名.properties
 *
 * 外部引用的插件名称就是properties文件的名称，并不是包名.插件名
 */
class SecondPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("this is SecondPlugin")
    }
}