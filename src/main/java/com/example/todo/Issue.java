package com.example.todo;

public class Issue {
    private final String filePath;
    private final int lineNumber;
    private final String content;

    public Issue(String filePath, int lineNumber, String content) {
        this.filePath = filePath;
        this.lineNumber = lineNumber;
        this.content = content;
    }

    public String getFilePath() { return filePath; }
    public int getLineNumber() { return lineNumber; }
    public String getContent() { return content; }
}