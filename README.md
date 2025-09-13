# KGK Project Structure Generator

![Java](https://img.shields.io/badge/Java-11%2B-orange) ![Maven](https://img.shields.io/badge/Maven-3.6%2B-blue) ![License](https://img.shields.io/badge/License-MIT-green)

The **KGK Project Structure Generator** is a Java-based command-line tool by Ganeshkumar Karuppaiah that dynamically creates project directories and files based on hierarchical input from text files. It simplifies project setup by ensuring consistent and efficient scaffolding with support for indentation-based hierarchy, customizable base paths, and comprehensive error handling.

## Features

- **Dynamic Creation**: Automatically generates directories and files from hierarchical input
- **Indentation-Based Hierarchy**: Supports 2-space or tab indentation to represent folder structure
- **Customizable Base Path**: Specify where the project structure should be created
- **Comprehensive Error Handling**: Detailed error messages for parsing and file system issues
- **Cross-Platform**: Works on Windows, macOS, and Linux
- **Lightweight**: No external dependencies beyond Java standard library
- **Extensible**: Clean architecture for future enhancements

## Requirements

- Java 11 or higher
- Maven 3.6+ (for building from source)

## Installation

### Option 1: Download Pre-built JAR
Download the latest release from the [Releases](../../releases) page.

### Option 2: Build from Source
```bash
git clone https://github.com/KGANESHKUMARK/kgk-project-structure-generator.git
cd kgk-project-structure-generator
mvn clean package
```

The JAR file will be created in the `target/` directory.

## Usage

### Basic Syntax
```bash
java -jar project-structure-generator-1.0.0.jar <input-file> [base-path]
```

### Arguments
- `input-file`: Path to the input file containing the project structure (e.g., `repo.txt`)
- `base-path`: Optional base directory where the structure should be created (defaults to current directory)

### Examples

#### Example 1: Generate in current directory
```bash
java -jar project-structure-generator-1.0.0.jar project-structure.txt
```

#### Example 2: Generate in specific directory
```bash
java -jar project-structure-generator-1.0.0.jar repo.txt /path/to/new/project
```

#### Example 3: Show help
```bash
java -jar project-structure-generator-1.0.0.jar --help
```

## Input File Format

The input file should contain the project structure using indentation to represent hierarchy:

- Use **2 spaces** or **tabs** for each indentation level
- **Directories**: End with `/` or have no file extension
- **Files**: Should have file extensions (e.g., `.java`, `.txt`, `.md`)
- **Empty lines**: Are ignored
- **Comments**: Not supported (will be treated as file/directory names)

### Example Input File (`repo.txt`)

```
src/
  main/
    java/
      com/
        example/
          Main.java
          service/
            UserService.java
            DataService.java
          model/
            User.java
            Product.java
    resources/
      application.properties
      static/
        css/
          style.css
        js/
          app.js
  test/
    java/
      com/
        example/
          MainTest.java
          service/
            UserServiceTest.java
docs/
  README.md
  API.md
pom.xml
.gitignore
LICENSE
```

### Generated Structure
Running the tool with the above input will create:
```
📁 src/
├── 📁 main/
│   ├── 📁 java/
│   │   └── 📁 com/
│   │       └── 📁 example/
│   │           ├── 📄 Main.java
│   │           ├── 📁 service/
│   │           │   ├── 📄 UserService.java
│   │           │   └── 📄 DataService.java
│   │           └── 📁 model/
│   │               ├── 📄 User.java
│   │               └── 📄 Product.java
│   └── 📁 resources/
│       ├── 📄 application.properties
│       └── 📁 static/
│           ├── 📁 css/
│           │   └── 📄 style.css
│           └── 📁 js/
│               └── 📄 app.js
└── 📁 test/
    └── 📁 java/
        └── 📁 com/
            └── 📁 example/
                ├── 📄 MainTest.java
                └── 📁 service/
                    └── 📄 UserServiceTest.java
📁 docs/
├── 📄 README.md
└── 📄 API.md
📄 pom.xml
📄 .gitignore
📄 LICENSE
```

## Error Handling

The tool provides comprehensive error handling for various scenarios:

### File System Errors
- **Input file not found**: Clear message indicating the missing file
- **Permission issues**: Detailed error when unable to create directories/files
- **Invalid paths**: Validation of file and directory names

### Structure Parsing Errors
- **Invalid indentation**: Detection of improper indentation jumps
- **Empty names**: Validation that file/directory names are not empty
- **Invalid characters**: Prevention of file system incompatible characters

### Example Error Messages
```bash
Error: Input file does not exist: missing-file.txt
Error: Invalid indentation jump from level 0 to level 3
Error: Node name contains invalid characters: invalid<file>.txt
Error: Invalid indentation level: -1
```

## Architecture

### Core Components

1. **ProjectStructureGenerator** (Interface)
   - Defines the contract for structure generation
   - Supports both file and line-based input

2. **DefaultProjectStructureGenerator** (Implementation)
   - Parses indentation-based hierarchy
   - Creates directories and files with error handling
   - Validates input and provides detailed error messages

3. **StructureNode** (Model)
   - Represents individual files/directories in the hierarchy
   - Encapsulates name, indentation level, and type information

4. **ProjectStructureException** (Exception)
   - Custom exception for structure-specific errors
   - Provides context-specific error messages

5. **ProjectStructureGeneratorMain** (CLI)
   - Command-line interface
   - Argument parsing and validation
   - User-friendly help and error messages

### Design Principles
- **Single Responsibility**: Each class has a focused purpose
- **Extensibility**: Interface-based design allows for future implementations
- **Error Safety**: Comprehensive error handling and validation
- **Testability**: Unit tests for core functionality

## Testing

Run the test suite:
```bash
mvn test
```

The project includes comprehensive unit tests covering:
- Basic structure generation
- Deep nesting scenarios
- Error conditions and edge cases
- File vs directory detection
- Indentation validation

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Setup
```bash
git clone https://github.com/KGANESHKUMARK/kgk-project-structure-generator.git
cd kgk-project-structure-generator
mvn clean compile
mvn test
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

**Ganeshkumar Karuppaiah** - [KGANESHKUMARK](https://github.com/KGANESHKUMARK)

## Changelog

### Version 1.0.0
- Initial release
- Indentation-based hierarchy parsing
- Directory and file creation
- Comprehensive error handling
- Command-line interface
- Unit tests and documentation

---

*Need help or have suggestions? Open an issue or start a discussion!*
