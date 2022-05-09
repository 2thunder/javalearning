package com.posuoren.pattern.construct.factory.abstractfactory;

import com.posuoren.pattern.construct.factory.abstractfactory.cat.Cat;
import com.posuoren.pattern.construct.factory.abstractfactory.cat.CatFactory;
import com.posuoren.pattern.construct.factory.normal.DogFacotry;
import com.posuoren.pattern.construct.factory.normal.dog.Dog;

import java.util.Optional;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/11 10:33
 */
public class AnimalFactoryDemo {

    public static void main(String[] args){
        AnimalFactory animalFactory = new AnimalFactory(new CatFactory(), new DogFacotry());
        Optional.ofNullable(animalFactory.getCat("AmericanShortHair")).ifPresent(Cat::sayHobby);

        Optional.ofNullable(animalFactory.getDog("Husky")).ifPresent(Dog::play);
    }
}
