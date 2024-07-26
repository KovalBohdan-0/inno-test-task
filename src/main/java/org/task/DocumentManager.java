package org.task;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class DocumentManager {

    private final Map<String, Document> documentStore = new HashMap<>();

    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document.getId() == null || document.getId().isEmpty()) {
            document.setId(UUID.randomUUID().toString());
        }
        documentStore.put(document.getId(), document);
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        return documentStore.values().stream()
                .filter(document -> matches(document, request))
                .collect(Collectors.toList());
    }

    private boolean matches(Document document, SearchRequest request) {
        if (request.getTitlePrefixes() != null && !request.getTitlePrefixes().isEmpty()) {
            boolean prefixMatch = request.getTitlePrefixes().stream()
                    .anyMatch(prefix -> document.getTitle().startsWith(prefix));
            if (!prefixMatch) return false;
        }

        if (request.getContainsContents() != null && !request.getContainsContents().isEmpty()) {
            boolean contentMatch = request.getContainsContents().stream()
                    .anyMatch(content -> document.getContent().contains(content));
            if (!contentMatch) return false;
        }

        if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
            if (!request.getAuthorIds().contains(document.getAuthor().getId())) {
                return false;
            }
        }

        if (request.getCreatedFrom() != null) {
            if (document.getCreated().isBefore(request.getCreatedFrom())) {
                return false;
            }
        }

        if (request.getCreatedTo() != null) {
            return !document.getCreated().isAfter(request.getCreatedTo());
        }

        return true;
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return Optional.ofNullable(documentStore.get(id));
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}
