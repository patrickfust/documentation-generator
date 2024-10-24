package dk.fust.docgen.schemagenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.CustomPropertyDefinition;
import com.github.victools.jsonschema.generator.CustomPropertyDefinitionProvider;
import com.github.victools.jsonschema.generator.FieldScope;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerationContext;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaKeyword;
import com.github.victools.jsonschema.generator.SchemaVersion;
import dk.fust.docgen.model.Description;
import dk.fust.docgen.model.Documentation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

/**
 * Generates documentation-schema.json based upon the model i api
 */
public class GenerateSchema {

    /**
     * Generates documentation-schema.json based upon the model i api
     * @param args not used
     * @throws IOException an error occurred
     */
    public static void main(String[] args) throws IOException {
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
        enumInLowerCase(configBuilder);
        annotatedWithDescription(configBuilder);

        SchemaGeneratorConfig config = configBuilder.build();
        SchemaGenerator generator = new SchemaGenerator(config);
        JsonNode jsonSchema = generator.generateSchema(Documentation.class);

        writeToSchemaFile(jsonSchema);
    }

    private static void annotatedWithDescription(SchemaGeneratorConfigBuilder configBuilder) {
        configBuilder.forFields().withDescriptionResolver(fieldScope -> {
            Description annotation = fieldScope.getRawMember().getAnnotation(Description.class);
            if (annotation != null) {
                return annotation.value();
            }
            return null;
        }).withDefaultResolver(fieldScope -> {
            Description annotation = fieldScope.getRawMember().getAnnotation(Description.class);
            if (annotation != null) {
                if (annotation.hasDefaultBoolean()) {
                    return annotation.defaultBoolean();
                }
            }
            return null;
        }).withRequiredCheck(check -> {
            Description annotation = check.getRawMember().getAnnotation(Description.class);
            if (annotation != null) {
                return annotation.required();
            }
            return false;
        });
    }

    private static void writeToSchemaFile(JsonNode jsonSchema) throws IOException {
        File schemaFile = new File("documentation-generator-api/src/main/resources", "documentation-schema.json");
        try (FileOutputStream fos = new FileOutputStream(schemaFile)) {
            fos.write(jsonSchema.toPrettyString().getBytes(StandardCharsets.UTF_8));
        }
    }

    private static void enumInLowerCase(SchemaGeneratorConfigBuilder configBuilder) {
        configBuilder.forFields().withCustomDefinitionProvider(new CustomPropertyDefinitionProvider<FieldScope>() {
            @Override
            public CustomPropertyDefinition provideCustomSchemaDefinition(FieldScope fieldScope, SchemaGenerationContext schemaGenerationContext) {
                if (fieldScope.getType().isInstanceOf(Enum.class)) {
                    SchemaGeneratorConfig config = schemaGenerationContext.getGeneratorConfig();
                    ObjectNode node = config.createObjectNode();
                    ArrayNode array = node.arrayNode();
                    Stream.of(fieldScope.getType().getErasedType().getEnumConstants()).forEach(o -> array.add(o.toString().toLowerCase()));
                    node.set(schemaGenerationContext.getKeyword(SchemaKeyword.TAG_ENUM), array);
                    return new CustomPropertyDefinition(node);
                }
                return null;
            }
        });
    }
}
