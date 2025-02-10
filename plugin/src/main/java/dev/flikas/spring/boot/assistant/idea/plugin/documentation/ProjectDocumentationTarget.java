package dev.flikas.spring.boot.assistant.idea.plugin.documentation;

import com.intellij.openapi.project.Project;
import com.intellij.platform.backend.documentation.DocumentationTarget;

public interface ProjectDocumentationTarget extends DocumentationTarget {
  Project getProject();
}
