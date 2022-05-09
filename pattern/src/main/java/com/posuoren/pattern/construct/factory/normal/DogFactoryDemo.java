package com.posuoren.pattern.construct.factory.normal;

import com.posuoren.pattern.construct.factory.normal.dog.Dog;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/9 19:58
 */
public class DogFactoryDemo {
    public static void main(String[] args){
        DogFacotry dogFacotry = new DogFacotry();
        Dog dog1 = dogFacotry.getDog("Husky");
        dog1.play();

        Dog dog2 = dogFacotry.getDog("Corgi");
        dog2.play();

        Dog dog3 = dogFacotry.getDog("Shepherd");
        dog3.play();
    }
}
