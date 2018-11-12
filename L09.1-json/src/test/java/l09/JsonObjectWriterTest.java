package l09;

import com.google.gson.GsonBuilder;
import ru.otus.l09.JsonObjectWriter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.otus.l09.objects.ArraysTestObject;
import ru.otus.l09.objects.CollectionsTestObject;
import ru.otus.l09.objects.ComplexTestObject;
import ru.otus.l09.objects.PrimitiveTestObject;

import static ru.otus.l09.Main.getRandomPrimitiveTestObject;
import static ru.otus.l09.Main.getComplexTestObject;
import static ru.otus.l09.Main.getCollectionsTestObject;
import static ru.otus.l09.Main.getArraysTestObject;


public class JsonObjectWriterTest {

    @Test
    public void writePrimitiveTestObjectTest(){
        PrimitiveTestObject testObject = getRandomPrimitiveTestObject();
        assertEquals(JsonObjectWriter.toJson(testObject),
                new GsonBuilder().create().toJson(testObject));
    }

    @Test
    public void writeArraysTestObjectTest(){
        ArraysTestObject testObject = getArraysTestObject();
        assertEquals(JsonObjectWriter.toJson(testObject),
                new GsonBuilder().create().toJson(testObject));
    }

    @Test
    public void writeCollectionsTestObjectTest(){
        CollectionsTestObject testObject = getCollectionsTestObject();
        assertEquals(JsonObjectWriter.toJson(testObject),
                new GsonBuilder().create().toJson(testObject));
    }

    @Test
    public void writeComplexTestObjectTest(){
        ComplexTestObject testObject = getComplexTestObject();
        assertEquals(JsonObjectWriter.toJson(testObject),
                new GsonBuilder().create().toJson(testObject));
    }

}
