package ru.otus.l09.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComplexTestObject {

    private int intTest;
    public transient String transientStringTest;
    public PrimitiveTestObject primitiveTestObject;
    public Set<PrimitiveTestObject> primitiveTestObjectSet = new HashSet<>();

    public Integer getIntTest(){
        return intTest;
    }

    public void setIntTest(int intTest) {
        this.intTest = intTest;
    }
}
