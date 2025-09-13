package com.kgk.projectgen;

/**
 * Custom exception for project structure generation errors.
 */
public class ProjectStructureException extends Exception {
    
    public ProjectStructureException(String message) {
        super(message);
    }
    
    public ProjectStructureException(String message, Throwable cause) {
        super(message, cause);
    }
}