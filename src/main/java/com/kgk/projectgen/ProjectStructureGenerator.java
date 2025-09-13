package com.kgk.projectgen;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Core interface for the Project Structure Generator.
 * Defines the contract for reading hierarchical project structures
 * and creating corresponding directories and files.
 */
public interface ProjectStructureGenerator {
    
    /**
     * Generates project structure from an input file.
     * 
     * @param inputFilePath Path to the input file containing the project structure
     * @param basePath Base directory where the project structure should be created
     * @throws IOException if there are issues reading the input file or creating directories/files
     * @throws ProjectStructureException if there are issues parsing the project structure
     */
    void generateFromFile(Path inputFilePath, Path basePath) throws IOException, ProjectStructureException;
    
    /**
     * Generates project structure from a list of structure lines.
     * 
     * @param structureLines List of strings representing the hierarchical structure
     * @param basePath Base directory where the project structure should be created
     * @throws IOException if there are issues creating directories/files
     * @throws ProjectStructureException if there are issues parsing the project structure
     */
    void generateFromLines(List<String> structureLines, Path basePath) throws IOException, ProjectStructureException;
}