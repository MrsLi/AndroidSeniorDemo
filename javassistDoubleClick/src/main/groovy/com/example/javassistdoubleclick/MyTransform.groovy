package com.example.javassistdoubleclick

import com.android.SdkConstants
import com.android.aaptcompiler.SDKConstants
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.AnnotationsAttribute
import org.gradle.api.Project
import org.gradle.internal.impldep.org.apache.ivy.util.FileUtil

class MyTransform extends Transform{

    def pool = ClassPool.default //定义字节码池
    def project

    MyTransform(Project project){
        this.project = project
    }

    /**
     * transform的文件夹名
     * @return
     */
    @Override
    String getName() {
        return "ModifyTransform"
    }

    /**
     * 自定义的transform的接受输入的类型
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * 本transform的作用范围,整个project
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * 是否是增量编译
     * @return
     */
    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        project.android.bootClasspath.each{
            pool.appendClassPath(it.absolutePath)
        }
        def inputs = transformInvocation.inputs
        def outputProvider = transformInvocation.outputProvider
        inputs.each {

            it.jarInputs.each {
                def preFileName = it.file.absolutePath ///文件的绝对路径
                pool.insertClassPath(preFileName)
                def dest = outputProvider.getContentLocation(it.name,it.contentTypes,it.scopes, Format.JAR)
                FileUtils.copyFile(it.file,dest)

            }

            it.directoryInputs.each {
                def preFileName = it.file.absolutePath ///文件的绝对路径
                pool.insertClassPath(preFileName)
                findTarget(it.file,preFileName)

                //获取output目录
                def dest = outputProvider.getContentLocation(it.name,it.contentTypes,it.scopes, Format.DIRECTORY)
                //将input的目录复制到output指定目录
                FileUtils.copyDirectory(it.file,dest)

            }
        }
    }


    /**
     *
     * @param dir
     * @param fileName dir的文件夹路径
     */
    void findTarget(File dir,String fileName){
        //递归寻找.class结尾的文件
        if (dir.isDirectory()){
            dir.listFiles().each {
                findTarget(it,fileName)
            }
        }else{
            modify(dir,fileName)
        }
    }


    void  modify(File dir,String fileName){
        def filePath =dir.absolutePath
        if(!filePath.endsWith(SdkConstants.DOT_CLASS)){
            return
        }
        if (filePath.contains('R$')||filePath.contains('R.class')||filePath.contains('BuildConfig.class')){
            return
        }
        //获得全类名,取得字节码对象,修改
        def className = filePath.replace(fileName,"").replace("\\",".").replace("/",".")
        def name = className.replace(SdkConstants.DOT_CLASS,"").substring(1)

        CtClass ctClass = pool.get(name)
        if(name.contains("com.example.javassist")){
            def body = "android.util.Log.e(\"JavassistActivity\", \"logCode:Before \");"   //要插入的代码
            addCode(ctClass,body,fileName)
        }
    }


    /**
     * 插入代码
     * @param ctClass
     * @param body
     * @param fileName
     */
    void addCode(CtClass ctClass,String body,String fileName){
        CtMethod[] ctMethods=ctClass.getDeclaredMethods()
        ctMethods.each {
            it.insertAfter(body)
            println("LongName:${it.getLongName()}")
            Object[] annotations = it.getAnnotations()
            if (annotations!=null&&annotations.length>0){
                println("遍历到注解不为空的方法")
                annotations.each {
                    println("注解是:${it.toString()}")
                }
            }
        }

        ctClass.writeFile(fileName)
        ctClass.detach()   //释放内存
    }
}