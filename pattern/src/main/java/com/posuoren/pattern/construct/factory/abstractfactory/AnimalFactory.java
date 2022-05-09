package com.posuoren.pattern.construct.factory.abstractfactory;

import com.posuoren.pattern.construct.factory.abstractfactory.cat.Cat;
import com.posuoren.pattern.construct.factory.abstractfactory.cat.CatFactory;
import com.posuoren.pattern.construct.factory.normal.DogFacotry;
import com.posuoren.pattern.construct.factory.normal.dog.Dog;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/11 10:30
 */
public class AnimalFactory extends AbstractAnimalFactory {
    private CatFactory catFactory;
    private DogFacotry dogFacotry;

    public AnimalFactory(CatFactory catFactory, DogFacotry dogFacotry){
        this.catFactory = catFactory;
        this.dogFacotry = dogFacotry;
    }
    /**
     * 获取猫
     *
     * @param catName 猫的品种名称
     * @return
     */
    @Override
    Cat getCat(String catName) {
        return catFactory.getCat(catName);
    }

    /**
     * 获取狗
     *
     * @param dogName 狗的品种名称
     * @return
     */
    @Override
    Dog getDog(String dogName) {
        return dogFacotry.getDog(dogName);
    }
}
