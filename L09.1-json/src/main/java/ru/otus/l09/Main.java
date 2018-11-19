package ru.otus.l09;

import com.google.gson.GsonBuilder;
import ru.otus.l09.objects.ArraysTestObject;
import ru.otus.l09.objects.CollectionsTestObject;
import ru.otus.l09.objects.ComplexTestObject;
import ru.otus.l09.objects.PrimitiveTestObject;

import java.util.Collections;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        PrimitiveTestObject primitiveTestObject = getRandomPrimitiveTestObject();
        CollectionsTestObject collectionsTestObject = getCollectionsTestObject();
        ArraysTestObject arraysTestObject = getArraysTestObject();
        ComplexTestObject complexTestObject = getComplexTestObject();

        compareJsonConversions(primitiveTestObject, "Primitive types");
        compareJsonConversions(arraysTestObject, "Arrays");
        compareJsonConversions(collectionsTestObject, "Collections");
        compareJsonConversions(complexTestObject, "Complex object");

        compareJsonConversions(new int[]{1, 2, 3, 5}, "ArrayThatHaveNoWrap");
        compareJsonConversions(new String[]{"1"}, "StringArrayThatHaveNoWrap");
        compareJsonConversions(Collections.singletonList(1), "ListThatHaveNoWrap");
        compareJsonConversions(Collections.singletonMap(1, "Val"), "MapThatHaveNoWrap");

        compareJsonConversions(null, "NullThatHaveNoWrap");
        compareJsonConversions(1, "IntegerThatHaveNoWrap");
        compareJsonConversions(10L, "LongThatHaveNoWrap");
        compareJsonConversions('c', "CharacterThatHaveNoWrap");
        compareJsonConversions("Test string", "StringThatHaveNoWrap");
    }

    public static void convertToJson(Object object, String text){
        System.out.println("Example: " + text);
        System.out.println(JsonObjectWriter.toJson(object));
        System.out.println();
    }

    public static void compareJsonConversions(Object object, String text){
        System.out.println("Example: " + text);
        System.out.println(JsonObjectWriter.toJson(object));
        System.out.println(new GsonBuilder().create().toJson(object));
        System.out.println();
    }

    public static PrimitiveTestObject getRandomPrimitiveTestObject(){
        PrimitiveTestObject primitiveTestObject = new PrimitiveTestObject();

        Random rand = new Random();

        primitiveTestObject.intTest = rand.nextInt(10000);
        primitiveTestObject.shortTest = (short) rand.nextInt(1 << 15);
        primitiveTestObject.longTest = rand.nextLong();
        primitiveTestObject.doubleTest = rand.nextDouble();
        primitiveTestObject.floatTest = rand.nextFloat();
        primitiveTestObject.boolTest = rand.nextBoolean();
        primitiveTestObject.byteTest = (byte) rand.nextInt(255);

        primitiveTestObject.charTest = (char)(rand.nextInt(26) + 'a');
        primitiveTestObject.strTest = "JSON";

        return primitiveTestObject;
    }

    public static ArraysTestObject getArraysTestObject(){
        ArraysTestObject arraysTestObject = new ArraysTestObject();
        Random rand = new Random();

        arraysTestObject.boolArrayTest = new boolean[] {rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean()};
        arraysTestObject.intArrayTest = new int[] {rand.nextInt(100), rand.nextInt(100)};
        arraysTestObject.floatArrayTest = new float[] {rand.nextFloat(), rand.nextFloat()};

        return arraysTestObject;
    }

    public static CollectionsTestObject getCollectionsTestObject(){
        CollectionsTestObject collectionsTestObject = new CollectionsTestObject();

        collectionsTestObject.integerList.add(1);
        collectionsTestObject.integerList.add(2);

        collectionsTestObject.stringSet.add("Rainbow");
        collectionsTestObject.stringSet.add("Deep Purple");

        collectionsTestObject.testMap.put("deepPurple", "Blackmore");
        collectionsTestObject.testMap.put("rainbow", "Dio");

        return collectionsTestObject;
    }

    public static ComplexTestObject getComplexTestObject(){
        ComplexTestObject complexTestObject = new ComplexTestObject();

        complexTestObject.setIntTest(new Random().nextInt(100)); ;
        complexTestObject.transientStringTest = "Not included";

        complexTestObject.primitiveTestObject = getRandomPrimitiveTestObject();

        for (int i = 0; i < 3; i++){
            complexTestObject.primitiveTestObjectSet.add(getRandomPrimitiveTestObject());
        }

        return complexTestObject;
    }

}
