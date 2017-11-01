package io.writer;

import java.util.*;

public class Test<T extends Object> {
    public static void main(String[] args) {

//        String[] idValues = "asd|dfg".split("|");
//        System.out.println((idValues[0]));

        Integer resultValue=34;

        resultValue=new Integer(resultValue.toString().substring(1));
        System.out.println(resultValue);

    }

}
