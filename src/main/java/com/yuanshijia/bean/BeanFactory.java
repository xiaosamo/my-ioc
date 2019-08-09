package com.yuanshijia.bean;

/**
 * @author yuanshijia
 * @date 2019-08-09
 * @description
 */
public interface BeanFactory {
    /**
     * 根据类名获取bean
     * @param name
     * @return
     * @throws Exception
     */
    Object getBean(String name) throws Exception;

}
