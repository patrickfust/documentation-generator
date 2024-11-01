package dk.fust.docgen

import dk.fust.docgen.model.Documentation
import spock.lang.Specification
import spock.lang.Unroll

class ModelValidatorSpec extends Specification {

    def "test validate empty documentation"() {
        when:
        Documentation documentation = new Documentation();
        ModelValidator modelValidator = new ModelValidator(documentation);
        modelValidator.validate()

        then:
        noExceptionThrown()
    }

    @Unroll
    def "test validations for #filename"() {
        given:
        Documentation documentation = TestHelper.loadTestDocumentation(filename)

        when:
        ModelValidator modelValidator = new ModelValidator(documentation)
        modelValidator.validate()

        then:
        IllegalArgumentException illegalArgumentException = thrown(IllegalArgumentException)
        illegalArgumentException.message == expectedValidationMessage

        where:
        filename                                     | expectedValidationMessage
        'foreignKey-missing-column.yaml'             | 'table_a.field_a has foreign key without column name'
        'foreignKey-table-not-exists.yaml'           | 'table_xxx.field_b does not exist. Is foreign key in table_a.field_a'
        'foreignKey-table-column-not-exists.yaml'    | 'table_b.field_x does not exist. Is foreign key in table_a.field_a'
        'foreignKey-wrong-datatype.yaml'             | 'table_b.field_b has different data types (UUID) compared to table_a.field_a (TEXT)'
        'foreignKey-wrong-datatype-generatedId.yaml' | 'table_b.table_b_id has different data types (INT) compared to table_a.field_a (BIGINT)'
    }

}
