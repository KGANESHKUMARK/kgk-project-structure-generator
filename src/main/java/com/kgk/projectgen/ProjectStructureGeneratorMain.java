package com.kgk.projectgen;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main class for the KGK Project Structure Generator CLI application.
 * Provides command-line interface for generating project structures from input files.
 */
public class ProjectStructureGeneratorMain {
    
    private static final String USAGE = 
        "KGK Project Structure Generator v1.0.0\n" +
        "Usage: java -jar project-structure-generator.jar <input-file> [base-path]\n" +
        "\n" +
        "Arguments:\n" +
        "  input-file  Path to the input file containing the project structure (e.g., repo.txt)\n" +
        "  base-path   Optional base directory where the structure should be created\n" +
        "              Defaults to current directory if not specified\n" +
        "\n" +
        "Example:\n" +
        "  java -jar project-structure-generator.jar repo.txt /path/to/project\n" +
        "  java -jar project-structure-generator.jar structure.txt\n" +
        "\n" +
        "Input file format:\n" +
        "  Use indentation (2 spaces or tabs) to represent hierarchy\n" +
        "  Directories can end with '/' or have no extension\n" +
        "  Files should have extensions\n" +
        "\n" +
        "Example input file:\n" +
        "  src/\n" +
        "    main/\n" +
        "      java/\n" +
        "        Main.java\n" +
        "      resources/\n" +
        "        config.properties\n" +
        "    test/\n" +
        "      java/\n" +
        "        MainTest.java\n" +
        "  README.md\n" +
        "  pom.xml";
    
    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
            System.out.println(USAGE);
            System.exit(0);
        }
        
        if (args.length < 1 || args.length > 2) {
            System.err.println("Error: Invalid number of arguments");
            System.err.println();
            System.err.println(USAGE);
            System.exit(1);
        }
        
        String inputFile = args[0];
        String basePath = args.length > 1 ? args[1] : System.getProperty("user.dir");
        
        try {
            generateProjectStructure(inputFile, basePath);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            if (Boolean.parseBoolean(System.getProperty("debug", "false"))) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }
    
    /**
     * Generates the project structure using the provided input file and base path.
     */
    private static void generateProjectStructure(String inputFile, String basePath) 
            throws IOException, ProjectStructureException {
        
        System.out.println("KGK Project Structure Generator");
        System.out.println("================================");
        System.out.println("Input file: " + inputFile);
        System.out.println("Base path: " + basePath);
        System.out.println();
        
        Path inputPath = Paths.get(inputFile);
        Path basePathObj = Paths.get(basePath);
        
        ProjectStructureGenerator generator = new DefaultProjectStructureGenerator();
        
        System.out.println("Generating project structure...");
        long startTime = System.currentTimeMillis();
        
        generator.generateFromFile(inputPath, basePathObj);
        
        long endTime = System.currentTimeMillis();
        System.out.println("Project structure generated successfully!");
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
        System.out.println("Structure created in: " + basePathObj.toAbsolutePath());
    }
}