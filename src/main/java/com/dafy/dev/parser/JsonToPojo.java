package com.dafy.dev.parser;

import com.dafy.dev.util.SourceCodeUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public final class JsonToPojo {

    private static final String DEFAULT_FILE_PATH = "./src/main/java";

    private static final Logger logger = LoggerFactory.getLogger(JsonToPojo.class);

    private static final JCodeModel codeModel = new JCodeModel();

    public static void fromInputStream(InputStream inputStream, String fullClassName, File out,
                                       boolean serializable,
                                       boolean addJsonAnnotation, boolean addToString) {

        try {
            Reader reader = new InputStreamReader(inputStream);
            JsonElement root = new JsonParser().parse(reader);

            generateCode(root, fullClassName, out, serializable, addJsonAnnotation, addToString);
        } catch (Exception e) {

            throw new IllegalStateException(e);
        }
    }

    public static void fromJsonNode(JsonElement root, String fullClassName,
                                    File out, boolean serializable, boolean addJsonAnnotation,
                                    boolean addToString) {

        try {
            generateCode(root, fullClassName, out, serializable, addJsonAnnotation, addToString);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("{},{}", root, e);
            //throw new IllegalStateException(e);
        }
    }

    static void generateCode(JsonElement root, String fullClassName, File out, boolean serializable,
                             boolean addJsonAnnotation, boolean addToString) {
        int lastIndexDot = fullClassName.lastIndexOf(".");
        String packageName = fullClassName.substring(0, lastIndexDot);
        String className = fullClassName.substring(lastIndexDot + 1, fullClassName.length());

        generateClass(packageName, className, root, serializable, addJsonAnnotation, addToString);

        try {
            codeModel.build(out);

        } catch (Exception e) {
            throw new IllegalStateException("Couldn't generate Pojo", e);
        }
    }

    private static JClass generateClass(String packageName, String className,
                                        JsonElement jsonElement,
                                        boolean serializable,
                                        boolean addJsonAnnotation,
                                        boolean addToString) {

        JClass elementClass = null;

        if (jsonElement.isJsonNull()) {
            elementClass = codeModel.ref(Object.class);

        } else if (jsonElement.isJsonPrimitive()) {

            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            elementClass = getClassForPrimitive(jsonPrimitive);

        } else if (jsonElement.isJsonArray()) {

            JsonArray array = jsonElement.getAsJsonArray();
            elementClass = getClassForArray(packageName, className, array, serializable,
                    addJsonAnnotation, addToString);

        } else if (jsonElement.isJsonObject()) {

            JsonObject jsonObj = jsonElement.getAsJsonObject();
            elementClass = getClassForObject(packageName, className, jsonObj, serializable,
                    addJsonAnnotation, addToString);
        }

        if (elementClass != null) {
            return elementClass;
        }

        throw new IllegalStateException("jsonElement type not supported");
    }

    private static JClass getClassForObject(String packageName, String className,
                                            JsonObject jsonObj,
                                            boolean serializable, boolean addJsonAnnotation,
                                            boolean addToString) {

        Map<String, JClass> fields = new LinkedHashMap<String, JClass>();

        for (Entry<String, JsonElement> element : jsonObj.entrySet()) {

            String fieldName = element.getKey();
            String fieldUppercase = SourceCodeUtil.uppercase(fieldName, false);

            JClass elementClass = generateClass(packageName, fieldUppercase, element.getValue(),
                    serializable, addJsonAnnotation, addToString);
            fields.put(fieldName, elementClass);
        }

        String classPackage = packageName + "." + className;
        generatePojo(classPackage, fields, serializable, addJsonAnnotation, addToString);

        JClass jclass = codeModel.ref(classPackage);
        return jclass;
    }

    private static JClass getClassForArray(String packageName, String className, JsonArray array,
                                           boolean serializable,
                                           boolean addJsonAnnotation,
                                           boolean addToString) {

        if (className.endsWith("List")) {
            className = className.substring(0, className.length() - 4) + "Item";
        } else if (className.endsWith("Arr")) {
            className = className.substring(0, className.length() - 3) + "Item";
        } else if (className.endsWith("Array")) {
            className = className.substring(0, className.length() - 5) + "Item";
        }

        JClass narrowClass = codeModel.ref(Object.class);
        if (array.size() > 0) {
            String elementName = className;
            if (className.endsWith("ies")) {
                elementName = elementName.substring(0, elementName.length() - 3) + "y";
            } else if (elementName.endsWith("s")) {
                elementName = elementName.substring(0, elementName.length() - 1);
            }

            narrowClass = generateClass(packageName, elementName, array.get(0), serializable,
                    addJsonAnnotation, addToString);
        }

        String narrowName = narrowClass.name();
        Class<?> boxedClass = null;
        if (narrowName.equals("int")) {
            boxedClass = Integer.class;
        } else if (narrowName.equals("long")) {
            boxedClass = Long.class;
        } else if (narrowName.equals("double")) {
            boxedClass = Double.class;
        }
        if (boxedClass != null) {
            narrowClass = codeModel.ref(boxedClass);
        }

        JClass listClass = codeModel.ref(List.class).narrow(narrowClass);
        return listClass;
    }

    private static JClass getClassForPrimitive(JsonPrimitive jsonPrimitive) {

        JClass jClass = null;

        if (jsonPrimitive.isNumber()) {
            Number number = jsonPrimitive.getAsNumber();
            double doubleValue = number.doubleValue();

            if (doubleValue != Math.round(doubleValue)) {
                jClass = codeModel.ref("double");
            } else {
                long longValue = number.longValue();
                if (longValue >= Integer.MAX_VALUE) {
                    jClass = codeModel.ref("long");
                } else {
                    jClass = codeModel.ref("int");
                }
            }
        } else if (jsonPrimitive.isBoolean()) {
            jClass = codeModel.ref("boolean");
        } else {
            jClass = codeModel.ref(String.class);
        }
        return jClass;
    }

    public static void generatePojo(String classFullName, Map<String, JClass> fields,
                                    boolean serializable,
                                    boolean addJsonAnnotation, boolean addToString) {
        logger.debug("generatePojo {},{}", classFullName, serializable);
        try {
            JDefinedClass definedClass = codeModel._class(classFullName);

            //生成序列化
            if (serializable) {
                definedClass._implements(Serializable.class);
                JFieldVar field1 = definedClass
                        .field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, long.class,
                                "serialVersionUID");
                long ser = new Random(System.currentTimeMillis()).nextLong();
                if (ser > 0)
                    ser = -ser;
                field1.init(JExpr.lit(ser));
            }

            for (Entry<String, JClass> field : fields.entrySet()) {

                addGetterSetter(definedClass, field.getKey(), field.getValue(), addJsonAnnotation);
            }

            generateToString(definedClass, classFullName, fields);

        } catch (Exception e) {
            throw new IllegalStateException("Couldn't generate Pojo", e);
        }

    }

    //生成getter setter
    private static void addGetterSetter(JDefinedClass definedClass, String fieldName,
                                        JClass fieldType, boolean addJsonAnnotation) {

        String fieldNameWithFirstLetterToUpperCase = uppercase(fieldName, false);

        JFieldVar field = definedClass.field(JMod.PRIVATE, fieldType, uppercase(fieldName, true));

        if (addJsonAnnotation) {
            //加注释
            JAnnotationUse jsonAnnotation = field.annotate(JsonProperty.class);
            jsonAnnotation.param("value", SourceCodeUtil.upperCaseToJson(fieldName));
        }

        String getterPrefix = "get";
        String fieldTypeName = fieldType.fullName();
        if (fieldTypeName.equals("boolean") || fieldTypeName.equals("java.lang.Boolean")) {
            getterPrefix = "is";
        }
        String getterMethodName = getterPrefix + fieldNameWithFirstLetterToUpperCase;
        JMethod getterMethod = definedClass.method(JMod.PUBLIC, fieldType, getterMethodName);
        JBlock block = getterMethod.body();
        block._return(field);

        String setterMethodName = "set" + fieldNameWithFirstLetterToUpperCase;
        JMethod setterMethod = definedClass.method(JMod.PUBLIC, Void.TYPE, setterMethodName);

        String setterParameter = uppercase(fieldName, true);
        setterMethod.param(fieldType, setterParameter);
        setterMethod.body()
                .assign(JExpr._this().ref(uppercase(fieldName, true)), JExpr.ref(setterParameter));
    }

    private static String getFirstUppercase(String word) {

        String firstLetterToUpperCase = word.substring(0, 1).toUpperCase();
        if (word.length() > 1) {
            firstLetterToUpperCase += word.substring(1, word.length());
        }
        return firstLetterToUpperCase;
    }

    private static String uppercase(String word, boolean firstLowerCase) {

        String ret = "";

        String parts[] = word.split("_");

        if (parts.length > 1) {
            for (int i = 0; i < parts.length; i++) {
                String w = "";
                String first = (i == 0 && firstLowerCase) ?
                        parts[i].substring(0, 1) :
                        parts[i].substring(0, 1).toUpperCase();
                w = first;
                if (parts[i].length() > 1) {
                    w += parts[i].substring(1, parts[i].length());
                }
                ret += w;
            }
        } else {
            if (!firstLowerCase) {
                String firstLetterToUpperCase = word.substring(0, 1).toUpperCase();
                if (word.length() > 1) {
                    firstLetterToUpperCase += word.substring(1, word.length());
                }
                ret = firstLetterToUpperCase;
            } else {
                ret = word;
            }
        }

        return ret;
    }

    //todo
    private static void generateToString(JDefinedClass definedClass,
                                         String className, Map<String, JClass> fields) {
        JMethod toStringMethod = definedClass.method(JMod.PUBLIC, String.class, "toString");
        StringBuilder sb = new StringBuilder();
        JBlock body = toStringMethod.body();

        sb.append("\"").append(SourceCodeUtil.getClassName(className)).append("{\"+\n");
        int i = 0;
        for (Entry<String, JClass> f : fields.entrySet()) {
            String key = uppercase(f.getKey(), true);
            sb.append(i++ == 0 ? "\t\t\t\t\"" : "\t\t\t\t\", ").append(key).append("=\" + ")
                    .append(key).append(" ").append(" + \n");
        }
        sb.append("\t\t\t\t\"}\"");
        body._return(JExpr.direct(sb.toString()));

    }
}
