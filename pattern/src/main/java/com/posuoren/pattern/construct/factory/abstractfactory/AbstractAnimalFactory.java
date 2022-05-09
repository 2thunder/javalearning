package com.posuoren.pattern.construct.factory.abstractfactory;

import com.posuoren.pattern.construct.factory.abstractfactory.cat.Cat;
import com.posuoren.pattern.construct.factory.normal.dog.Dog;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/11 10:12
 */
public abstract class AbstractAnimalFactory {

    /**
     * 获取猫
     * @param catName 猫的品种名称
     * @return
     */
    abstract Cat getCat(String catName);

    /**
     * 获取狗
     * @param dogName 狗的品种名称
     * @return
     */
    abstract Dog getDog(String dogName);
}
