package com.example.todo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            Config config = new Config();
            config.load("app.properties");

            TodoScanner scanner = new TodoScanner(config);
            List<Issue> issues = scanner.scan();

            printReport(issues);

        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
        }
    }

    private static void printReport(List<Issue> issues) {
        System.out.println("Found " + issues.size() + " issues:\n");

        Map<String, List<Issue>> grouped = issues.stream()
                .collect(Collectors.groupingBy(Issue::getFilePath));

        grouped.forEach((file, fileIssues) -> {
            System.out.println("[" + file + "]");
            fileIssues.forEach(issue ->
                    System.out.println("  L" + issue.getLineNumber() + ": " + issue.getContent())
            );
            System.out.println();
        });
    }
}