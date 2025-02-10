package dev.flikas.spring.boot.assistant.idea.plugin.documentation;

import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.lang.documentation.psi.UtilKt;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.platform.backend.documentation.DocumentationLinkHandler;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.documentation.LinkResolveResult;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Workaround for hyperlinks in {@link DocumentationTarget} is not working.
 */
@SuppressWarnings({"removal", "UnstableApiUsage"})
public class ProjectDocumentationLinkHandler implements DocumentationLinkHandler {
  @Override
  public @Nullable LinkResolveResult resolveLink(@NotNull DocumentationTarget target, @NotNull String url) {
    if (target instanceof ProjectDocumentationTarget pdt) {
      Project project = pdt.getProject();
      @Nullable Pair<@NotNull PsiElement, @Nullable String> resolved =
          DocumentationManager.targetAndRef(project, url, null);
      if (resolved == null) return null;
      return LinkResolveResult.resolvedTarget(UtilKt.psiDocumentationTargets(resolved.getFirst(), null).getFirst());
    } else {
      return null;
    }
  }
}
