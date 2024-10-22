package dk.fust.docgen.destination

class MockDestination implements Destination {

    String document
    String destination

    @Override
    void validate() {

    }

    @Override
    void sendDocumentToDestination(String document, String destination) throws IOException {
        this.document = document
        this.destination = destination
    }

}
