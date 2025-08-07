# Maven plugin

You can see a demo of how to use the documentation generator's Maven plugin at [demo-erdiagram](../demos/demo-erdiagram)

## Configuration

In the `build`-section of your pom, you can add the `generateDocumentation` goal.

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>dk.fust.docgen</groupId>
        <artifactId>documentation-generator-maven-plugin</artifactId>
        <version>1.13.0-SNAPSHOT</version>
        <configuration>
          <documentationConfigurationFile>generator-configuration.yml</documentationConfigurationFile>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>generateDocumentation</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```

## Goals

There're two goals: `help` and `generatorDocumentation`.

### Help

Show help:

`mvn dk.fust.docgen:documentation-generator-maven-plugin:help`

### Generate documentation

Generate the documentation:

`mvn dk.fust.docgen:documentation-generator-maven-plugin:generateDocumentation`

