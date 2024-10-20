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
        'foreignKey-missing-column.yaml'             | 'Field field_a has foreign key without column name'
        'foreignKey-table-not-exists.yaml'           | 'TableName table_xxx and columnName field_b does not exist'
        'foreignKey-table-column-not-exists.yaml'    | 'TableName table_b and columnName field_x does not exist'
        'foreignKey-wrong-datatype.yaml'             | 'TableName table_b and columnName field_b has different data types (TEXT and UUID)'
        'foreignKey-wrong-datatype-generatedId.yaml' | 'TableName table_b and columnName table_b_id has different data types (BIGINT and INT)'
        'index-with-non-existing-field.yaml'         | 'Index idx_that_is_pointless points to non-existing field does_not_exist'
    }

}
