package com.kgk.projectgen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Default implementation of ProjectStructureGenerator.
 * Supports indentation-based hierarchy parsing and creates directories and files accordingly.
 */
public class DefaultProjectStructureGenerator implements ProjectStructureGenerator {
    
    private static final String DEFAULT_INDENTATION = "  "; // 2 spaces
    
    @Override
    public void generateFromFile(Path inputFilePath, Path basePath) throws IOException, ProjectStructureException {
        if (!Files.exists(inputFilePath)) {
            throw new ProjectStructureException("Input file does not exist: " + inputFilePath);
        }
        
        if (!Files.isRegularFile(inputFilePath)) {
            throw new ProjectStructureException("Input path is not a regular file: " + inputFilePath);
        }
        
        List<String> lines = Files.readAllLines(inputFilePath);
        generateFromLines(lines, basePath);
    }
    
    @Override
    public void generateFromLines(List<String> structureLines, Path basePath) throws IOException, ProjectStructureException {
        if (structureLines == null || structureLines.isEmpty()) {
            throw new ProjectStructureException("Structure lines cannot be null or empty");
        }
        
        if (basePath == null) {
            throw new ProjectStructureException("Base path cannot be null");
        }
        
        // Ensure base directory exists
        Files.createDirectories(basePath);
        
        List<StructureNode> nodes = parseStructureLines(structureLines);
        createStructure(nodes, basePath);
    }
    
    /**
     * Parses structure lines into StructureNode objects.
     * Determines indentation level and whether each entry is a file or directory.
     */
    private List<StructureNode> parseStructureLines(List<String> lines) throws ProjectStructureException {
        List<StructureNode> nodes = new ArrayList<>();
        
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            
            // Skip empty lines
            if (line.trim().isEmpty()) {
                continue;
            }
            
            try {
                StructureNode node = parseLine(line);
                nodes.add(node);
            } catch (Exception e) {
                throw new ProjectStructureException(
                    String.format("Error parsing line %d: '%s' - %s", i + 1, line, e.getMessage()), e);
            }
        }
        
        return nodes;
    }
    
    /**
     * Parses a single line to create a StructureNode.
     */
    private StructureNode parseLine(String line) throws ProjectStructureException {
        if (line == null) {
            throw new ProjectStructureException("Line cannot be null");
        }
        
        // Calculate indentation level
        int level = calculateIndentationLevel(line);
        String name = line.trim();
        
        if (name.isEmpty()) {
            throw new ProjectStructureException("Node name cannot be empty");
        }
        
        // Determine if it's a directory or file
        // Convention: directories end with '/' or don't have an extension
        boolean isDirectory = name.endsWith("/") || !name.contains(".");
        
        // Remove trailing slash if present
        if (name.endsWith("/")) {
            name = name.substring(0, name.length() - 1);
        }
        
        // Validate name
        if (name.isEmpty()) {
            throw new ProjectStructureException("Node name cannot be empty after processing");
        }
        
        if (containsInvalidCharacters(name)) {
            throw new ProjectStructureException("Node name contains invalid characters: " + name);
        }
        
        return new StructureNode(name, level, isDirectory);
    }
    
    /**
     * Calculates the indentation level based on leading whitespace.
     */
    private int calculateIndentationLevel(String line) {
        int spaces = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                spaces++;
            } else if (c == '\t') {
                spaces += 4; // Treat tab as 4 spaces
            } else {
                break;
            }
        }
        return spaces / 2; // Assuming 2-space indentation
    }
    
    /**
     * Checks if the name contains invalid file system characters.
     */
    private boolean containsInvalidCharacters(String name) {
        // Basic check for common invalid characters in file/directory names
        String invalidChars = "<>:\"|?*";
        for (char c : invalidChars.toCharArray()) {
            if (name.indexOf(c) >= 0) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Creates the actual directory and file structure.
     */
    private void createStructure(List<StructureNode> nodes, Path basePath) throws IOException, ProjectStructureException {
        // Build the complete paths for each node
        Stack<String> pathStack = new Stack<>();
        
        for (StructureNode node : nodes) {
            try {
                // Adjust path stack based on current level
                adjustPathStack(pathStack, node.getLevel());
                
                // Add current node to path stack
                pathStack.push(node.getName());
                
                // Build the complete path
                Path completePath = basePath;
                for (String pathComponent : pathStack) {
                    completePath = completePath.resolve(pathComponent);
                }
                
                if (node.isDirectory()) {
                    Files.createDirectories(completePath);
                } else {
                    // Ensure parent directory exists
                    Files.createDirectories(completePath.getParent());
                    // Create file if it doesn't exist
                    if (!Files.exists(completePath)) {
                        Files.createFile(completePath);
                    }
                    // Remove file from path stack as it can't contain children
                    pathStack.pop();
                }
                
            } catch (IOException e) {
                throw new IOException("Failed to create " + (node.isDirectory() ? "directory" : "file") + 
                                    " '" + node.getName() + "': " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Adjusts the path stack to match the current indentation level.
     */
    private void adjustPathStack(Stack<String> pathStack, int currentLevel) throws ProjectStructureException {
        if (currentLevel < 0) {
            throw new ProjectStructureException("Invalid indentation level: " + currentLevel);
        }
        
        if (currentLevel > pathStack.size()) {
            throw new ProjectStructureException(
                String.format("Invalid indentation jump from level %d to %d", pathStack.size(), currentLevel));
        }
        
        // Pop path components until we're at the right level
        while (pathStack.size() > currentLevel) {
            pathStack.pop();
        }
    }
}