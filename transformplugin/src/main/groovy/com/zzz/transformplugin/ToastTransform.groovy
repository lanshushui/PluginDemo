package com.zzz.transformplugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.apache.commons.codec.digest.DigestUtils
import com.android.build.api.transform.Format
import java.io.IOException;
import java.util.Set;

/**
 * author : chentao
 * date : 2020/12/2
 * email: chentao3@yy.com
 */
public class ToastTransform extends Transform {

    private Project mProject;

    public ToastTransform(Project project) {
        this.mProject = project;
    }
    // 设置我们自定义的Transform对应的Task名称
    // 类似：transformClassesWithPreDexForXXX
    // 这里应该是：transformClassesWithInjectTransformForxx
    @Override
    public String getName() {
        return "ToastTransform";
    }
    // 指定输入的类型，通过这里的设定，可以指定我们要处理的文件类型
    //  这样确保其他类型的文件不会传入
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }
    // 指定Transform的作用范围
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }
    // 当前Transform是否支持增量编译
    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        println '--------------------transform Start-------------------'

        // Transform的inputs有两种类型，一种是目录，一种是jar包，要分开遍历
        transformInvocation.inputs.each {
            TransformInput input ->

                // 遍历文件夹
                //文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
                println '--------------------transform input.directoryInputs-------------------'
                input.directoryInputs.each {
                    DirectoryInput directoryInput ->
                        // 注入代码
                        MyInjectByJavassit.injectToast(directoryInput.file.absolutePath, mProject)

                        // 获取输出目录
                        def dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
                                directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)

                        println("directory output dest: $dest.absolutePath")
                        // 将input的目录复制到output指定目录
                        FileUtils.copyDirectory(directoryInput.file, dest)

                }
                //对类型为jar文件的input进行遍历
                println '--------------------transform input.jarInputs-------------------'
                input.jarInputs.each {
                        //jar文件一般是第三方依赖库jar文件
                    JarInput jarInput ->
                        // 重命名输出文件（同目录copyFile会冲突）
                        def jarName = jarInput.name
                        println("jar: $jarInput.file.absolutePath")
                        def md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
                        if (jarName.endsWith('.jar')) {
                            jarName = jarName.substring(0, jarName.length() - 4)
                        }
                        def dest = transformInvocation.outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                        println("jar output dest: $dest.absolutePath")
                        FileUtils.copyFile(jarInput.file, dest)
                }
        }
        println '---------------------transform End-------------------'
    }
}
