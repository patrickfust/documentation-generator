# Demo of the document generator

## Data lineage

This will we overriden when generating documentation using the gradle task.

```shell
./gradlew generateDocumentation
```

### Data lineage that will be rewritten
[//]: #my-data-lineage_START ()
| Target |  |  | Source |  |  |  | Transformation |  |
|--|--|--|--|--|--|--|--|--|
| Table Name | Column Name | Data Type | Database Name | Table Name | Column Name | Data Type |  |
| table_a | field_a | int |  |  |  |  |  |  |
| table_b | field_b | int | This DB | table_a | field_a | int | year / area * people flirting |  |
| table_in_group | field_b | int |  |  |  |  |  |  |
| table_in_group | point_to_another_file | int | External DB name | external_table_a | ext_a | int |  |  |
[//]: #my-data-lineage_END ()
