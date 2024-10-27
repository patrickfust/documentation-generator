package dk.fust.docgen.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.util.Assert;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Loads and parse a yaml- or json-file containing list of configurations
 */
public class DocumentationConfigurationLoaderService {

    private static final String CLASS_NAME = "className";
    private final DocumentationService documentationService = new DocumentationService();

    /**
     * Reads generator configurations from a yaml- or json-file
     * @param configurationFile json or yaml file
     * @return parsed list of GeneratorConfiguration
     * @throws IOException an error occurred
     */
    public List<GeneratorConfiguration> readConfigurations(File configurationFile) throws IOException {
        List<GeneratorConfiguration> configurations = new ArrayList<>();
        JsonNode jsonNode = documentationService.loadFileAsTree(configurationFile);
        for (JsonNode node : jsonNode) {
            configurations.add((GeneratorConfiguration) createAndPopulateInstance(node));
        }
        return configurations;
    }

    private Object createAndPopulateInstance(JsonNode node) {
        try {
            Assert.isNotNull(node.get(CLASS_NAME), "Missing " + CLASS_NAME + " for element");
            String className = node.get(CLASS_NAME).textValue();
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            for (Iterator<String> it = node.fieldNames(); it.hasNext(); ) {
                String fieldName = it.next();
                setValue(fieldName, node.get(fieldName), clazz, instance);
            }
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void setValue(String fieldName, JsonNode node, Class<T> clazz, Object instance) throws ReflectiveOperationException {
        try {
            Class<?> dataType = clazz.getDeclaredField(fieldName).getType();
            String capitalizedName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method method;
            try {
                method = clazz.getMethod("set" + capitalizedName, dataType);
            } catch (NoSuchMethodException nsme) {
                // Perhaps without capitalization
                method = clazz.getMethod("set" + fieldName, dataType);
            }
            method.invoke(instance, convertObject(node, dataType));
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            // It's OK
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object convertObject(JsonNode jsonNode, Class<?> dataType) {
        if (dataType == String.class) {
            return jsonNode.textValue();
        }
        if (dataType == File.class) {
            return new File(jsonNode.textValue());
        }
        if (dataType == int.class || dataType == Integer.class) {
            return jsonNode.intValue();
        }
        if (dataType == long.class || dataType == Long.class) {
            return jsonNode.longValue();
        }
        if (dataType == boolean.class || dataType == Boolean.class) {
            return jsonNode.booleanValue();
        }
        if (dataType.isEnum()) {
            return Enum.valueOf((Class<Enum>) dataType, jsonNode.textValue());
        }
        if (dataType == List.class) {
            return convertList(jsonNode);
        }
        return createAndPopulateInstance(jsonNode);
    }

    private List convertList(JsonNode jsonNode) {
        List<Object> list = new ArrayList<>();
        ArrayNode arrayNode = (ArrayNode) jsonNode;
        for (int idx = 0; idx < arrayNode.size(); idx++) {
            JsonNode node = arrayNode.get(idx);
            Object instance = createAndPopulateInstance(node);
            list.add(instance);
        }

        return list;
    }

}
