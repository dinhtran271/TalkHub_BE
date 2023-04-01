package com.talkhub.util.json;

import com.google.gson.*;

public class GsonUtil {
    public static Gson gson = new Gson();
    public static Gson beautyGson = new GsonBuilder().setPrettyPrinting().create();
    public static String toJsonString(Object obj) {
        return gson.toJson(obj);
    }
    public static String toBeautifulJsonStringGson(Object obj) {
        return beautyGson.toJson(obj);
    }

    public static JsonArray toJsonArray(String json) {
        try {
            return gson.fromJson(json, JsonArray.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonObject toJsonObject(String json) {
        try {
            return gson.fromJson(json, JsonObject.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isJsonNull(JsonElement element){
        return element.isJsonNull() || element.getAsString().equals("null");
    }
    public static Object toJsonObject(String json, Class clazz) {
        return gson.fromJson(json, clazz);
    }

    public static JsonArray toJsonArray(int[][] arr) {
        try {
            JsonArray array = new JsonArray();
            for(int i = 0; i < arr.length; i++){
                array.add(toJsonArray(arr[i]));
            }
            return array;
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonArray toJsonArray(long[][] arr) {
        try {
            JsonArray array = new JsonArray();
            for(int i = 0; i < arr.length; i++){
                array.add(toJsonArray(arr[i]));
            }
            return array;
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonArray toJsonArray(double[][] arr) {
        try {
            JsonArray array = new JsonArray();
            for(int i = 0; i < arr.length; i++){
                array.add(toJsonArray(arr[i]));
            }
            return array;
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonArray toJsonArray(int[] arr){
        JsonArray array = new JsonArray();
        for(int i = 0; i < arr.length; i++){
            array.add(arr[i]);
        }
        return array;
    }

    public static JsonArray toJsonArray(long[] arr){
        JsonArray array = new JsonArray();
        for(int i = 0; i < arr.length; i++){
            array.add(arr[i]);
        }
        return array;
    }

    public static JsonArray toJsonArray(double[] arr){
        JsonArray array = new JsonArray();
        for(int i = 0; i < arr.length; i++){
            array.add(arr[i]);
        }
        return array;
    }

    public static int[] toIntArray(JsonArray arr){
        int[] array = new int[arr.size()];
        for(int i = 0; i < arr.size(); i++){
            array[i] = arr.get(i).getAsInt();
        }
        return array;
    }

    public static long[] toLongArray(JsonArray arr){
        long[] array = new long[arr.size()];
        for(int i = 0; i < arr.size(); i++){
            array[i] = arr.get(i).getAsLong();
        }
        return array;
    }

    public static double[] toDoubleArray(JsonArray arr){
        double[] array = new double[arr.size()];
        for(int i = 0; i < arr.size(); i++){
            array[i] = arr.get(i).getAsDouble();
        }
        return array;
    }

    public static int[][] toTwoDimensionIntArray(JsonArray arr){
        int[][] result = new int[arr.size()][];
        for(int i = 0; i < arr.size(); i++){
            JsonArray jsonArray = arr.get(i).getAsJsonArray();
            result[i] = toIntArray(jsonArray);
        }
        return result;
    }
}
