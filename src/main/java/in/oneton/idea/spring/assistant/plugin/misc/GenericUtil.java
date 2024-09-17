package in.oneton.idea.spring.assistant.plugin.misc;

import com.intellij.application.options.CodeStyle;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.lang.jvm.types.JvmPrimitiveTypeKind;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import in.oneton.idea.spring.assistant.plugin.suggestion.SuggestionNode;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLLanguage;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.intellij.codeInsight.completion.CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED;
import static com.intellij.codeInsight.documentation.DocumentationManagerUtil.createHyperlink;
import static com.intellij.openapi.util.text.StringUtil.containsChar;
import static com.intellij.openapi.util.text.StringUtil.endsWithChar;
import static com.intellij.openapi.util.text.StringUtil.isNotEmpty;
import static com.intellij.openapi.util.text.StringUtil.replace;
import static java.text.BreakIterator.getSentenceInstance;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

@UtilityClass
public class GenericUtil {
  private static final Pattern PACKAGE_REMOVAL_PATTERN =
      Pattern.compile("[a-zA-Z_][a-zA-Z_0-9]*\\.");
  private static final Pattern GENERIC_SECTION_REMOVAL_PATTERN =
      Pattern.compile("<(?<commaDelimitedTypes>[^>]+)>");
  //  private static final Pattern CLASSNAME_MATCH_PATTERN =
  //      Pattern.compile("([a-zA-Z_][a-zA-Z_0-9]*(?:\\.[a-zA-Z_][a-zA-Z_0-9]*)*)");

  private static final Pattern methodToFragmentConverter = Pattern.compile("(.+)\\.(.+)\\(.*\\)");

  public static String typeForDocumentationNavigation(String type) {

    return type.replaceAll("\\$", ".");
  }

  public static String shortenFrequentJavaType(CharSequence type) {
    JvmPrimitiveTypeKind ptk = JvmPrimitiveTypeKind.getKindByFqn(type.toString());
    if (ptk != null) {
      return ptk.getName();
    }
    type = trimJavaPackage(type, "java.lang");
    type = trimJavaPackage(type, "java.util");
    type = trimJavaPackage(type, "java.util.concurrent");
    return type.toString();
  }

  public static CharSequence trimJavaPackage(CharSequence type, CharSequence packageName) {
    String prefix = packageName + ".";
    if (type.toString().startsWith(prefix)
        && type.toString().indexOf('.', prefix.length()) < 0) {
      return type.subSequence(prefix.length(), type.length());
    }
    return type;
  }

  /**
   * @return length of rendered class name.
   */
  public static int updateClassNameAsJavadocHtml(StringBuilder buffer, String typeList) {
    int len = 0;
    char[] chars = typeList.toCharArray();
    StringBuilder baseClass = new StringBuilder();
    for (char c : chars) {
      if (Character.isJavaIdentifierPart(c) || c == '.') {
        baseClass.append(c);
      } else {
        String shortenName = shortenFrequentJavaType(baseClass.toString());
        createHyperlink(
            buffer,
            typeForDocumentationNavigation(baseClass.toString()),
            shortenName,
            false
        );
        baseClass.setLength(0);
        if (c == '<') {
          buffer.append("&lt;");
        } else if (c == '>') {
          buffer.append("&gt;");
        } else if (c == ',') {
          buffer.append(", ");
        } else {
          buffer.append(c);
        }
        len += shortenName.length() + 1;
      }
    }
    if (baseClass.length() > 0) {
      String shortenName = shortenFrequentJavaType(baseClass.toString());
      createHyperlink(
          buffer,
          typeForDocumentationNavigation(baseClass.toString()),
          shortenName,
          false
      );
      len += shortenName.length();
    }
    return len;
  }

  public static String methodForDocumentationNavigation(String typeAndMethod) {
    return methodToFragmentConverter.matcher(typeForDocumentationNavigation(typeAndMethod))
                                    .replaceAll("$1#$2");
  }

  @NotNull
  public static String getCodeStyleIntent(InsertionContext insertionContext) {
    final @NotNull CommonCodeStyleSettings currentSettings =
        CodeStyle.getLanguageSettings(insertionContext.getFile(), YAMLLanguage.INSTANCE);
    final CommonCodeStyleSettings.IndentOptions indentOptions = currentSettings.getIndentOptions();
    assert indentOptions != null;
    return indentOptions.USE_TAB_CHARACTER ?
        "\t" :
        StringUtil.repeatSymbol(' ', indentOptions.INDENT_SIZE);
  }

