package com.kgk.projectgen;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.io.IOException;

/**
 * Integration tests for the complete ProjectStructureGenerator application.
 * Tests the end-to-end functionality without calling main() directly.
 */
public class ProjectStructureGeneratorMainTest {
    
    private Path tempDir;
    private Path testInputFile;
    
    @Before
    public void setUp() throws IOException {
        tempDir = Files.createTempDirectory("kgk-integration-test");
        testInputFile = tempDir.resolve("test-input.txt");
        
        // Create a test input file
        String testContent = 
            "webapp/\n" +
            "  src/\n" +
            "    main/\n" +
            "      java/\n" +
            "        Application.java\n" +
            "      resources/\n" +
            "        application.yml\n" +
            "    test/\n" +
            "      java/\n" +
            "        ApplicationTest.java\n" +
            "  README.md\n" +
            "  pom.xml\n";
        
        Files.write(testInputFile, testContent.getBytes());
    }
    
    @After
    public void tearDown() throws IOException {
        // Clean up temp directory
        if (Files.exists(tempDir)) {
            Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        // Ignore cleanup errors
                    }
                });
        }
    }
    
    @Test
    public void testEndToEndFileGeneration() throws IOException, ProjectStructureException {
        Path outputDir = tempDir.resolve("output");
        
        ProjectStructureGenerator generator = new DefaultProjectStructureGenerator();
        generator.generateFromFile(testInputFile, outputDir);
        
        // Verify the structure was created correctly
        assertTrue("webapp directory should exist", Files.exists(outputDir.resolve("webapp")));
        assertTrue("src directory should exist", Files.exists(outputDir.resolve("webapp/src")));
        assertTrue("main directory should exist", Files.exists(outputDir.resolve("webapp/src/main")));
        assertTrue("java directory should exist", Files.exists(outputDir.resolve("webapp/src/main/java")));
        assertTrue("Application.java should exist", Files.exists(outputDir.resolve("webapp/src/main/java/Application.java")));
        assertTrue("application.yml should exist", Files.exists(outputDir.resolve("webapp/src/main/resources/application.yml")));
        assertTrue("ApplicationTest.java should exist", Files.exists(outputDir.resolve("webapp/src/test/java/ApplicationTest.java")));
        assertTrue("README.md should exist", Files.exists(outputDir.resolve("webapp/README.md")));
        assertTrue("pom.xml should exist", Files.exists(outputDir.resolve("webapp/pom.xml")));
        
        // Verify types (directories vs files)
        assertTrue("webapp should be a directory", Files.isDirectory(outputDir.resolve("webapp")));
        assertTrue("Application.java should be a file", Files.isRegularFile(outputDir.resolve("webapp/src/main/java/Application.java")));
        assertTrue("README.md should be a file", Files.isRegularFile(outputDir.resolve("webapp/README.md")));
    }
    
    @Test
    public void testFileCreationFromComplexStructure() throws IOException, ProjectStructureException {
        // Create a more complex input structure
        String complexContent = 
            "project/\n" +
            "  frontend/\n" +
            "    src/\n" +
            "      components/\n" +
            "        Header.js\n" +
            "        Footer.js\n" +
            "      styles/\n" +
            "        main.css\n" +
            "    package.json\n" +
            "  backend/\n" +
            "    src/\n" +
            "      controllers/\n" +
            "        UserController.java\n" +
            "      services/\n" +
            "        UserService.java\n" +
            "    pom.xml\n" +
            "  docs/\n" +
            "    architecture.md\n" +
            "    deployment.md\n" +
            "  .gitignore\n" +
            "  README.md\n";
        
        Path complexInputFile = tempDir.resolve("complex-structure.txt");
        Files.write(complexInputFile, complexContent.getBytes());
        
        Path outputDir = tempDir.resolve("complex-output");
        
        ProjectStructureGenerator generator = new DefaultProjectStructureGenerator();
        generator.generateFromFile(complexInputFile, outputDir);
        
        // Verify complex structure
        assertTrue("Frontend Header.js should exist", 
                  Files.exists(outputDir.resolve("project/frontend/src/components/Header.js")));
        assertTrue("Backend UserController.java should exist", 
                  Files.exists(outputDir.resolve("project/backend/src/controllers/UserController.java")));
        assertTrue("Documentation should exist", 
                  Files.exists(outputDir.resolve("project/docs/architecture.md")));
        assertTrue(".gitignore should exist", 
                  Files.exists(outputDir.resolve("project/.gitignore")));
        
        // Verify mixed file types
        assertTrue("package.json should be a file", 
                  Files.isRegularFile(outputDir.resolve("project/frontend/package.json")));
        assertTrue("controllers should be a directory", 
                  Files.isDirectory(outputDir.resolve("project/backend/src/controllers")));
    }
}