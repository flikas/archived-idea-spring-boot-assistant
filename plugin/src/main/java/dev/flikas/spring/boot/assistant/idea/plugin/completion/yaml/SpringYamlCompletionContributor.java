package dev.flikas.spring.boot.assistant.idea.plugin.completion.yaml;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;
import dev.flikas.spring.boot.assistant.idea.plugin.filetype.SpringBootConfigurationYamlFileType;
import org.jetbrains.yaml.YAMLLanguage;

import static com.intellij.patterns.PlatformPatterns.virtualFile;

public class SpringYamlCompletionContributor extends CompletionContributor {
  public SpringYamlCompletionContributor() {
    extend(
        CompletionType.BASIC,
        PlatformPatterns.psiElement().withLanguage(YAMLLanguage.INSTANCE)
            .inVirtualFile(virtualFile().ofType(SpringBootConfigurationYamlFileType.INSTANCE)),
        new YamlCompletionProvider()
    );
  }
}
