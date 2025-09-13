package com.kgk.projectgen;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;

/**
 * Unit tests for DefaultProjectStructureGenerator.
 */
public class DefaultProjectStructureGeneratorTest {
    
    private DefaultProjectStructureGenerator generator;
    private Path tempDir;
    
    @Before
    public void setUp() throws IOException {
        generator = new DefaultProjectStructureGenerator();
        tempDir = Files.createTempDirectory("kgk-test");
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
    public void testGenerateSimpleStructure() throws IOException, ProjectStructureException {
        List<String> structure = Arrays.asList(
            "src/",
            "  main/",
            "    Main.java",
            "  test/",
            "    MainTest.java",
            "README.md"
        );
        
        generator.generateFromLines(structure, tempDir);
        
        // Verify directories
        assertTrue("src directory should exist", Files.exists(tempDir.resolve("src")));
        assertTrue("src/main directory should exist", Files.exists(tempDir.resolve("src/main")));
        assertTrue("src/test directory should exist", Files.exists(tempDir.resolve("src/test")));
        
        // Verify files
        assertTrue("Main.java should exist", Files.exists(tempDir.resolve("src/main/Main.java")));
        assertTrue("MainTest.java should exist", Files.exists(tempDir.resolve("src/test/MainTest.java")));
        assertTrue("README.md should exist", Files.exists(tempDir.resolve("README.md")));
        
        // Verify they are the correct type
        assertTrue("src should be a directory", Files.isDirectory(tempDir.resolve("src")));
        assertTrue("Main.java should be a file", Files.isRegularFile(tempDir.resolve("src/main/Main.java")));
    }
    
    @Test
    public void testGenerateWithDeepNesting() throws IOException, ProjectStructureException {
        List<String> structure = Arrays.asList(
            "level1/",
            "  level2/",
            "    level3/",
            "      level4/",
            "        deep.txt",
            "  sibling.txt"
        );
        
        generator.generateFromLines(structure, tempDir);
        
        Path deepFile = tempDir.resolve("level1/level2/level3/level4/deep.txt");
        Path siblingFile = tempDir.resolve("level1/sibling.txt");
        
        assertTrue("Deep nested file should exist", Files.exists(deepFile));
        assertTrue("Sibling file should exist", Files.exists(siblingFile));
        assertTrue("Deep file should be a regular file", Files.isRegularFile(deepFile));
    }
    
    @Test
    public void testEmptyLines() throws IOException, ProjectStructureException {
        List<String> structure = Arrays.asList(
            "src/",
            "",
            "  main/",
            "",
            "    Main.java",
            "",
            "README.md"
        );
        
        generator.generateFromLines(structure, tempDir);
        
        assertTrue("src directory should exist", Files.exists(tempDir.resolve("src")));
        assertTrue("Main.java should exist", Files.exists(tempDir.resolve("src/main/Main.java")));
        assertTrue("README.md should exist", Files.exists(tempDir.resolve("README.md")));
    }
    
    @Test(expected = ProjectStructureException.class)
    public void testNullStructureLines() throws IOException, ProjectStructureException {
        generator.generateFromLines(null, tempDir);
    }
    
    @Test(expected = ProjectStructureException.class)
    public void testEmptyStructureLines() throws IOException, ProjectStructureException {
        generator.generateFromLines(Arrays.asList(), tempDir);
    }
    
    @Test(expected = ProjectStructureException.class)
    public void testNullBasePath() throws IOException, ProjectStructureException {
        List<String> structure = Arrays.asList("test.txt");
        generator.generateFromLines(structure, null);
    }
    
    @Test(expected = ProjectStructureException.class)
    public void testInvalidIndentationJump() throws IOException, ProjectStructureException {
        List<String> structure = Arrays.asList(
            "level1/",
            "      level4.txt"  // Jump from level 0 to level 3
        );
        
        generator.generateFromLines(structure, tempDir);
    }
    
    @Test(expected = ProjectStructureException.class)
    public void testInvalidFileName() throws IOException, ProjectStructureException {
        List<String> structure = Arrays.asList(
            "invalid<file>.txt"
        );
        
        generator.generateFromLines(structure, tempDir);
    }
    
    @Test
    public void testFileWithoutExtension() throws IOException, ProjectStructureException {
        List<String> structure = Arrays.asList(
            "Makefile",
            "Dockerfile",
            "src/",
            "  lib"
        );
        
        generator.generateFromLines(structure, tempDir);
        
        // Files without extensions should be treated as directories
        assertTrue("Makefile should be created as directory", Files.isDirectory(tempDir.resolve("Makefile")));
        assertTrue("Dockerfile should be created as directory", Files.isDirectory(tempDir.resolve("Dockerfile")));
        assertTrue("lib should be created as directory", Files.isDirectory(tempDir.resolve("src/lib")));
    }
    
    @Test
    public void testMixedIndentation() throws IOException, ProjectStructureException {
        List<String> structure = Arrays.asList(
            "root/",
            "  child1/",
            "    grandchild1.txt",
            "  child2/",
            "    grandchild2.txt",
            "sibling.txt"
        );
        
        generator.generateFromLines(structure, tempDir);
        
        assertTrue("grandchild1.txt should exist", Files.exists(tempDir.resolve("root/child1/grandchild1.txt")));
        assertTrue("grandchild2.txt should exist", Files.exists(tempDir.resolve("root/child2/grandchild2.txt")));
        assertTrue("sibling.txt should exist", Files.exists(tempDir.resolve("sibling.txt")));
    }
}