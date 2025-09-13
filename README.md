# kgk-project-structure-generator

## Description
The `Project Structure Generator` is a Java-based tool developed by **`Ganeshkumar Karuppaiah`** that simplifies the process of setting up project scaffolding. It dynamically creates project directory and file structures based on a predefined template or a file input. This tool is particularly useful for developers who want to maintain consistent project structures across multiple projects.

The tool reads a hierarchical structure from an input file (e.g., `repo.txt`) and generates the corresponding directories and files in the specified base path. It supports indentation-based hierarchy, making it easy to define complex project structures in a human-readable format.

## Features
- **Dynamic Structure Creation**: Automatically generates directories and files based on a hierarchical input.
- **File-Based Input**: Reads project structure from a file (e.g., `repo.txt`) with indentation-based hierarchy.
- **Customizable Base Path**: Allows users to specify the base directory where the structure will be created.
- **Error Handling**: Ensures proper creation of directories and files, with detailed error messages for failures.
- **Cross-Platform Compatibility**: Works seamlessly on Windows, macOS, and Linux systems.
- **Lightweight and Fast**: Minimal dependencies and quick execution.

## Repository
This project is hosted on GitHub under the repository: [kganeshkumark](https://github.com/kganeshkumark/project-structure-generator).

## Usage
1. Clone the repository:
   ```bash
   git clone https://github.com/KGANESHKUMARK/project-structure-generator.git

2. Navigate to the project directory:
   ``````bash 
    cd project-structure-generator
3. Run the program in the workspace
   ```bash
   java -cp target/project-structure-generator.jar src.CreateProjectStructureFromFile
4. Provide the base path and input file path when prompted:
   Base Path: The directory where the project structure should be created. Leave empty to use the current directory.
   Input File Path: The path to the file containing the project structure (e.g., repo.txt). Leave empty to use the default src/repo.txt.
5.
File Structure Example
The repo.txt file should contain the following structure
```
spring-boot-backend
├── src/main/java/com/example/springbootbackend
│   ├── controller
│   │   ├── HelloController.java
│   │   └── UserController.java
│   ├── entity
│   │   └── User.java
│   ├── repository
│   │   └── UserRepository.java
│   └── SpringBootBackendApplication.java
├── src/main/resources
│   └── application.properties
├── src/test/java/com/example/springbootbackend
│   ├── HelloControllerTest.java
│   └── UserControllerTest.java
└── pom.xml
```

6. Generate the above project structure was available in the same path.


# Code Highlights
## Key Classes
CreateProjectStructureFromFile:  
Reads the input file containing the project structure.
Parses the hierarchical structure and creates directories and files accordingly.
Handles errors such as missing files or invalid paths.


## Error Handling

The program checks if the input file exists and provides a clear error message if it does not.
It ensures that parent directories are created before creating files.
Any exceptions during file or directory creation are logged with detailed stack traces.

## Benefits
Saves time by automating the creation of project scaffolding.
Ensures consistency across multiple projects.
Reduces the risk of human error when setting up complex directory structures.

## License
This project is licensed under the MIT License. See the copyright notice in the source files for details.

## Author
Developed by Ganeshkumar Karuppaiah.
