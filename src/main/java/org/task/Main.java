package org.task;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        DocumentManager documentManager = new DocumentManager();

        // Create an author
        DocumentManager.Author author = DocumentManager.Author.builder()
                .id("1")
                .name("Bohdan Koval")
                .build();

        // Create and save a document
        DocumentManager.Document document1 = DocumentManager.Document.builder()
                .title("Java Programming")
                .content("Document content")
                .author(author)
                .created(Instant.now())
                .build();

        DocumentManager.Document savedDocument1 = documentManager.save(document1);
        System.out.println("Saved Document 1: " + savedDocument1);

        // Create and save another document
        DocumentManager.Document document2 = DocumentManager.Document.builder()
                .title("Document 2")
                .content("Document content 2")
                .author(author)
                .created(Instant.now())
                .build();

        DocumentManager.Document savedDocument2 = documentManager.save(document2);
        System.out.println("Saved Document 2: " + savedDocument2);

        // Search for documents with title prefix "Java"
        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .titlePrefixes(Collections.singletonList("Java"))
                .build();

        List<DocumentManager.Document> searchResults = documentManager.search(searchRequest);
        System.out.println("Search Results: " + searchResults);

        // Find a document by ID
        Optional<DocumentManager.Document> foundDocument = documentManager.findById(savedDocument1.getId());
        foundDocument.ifPresent(document -> System.out.println("Found: " + document));
    }
}