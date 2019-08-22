package com.yuanshijia.bean;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author yuanshijia
 * @date 2019-08-09
 * @description
 */
@Data
@ToString
public class BeanDefinition {

    /**
     * 对象的name
     */
    private String name;

    /**
     * 对象的类名
     */
    private String className;

    /**
     * 如果是接口，接口的名称
     */
    private String interfaceName;

    /**
     * 构造函数的传参的列表
     */
    private List<ConstructorArg> constructorArgs;

    /**
     * 需要注入的参数列表
     */
    private List<PropertyArg> propertyArgs;

    
}
