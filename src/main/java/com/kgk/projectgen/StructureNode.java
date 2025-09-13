package com.kgk.projectgen;

/**
 * Represents a node in the project structure hierarchy.
 * Each node can be either a directory or a file.
 */
public class StructureNode {
    private final String name;
    private final int level;
    private final boolean isDirectory;
    
    public StructureNode(String name, int level, boolean isDirectory) {
        this.name = name;
        this.level = level;
        this.isDirectory = isDirectory;
    }
    
    public String getName() {
        return name;
    }
    
    public int getLevel() {
        return level;
    }
    
    public boolean isDirectory() {
        return isDirectory;
    }
    
    public boolean isFile() {
        return !isDirectory;
    }
    
    @Override
    public String toString() {
        return String.format("StructureNode{name='%s', level=%d, isDirectory=%s}", 
                           name, level, isDirectory);
    }
}