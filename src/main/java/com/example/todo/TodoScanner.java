package com.example.todo;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TodoScanner {
    private final Config config;

    public TodoScanner(Config config) {
        this.config = config;
    }

    public List<Issue> scan() throws IOException {
        List<Issue> issues = new ArrayList<>();
        Path startPath = Paths.get(config.getSourceDirectory());

        if (!Files.exists(startPath)) {
            throw new IllegalArgumentException("Directory not found: " + startPath);
        }

        try (Stream<Path> stream = Files.walk(startPath)) {
            stream.filter(p -> !Files.isDirectory(p))
                    .filter(this::isValidExtension)
                    .forEach(path -> scanFile(path, issues));
        }
        return issues;
    }

    private boolean isValidExtension(Path path) {
        String fileName = path.toString().toLowerCase();
        return config.getFileExtensions().stream()
                .anyMatch(ext -> fileName.endsWith(ext.toLowerCase()));
    }

    private void scanFile(Path path, List<Issue> issues) {
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                for (String keyword : config.getKeywords()) {
                    if (line.toUpperCase().contains(keyword.toUpperCase())) {
                        issues.add(new Issue(path.toString(), i + 1, line.trim()));
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + path);
        }
    }
}