  @NotNull
  public static String getFirstSentenceWithoutDot(String fullSentence) {
    if (containsChar(fullSentence, '.')) {
      BreakIterator breakIterator = getSentenceInstance(Locale.US);
      breakIterator.setText(fullSentence);
      fullSentence = fullSentence.substring(breakIterator.first(), breakIterator.next()).trim();
    }

    if (isNotEmpty(fullSentence)) {
      String withoutDot = endsWithChar(fullSentence, '.') ?
          fullSentence.substring(0, fullSentence.length() - 1) :
          fullSentence;
      return replace(withoutDot, "\n", "");
    } else {
      return "";
    }
  }

  public static String moduleNamesAsStrCommaDelimited(List<Module> newModules,
      boolean includeProjectName) {
    return moduleNamesAsStrCommaDelimited(newModules.stream(), includeProjectName);
  }

  public static String moduleNamesAsStrCommaDelimited(Module[] newModules,
      boolean includeProjectName) {
    return moduleNamesAsStrCommaDelimited(stream(newModules), includeProjectName);
  }

  private static String moduleNamesAsStrCommaDelimited(Stream<Module> moduleStream,
      boolean includeProjectName) {
    return moduleStream.map(module -> includeProjectName ?
        module.getProject().getName() + ":" + module.getName() :
        module.getName()).collect(joining(", "));
  }

  public static String truncateIdeaDummyIdentifier(@NotNull PsiElement element) {
    return truncateIdeaDummyIdentifier(element.getText());
  }

  public static String truncateIdeaDummyIdentifier(String text) {
    return text.replace(DUMMY_IDENTIFIER_TRIMMED, "");
  }

  @SafeVarargs
  public static <T> List<T> modifiableList(T... items) {
    return new ArrayList<>(asList(items));
  }

  public static <T> List<T> newListWithMembers(List<T> itemsToCopy, T newItem) {
    ArrayList<T> newModifiableList = new ArrayList<>(itemsToCopy);
    newModifiableList.add(newItem);
    return newModifiableList;
  }

  public static String removeGenerics(String type) {
    Matcher matcher = GENERIC_SECTION_REMOVAL_PATTERN.matcher(type);
    if (matcher.find()) {
      return matcher.replaceAll("");
    }
    return type;
  }

  public static String shortenedType(String type) {
    if (type == null) {
      return null;
    }
    Matcher matcher = PACKAGE_REMOVAL_PATTERN.matcher(type);
    if (matcher.find()) {
      return matcher.replaceAll("");
    }
    return type;
  }

  public static String dotDelimitedOriginalNames(
      List<? extends SuggestionNode> matchesTopFirstTillParentNode, SuggestionNode currentNode) {
    StringBuilder builder = new StringBuilder();

    for (SuggestionNode aMatchesTopFirstTillParentNode : matchesTopFirstTillParentNode) {
      String originalName = aMatchesTopFirstTillParentNode.getOriginalName();
      if (originalName != null) {
        builder.append(originalName).append(".");
      }
    }

    String originalName = currentNode.getOriginalName();
    if (originalName != null) {
      builder.append(originalName);
    }
    return builder.toString();
  }

  public static String dotDelimitedOriginalNames(List<? extends SuggestionNode> matches) {
    return dotDelimitedOriginalNames(matches, 0);
  }

  public static String dotDelimitedOriginalNames(List<? extends SuggestionNode> matches,
      int startIndex) {
    StringBuilder builder = new StringBuilder();

    for (int i = startIndex; i < matches.size(); i++) {
      String originalName = matches.get(i).getOriginalName();
      if (originalName != null) {
        builder.append(originalName);
        boolean appendDot = i < matches.size() - 1;
        if (appendDot) {
          builder.append(".");
        }
      }
    }
    return builder.toString();
  }

  @NotNull
  public static String getIndent(String indent, int numOfHops) {
    if (numOfHops == 0) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < numOfHops; i++) {
      builder.append(indent);
    }
    return builder.toString();
  }

  @NotNull
  public static String getOverallIndent(String existingIndentation, String indentPerLevel,
      int numOfLevels) {
    return existingIndentation + getIndent(indentPerLevel, numOfLevels);
  }

  @NotNull
  public static <T extends Comparable<T>> SortedSet<T> newSingleElementSortedSet(T t) {
    SortedSet<T> suggestions = new TreeSet<>();
    suggestions.add(t);
    return suggestions;
  }

  public static Optional<String> getKeyNameOfObject(final PsiElement psiElement) {
    return Optional.of(psiElement).filter(el -> el instanceof YAMLKeyValue)
                   .map(YAMLKeyValue.class::cast).map(YAMLKeyValue::getName);
  }

}
