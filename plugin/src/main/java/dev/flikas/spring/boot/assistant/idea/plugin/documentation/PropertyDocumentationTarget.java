package dev.flikas.spring.boot.assistant.idea.plugin.documentation;

import com.intellij.icons.AllIcons;
import com.intellij.model.Pointer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.platform.backend.documentation.DocumentationResult;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.presentation.TargetPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import dev.flikas.spring.boot.assistant.idea.plugin.documentation.service.DocumentationService;
import dev.flikas.spring.boot.assistant.idea.plugin.metadata.index.MetadataProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class PropertyDocumentationTarget implements ProjectDocumentationTarget {
  private final MetadataProperty property;
  private final Project project;


  public PropertyDocumentationTarget(MetadataProperty property) {
    this.property = property;
    this.project = property.getIndex().project();
  }


  @Override
  public Project getProject() {
    return this.project;
  }


  @Override
  public @NotNull Pointer<? extends DocumentationTarget> createPointer() {
    return Pointer.delegatingPointer(Pointer.hardPointer(property), PropertyDocumentationTarget::new);
  }


  @Override
  public @NotNull TargetPresentation computePresentation() {
    String locationText = property.getSourceType().map(PsiElement::getContainingFile).map(PsiFile::getVirtualFile)
        .map(f -> ProjectFileIndex.getInstance(project).getOrderEntriesForFile(f))
        .map(l -> l.stream().map(OrderEntry::getPresentableName).distinct().collect(Collectors.joining(", ")))
        .orElse(null);

    return TargetPresentation.builder(property.getNameStr())
        .icon(property.getIcon().getSecond())
        .containerText(property.getMetadata().getSourceType())
        .locationText(locationText, AllIcons.Nodes.Library)
        .presentation();
  }


  @Override
  public @Nullable DocumentationResult computeDocumentation() {
    return DocumentationResult.documentation(DocumentationService.getInstance(project).generateDoc(this.property));
  }
}
