package dev.flikas.spring.boot.assistant.idea.plugin.documentation;

import com.intellij.model.Pointer;
import com.intellij.openapi.project.Project;
import com.intellij.platform.backend.documentation.DocumentationResult;
import com.intellij.platform.backend.presentation.TargetPresentation;
import dev.flikas.spring.boot.assistant.idea.plugin.documentation.service.DocumentationService;
import dev.flikas.spring.boot.assistant.idea.plugin.metadata.index.hint.Hint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HintDocumentationTarget implements ProjectDocumentationTarget {
  @NotNull private final Hint hintValue;
  @NotNull private final Project project;


  public HintDocumentationTarget(@NotNull Hint hintValue, @NotNull Project project) {
    this.hintValue = hintValue;
    this.project = project;
  }


  @Override
  @NotNull
  public Project getProject() {
    return this.project;
  }


  @SuppressWarnings("UnstableApiUsage")
  @Override
  public @NotNull Pointer<HintDocumentationTarget> createPointer() {
    return Pointer.hardPointer(this);
  }


  @SuppressWarnings("UnstableApiUsage")
  @Override
  public @NotNull TargetPresentation computePresentation() {
    return TargetPresentation.builder(hintValue.value())
        .icon(hintValue.icon())
        .presentation();
  }


  @Override
  public @Nullable DocumentationResult computeDocumentation() {
    return DocumentationResult.documentation(DocumentationService.getInstance(project).generateDoc(hintValue));
  }
}
