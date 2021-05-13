package com.example.annotation_compiler;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class ElementForType {
    //所有绑定View的成员变量节点
    List<VariableElement> variableElements;
    //所有点击事件的方法的节点结合
    List<ExecutableElement> methodElements;

    public List<VariableElement> getVariableElements() {
        return variableElements;
    }

    public void setVariableElements(List<VariableElement> variableElements) {
        this.variableElements = variableElements;
    }

    public List<ExecutableElement> getMethodElements() {
        return methodElements;
    }

    public void setMethodElements(List<ExecutableElement> methodElements) {
        this.methodElements = methodElements;
    }
}
