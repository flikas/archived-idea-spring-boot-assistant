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
import dev.flikas.spring.boot.assistant.idea.plugin.metadata.index.MetadataGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class GroupDocumentationTarget implements ProjectDocumentationTarget {
  private final MetadataGroup group;
  private final @NotNull Project project;


  public GroupDocumentationTarget(MetadataGroup group) {
    this.group = group;
    this.project = group.getIndex().project();
  }


  @Override
  public @NotNull Project getProject() {
    return this.project;
  }


  @Override
  public @NotNull TargetPresentation computePresentation() {
    String locationText = group
        .getSourceType()
        .map(PsiElement::getContainingFile)
        .map(PsiFile::getVirtualFile)
        .map(f -> ProjectFileIndex.getInstance(project).getOrderEntriesForFile(f))
        .map(l -> l.stream().map(OrderEntry::getPresentableName).distinct().collect(Collectors.joining(", ")))
        .orElse(null);
    OrderEntry a;
    return TargetPresentation.builder(group.getNameStr())
        .icon(group.getIcon().getSecond())
        .containerText(group.getMetadata().getSourceType())
        .locationText(locationText, AllIcons.Nodes.Library)
        .presentation();
  }


  @Override
  public @NotNull Pointer<? extends DocumentationTarget> createPointer() {
    return Pointer.delegatingPointer(Pointer.hardPointer(group), GroupDocumentationTarget::new);
  }


  @Override
  public @Nullable DocumentationResult computeDocumentation() {
    return DocumentationResult.documentation(DocumentationService.getInstance(project).generateDoc(this.group));
  }
}
