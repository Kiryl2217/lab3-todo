package com.example.todo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Config {
    private String sourceDirectory;
    private List<String> fileExtensions;
    private List<String> keywords;

    public void load(String configPath) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(configPath)) {
            props.load(fis);
        }

        this.sourceDirectory = props.getProperty("scan.source.directory", "./");

        String exts = props.getProperty("scan.file.extensions", ".java");
        this.fileExtensions = Arrays.stream(exts.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        String keys = props.getProperty("scan.keywords", "TODO");
        this.keywords = Arrays.stream(keys.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public String getSourceDirectory() { return sourceDirectory; }
    public List<String> getFileExtensions() { return fileExtensions; }
    public List<String> getKeywords() { return keywords; }
}