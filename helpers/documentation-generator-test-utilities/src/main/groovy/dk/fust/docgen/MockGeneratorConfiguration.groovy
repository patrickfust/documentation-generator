package dk.fust.docgen;

import dk.fust.docgen.destination.Destination;

import java.io.File;

class MockGeneratorConfiguration implements GeneratorConfiguration {

    File documentationFile
    Destination destination
    int anInt
    Integer aBigInteger
    boolean aBoolean
    Boolean aBigBoolean
    long aLong
    Long aBigLong
    String aString
    MockEnum mockEnum

    @Override
    void validate() {
    }

    @Override
    Generator getGenerator() {
        return null;
    }

}
