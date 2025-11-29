package com.example.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoScannerTest {

    @TempDir
    Path tempDir;

    @Test
    void testScanFindsTodos() throws IOException {

        File configFile = tempDir.resolve("test.properties").toFile();
        String configContent = "scan.source.directory=" + tempDir.toString().replace("\\", "/") + "\n" +
                "scan.file.extensions=.java,.txt\n" +
                "scan.keywords=TODO,FIXME";
        Files.write(configFile.toPath(), configContent.getBytes());

        Config config = new Config();
        config.load(configFile.getAbsolutePath());

        Path javaFile = tempDir.resolve("Test.java");
        String javaCode = "public class Test {\n" +
                "  // TODO: Fix this\n" +
                "  int x = 0;\n" +
                "}";
        Files.write(javaFile, javaCode.getBytes());

        Path txtFile = tempDir.resolve("Note.txt");
        String txtContent = "Just a note.\nFIXME: urgent!";
        Files.write(txtFile, txtContent.getBytes());

        Path mdFile = tempDir.resolve("Read.md");
        Files.write(mdFile, "TODO: update readme".getBytes());

        TodoScanner scanner = new TodoScanner(config);
        List<Issue> issues = scanner.scan();

        assertEquals(2, issues.size(), "Should find 2 issues (.java and .txt)");

        boolean hasTodo = issues.stream().anyMatch(i -> i.getContent().contains("TODO: Fix this"));
        boolean hasFixme = issues.stream().anyMatch(i -> i.getContent().contains("FIXME: urgent"));

        assertTrue(hasTodo);
        assertTrue(hasFixme);
    }
}