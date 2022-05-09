package com.posuoren.pattern.construct.factory.normal;

import com.posuoren.pattern.construct.factory.normal.dog.Corgi;
import com.posuoren.pattern.construct.factory.normal.dog.Dog;
import com.posuoren.pattern.construct.factory.normal.dog.Husky;
import com.posuoren.pattern.construct.factory.normal.dog.Shepherd;

/**
 * @Description
 * @Author leiyutian
 * @Date 2020/2/9 19:54
 */
public class DogFacotry {
    public Dog getDog(String name){
        if(name == null){
            return null;
        }
        if(name.equalsIgnoreCase("Husky")){
            return new Husky();
        }else if(name.equalsIgnoreCase("Corgi")){
            return new Corgi();
        }else if(name.equalsIgnoreCase("Shepherd")){
            return new Shepherd();
        }
        return null;
    }
}
