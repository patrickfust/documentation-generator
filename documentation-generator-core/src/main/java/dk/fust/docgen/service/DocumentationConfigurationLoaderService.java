package dk.fust.docgen.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads and parse a yaml- or json-file containing list of configurations
 */
@Slf4j
public class DocumentationConfigurationLoaderService {

    private static final String CLASS_NAME = "className";
    private final DocumentationService documentationService = new DocumentationService();

    private File configurationFile;

    /**
     * Reads generator configurations from a yaml- or json-file
     * @param configurationFile json or yaml file
     * @return parsed list of GeneratorConfiguration
     * @throws IOException an error occurred
     */
    public List<GeneratorConfiguration> readConfigurations(File configurationFile) throws IOException {
        this.configurationFile = configurationFile;
        List<GeneratorConfiguration> configurations = new ArrayList<>();
        JsonNode jsonNode = documentationService.loadFileAsTree(configurationFile);
        for (JsonNode node : jsonNode) {
            configurations.add((GeneratorConfiguration) createAndPopulateInstance(node, null));
        }
        return configurations;
    }

    private Object createAndPopulateInstance(JsonNode node, Class<?> dataType) {
        try {
            if (node.get(CLASS_NAME) == null && dataType == null) {
                if (node.canConvertToInt()) {
                    return node.intValue();
                }
                return node.textValue();
            }
            Class<?> clazz;
            if (node.get(CLASS_NAME) != null) {
                String className = node.get(CLASS_NAME).textValue();
                clazz = Class.forName(className);
            } else {
                clazz = dataType;
            }
            Object instance = clazz.getDeclaredConstructor().newInstance();
            boolean hasFields = false;
            for (Iterator<String> it = node.fieldNames(); it.hasNext(); ) {
                String fieldName = it.next();
                setValue(fieldName, node.get(fieldName), clazz, instance);
                hasFields = true;
            }
            if (!hasFields && node instanceof TextNode) {
                setValue("value", node, clazz, instance);
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
            if (clazz.getSuperclass() != null) {
                // Try it using the super class
                setValue(fieldName, node, clazz.getSuperclass(), instance);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object convertObject(JsonNode jsonNode, Class<?> dataType) {
        if (dataType == String.class) {
            return jsonNode.textValue();
        }
        if (dataType == File.class) {
            File file = new File(jsonNode.textValue());
            if (!file.exists()) {
                // Maybe we can write to it
                try {
                    Files.writeString(file.toPath(), "something");
                    log.debug("We could write to the file");
                    file.delete();
                } catch (IOException e) {
                    log.debug("Can't find the file or write to it: {}", file.getAbsolutePath());
                    // Maybe it's in the same folder as the configuration file
                    file = new File(configurationFile.getParentFile(), jsonNode.textValue());
                    log.debug("Trying {} instead", file.getAbsolutePath());
                }
            }
            return file;
        }
        if (dataType == int.class || dataType == Integer.class) {
            return jsonNode.intValue();
        }
        if (dataType == long.class || dataType == Long.class) {
            return jsonNode.longValue();
        }
        if (dataType == short.class || dataType == Short.class) {
            return jsonNode.shortValue();
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
        if (dataType == Map.class) {
            return convertMap(jsonNode);
        }
        return createAndPopulateInstance(jsonNode, dataType);
    }

    private Map convertMap(JsonNode jsonNode) {
        Map<Object, Object> map = new LinkedHashMap<>();
        Iterator<String> fieldNames = jsonNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode node =jsonNode.get(fieldName);
            String value = node.textValue();
            map.put(fieldName, value);
        }
        return map;
    }

    private List convertList(JsonNode jsonNode) {
        List<Object> list = new ArrayList<>();
        ArrayNode arrayNode = (ArrayNode) jsonNode;
        for (int idx = 0; idx < arrayNode.size(); idx++) {
            JsonNode node = arrayNode.get(idx);
            Object instance = createAndPopulateInstance(node, null);
            list.add(instance);
        }

        return list;
    }

}
