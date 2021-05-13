package com.example.annotation_compiler;

import com.example.annotation.BindView;
import com.example.annotation.OnClick;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class) //注册这是一个注解处理器,才能被java的虚拟机调用
public class AnnotationCompiler extends AbstractProcessor {

    Filer filer;
    ProcessingEnvironment processingEnv;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        this.processingEnv = processingEnv;

    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    /**
     * 返回需要处理的注解
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindView.class.getCanonicalName());
        types.add(OnClick.class.getCanonicalName());
        return types;
    }

    /**
     * 处理注解
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<TypeElement,ElementForType> map = finAndParseTarget(roundEnv);
        //创建文件,写入代码
        if(map.size()>0){
            Iterator<TypeElement> iterator = map.keySet().iterator();
            Writer writer = null;
            while (iterator.hasNext()){
                TypeElement typeElement = iterator.next();
                ElementForType elementForType = map.get(typeElement);
                //获取类名
                String className = typeElement.getSimpleName().toString();
                //获取包名
                String packageName = getpackageName(typeElement);
                //创建一个新的类名
                String newClassName = className+"$$ViewBinder";
                //创建文件
                try {
                    JavaFileObject sourceFile = filer.createSourceFile(packageName + "." + newClassName);
                    writer = sourceFile.openWriter();
                    StringBuffer stringBuffer = getStringBuffer(packageName,newClassName,typeElement,elementForType);
                    writer.write(stringBuffer.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
        return false;
    }

    private StringBuffer getStringBuffer(String packageName,String newClassName,TypeElement typeElement,ElementForType elementForType) {
        StringBuffer stringBuffer  = new StringBuffer();
        stringBuffer.append("package "+packageName+";\n");
        stringBuffer.append("import android.view.View;\n");
        stringBuffer.append("public class "+newClassName+"{\n");
        stringBuffer.append("public "+newClassName+"(final "+typeElement.getQualifiedName().toString()+" target){\n");
        //循环遍历注解,拼接代码
        if (elementForType != null&&elementForType.getVariableElements() != null&&elementForType.getVariableElements().size()>0) {
            for (VariableElement variableElement : elementForType.getVariableElements()) {
                //获取变量的类型
                TypeMirror typeMirror = variableElement.asType();
                //获取变量名
                String variableName = variableElement.getSimpleName().toString();
                int rsId = variableElement.getAnnotation(BindView.class).value();
                stringBuffer.append("target."+variableName+"=("+typeMirror+")target.findViewById("+rsId+");\n");
            }

        }

        if (elementForType != null&&elementForType.getMethodElements() != null&&elementForType.getMethodElements().size()>0) {
            for (ExecutableElement executableElement : elementForType.getMethodElements()) {
                int[] rsIds = executableElement.getAnnotation(OnClick.class).value();
                String methodName = executableElement.getSimpleName().toString();
                for (int rsId : rsIds) {
                    stringBuffer.append("target.findViewById("+rsId+").setOnClickListener(new View.OnClickListener(){"+"\n");
                    stringBuffer.append("public void onClick(View p0){\n");
                    stringBuffer.append("target."+methodName+"(p0);\n");
                    stringBuffer.append("}\n});\n");
                }
            }

        }

        stringBuffer.append("}\n");

        stringBuffer.append("}\n");
        return stringBuffer;
    }

    private Map<TypeElement, ElementForType> finAndParseTarget(RoundEnvironment roundEnv) {
        Map<TypeElement,ElementForType> map = new HashMap<>();
        Set<? extends Element> variableElements = roundEnv.getElementsAnnotatedWith(BindView.class);
        Set<? extends Element> methodElements = roundEnv.getElementsAnnotatedWith(OnClick.class);

        //遍历所有的节点,把他们和类节点对应
        for (Element element : variableElements) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            ElementForType elementForType = map.get(typeElement);
            List<VariableElement> variableElements1;
            if (elementForType != null) {
                variableElements1 = elementForType.getVariableElements();
                if (variableElements1 == null) {
                    variableElements1 = new ArrayList<>();
                    elementForType.setVariableElements(variableElements1);
                }
            }else {
                elementForType = new ElementForType();
                variableElements1 =new ArrayList<>();
                elementForType.setVariableElements(variableElements1);
                if (!map.containsKey(typeElement)) {
                    map.put(typeElement,elementForType);
                }

            }
            variableElements1.add(variableElement);
        }


        //方法注解的归类


        for (Element methodElement : methodElements) {
            ExecutableElement executableElement = (ExecutableElement) methodElement;
            TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();

            ElementForType elementForType = map.get(typeElement);
            List<ExecutableElement> executableElements;
            if (elementForType != null) {
                executableElements = elementForType.getMethodElements();
                if (executableElements == null) {
                    executableElements = new ArrayList<>();
                }
                elementForType.setMethodElements(executableElements);
            }else {
                elementForType = new ElementForType();
                executableElements = new ArrayList<>();
                elementForType.setMethodElements(executableElements);
                if (!map.containsKey(typeElement)) {
                    map.put(typeElement,elementForType);
                }
            }

            executableElements.add(executableElement);
        }

        return map;
    }

    /**
     * 根据节点获取包名
     * @param typeElement
     */

    private String getpackageName(Element typeElement) {
        PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(typeElement);
        Name qualifiedName = packageElement.getQualifiedName();
        return qualifiedName.toString();
    }
    public void logUtil(String log){
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE,log);
    }
}