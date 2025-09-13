/*
 * Copyright (c) 2025 KGK GANESHKUMAR KARUPPAIAH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Stack;

public class CreateProjectStructureFromFile {

    public static void main(String[] args) {
        // Get the base path from the user
        Scanner scanner = new Scanner(System.in);
        System.out.println("KGK: Enter the base path where the project structure should be created (leave empty for current directory):");
        String basePathInput = scanner.nextLine();
        Path basePath = basePathInput.isBlank() ? Paths.get("").toAbsolutePath() : Paths.get(basePathInput);

        // Get the file path containing the project structure
        System.out.println("KGK: Enter the file path containing the project structure (leave empty for 'repo.txt' in current directory):");
        String filePathInput = scanner.nextLine();
        Path filePath = filePathInput.isBlank() ? Paths.get("src/repo.txt").toAbsolutePath() : Paths.get(filePathInput);

        // Check if the file exists
        if (!Files.exists(filePath)) {
            System.err.println("KGK: Error: File not found at " + filePath);
            return;
        }

        try {
            // Read the project structure from the file
            String inputStructure = Files.readString(filePath);

            // Create the project structure
            createStructureFromInput(basePath, inputStructure);
        } catch (IOException e) {
            System.err.println("KGK: Failed to read the file: " + filePath);
            e.printStackTrace();
        }
    }

    public static void createStructureFromInput(Path basePath, String inputStructure) {
        String[] lines = inputStructure.split("\n");
        Stack<Path> pathStack = new Stack<>();

        for (String line : lines) {
            // Determine the current level of indentation
            int currentIndent = line.lastIndexOf("│") + 1;
            currentIndent = Math.max(currentIndent, line.lastIndexOf("├") + 1);
            currentIndent = Math.max(currentIndent, line.lastIndexOf("└") + 1);

            // Extract the name of the directory or file
            String name = line.replaceAll("[│├└──]+", "").trim();

            // Adjust the stack based on the current indentation
            while (pathStack.size() > currentIndent) {
                pathStack.pop();
            }

            // Determine the current path
            Path currentPath = pathStack.isEmpty() ? basePath.resolve(name) : pathStack.peek().resolve(name);

            try {
                if (name.contains(".")) {
                    // Create a file
                    Files.createDirectories(currentPath.getParent()); // Ensure parent directories exist
                    Files.createFile(currentPath);
                    System.out.println("KGK: File created: " + currentPath);
                } else {
                    // Create a directory
                    Files.createDirectories(currentPath);
                    System.out.println("KGK: Directory created: " + currentPath);
                    pathStack.push(currentPath); // Push only directories onto the stack
                }
            } catch (IOException e) {
                System.err.println("KGK: Failed to create: " + currentPath);
                e.printStackTrace();
            }
        }
    }
}