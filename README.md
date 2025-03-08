> [!IMPORTANT] 
> # ARCHIVED
> This repository is ARCHIVED, the subsequent development will move to this repository: [idea-spring-boot-assistant](https://github.com/flikas/idea-spring-boot-assistant).
> 
> Because the [upstream](https://github.com/1tontech/intellij-spring-assistant) is inactive from year 2018, and current code is too different from the upstream, we decided [leave the fork network](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/working-with-forks/detaching-a-fork#manually-leaving-the-fork-network) by creating a new repository.
>
> Thanks again to the groundbreaking work by [@1tontech](https://twitter.com/1tontech).
>
> The Issues here will still be addressed, but it is more welcome to create an issue in the [new repository](https://github.com/flikas/idea-spring-boot-assistant/issues).

Spring Boot Assistant - IntelliJ-based plugin that assists you in developing spring boot applications
=====================================================================================

![Plugin in action](plugin-docs/demo-completion.gif)

## What does the plugin do

<!-- Plugin description -->

This plugin adds support for
[Spring Boot external-config files](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.files)
(application.yaml/properties, etc.).

### Features:

- Completion
    - Auto-completion of the configuration properties in your `application.yaml/properties` files based on the spring
      boot's autoconfiguration jars are present in the classpath.
    - Auto-completion of the configuration properties in your `application.yaml/properties` files if you have classes
      annotated with `@ConfigurationProperties`,
      [if your build is properly configured](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#configuration-metadata.annotation-processor).
    - Short form search & search for element deep within is also supported. i.e, `sp.d` will show you `spring.data`,
      `spring.datasource`, also, `port` would show `server.port` as a suggestion.
    - Auto-completion of the configuration property values, with support
      for [the value providers](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#configuration-metadata.manual-hints.value-providers).
- Documentation
    - Quick documentation for groups & properties (not all groups & properties will have documentation, depends on
      whether the original author specified documentation or not for any given element).
- Inspection
    - Key not defined: Report problem if the property in config file is not defined in any metadata.
    - Property deprecated: Report problem if using a deprecated property.
    - Invalid value: Report problem if the value does not match the property type.
- Navigation
    - Support for IDE feature `Go to Declaration or Usages (Ctrl+B)` in `application.yaml/properties` files.
- Editing
    - Add line separator in yaml key (press Enter), will split yaml key in proper format.
    - Enhanced `Join Lines (Ctrl+Shift+J/⌃ ⇧ J)` command, which join yaml keys in proper format.

### Bug report:

Please create an issue at [GitHub](https://github.com/flikas/idea-spring-boot-assistant/issues).

<!-- Plugin description end -->

## Installation

Both of the following two ways are fine.

### Install from JetBrains Marketplace website

1. Go to [Plugin Page](https://plugins.jetbrains.com/plugin/17747-spring-boot-assistant)
2. Hit `Get` or `Install to IntelliJ IDEA ...` button.

### Install in IDE

To install the plugin open your editor (IntelliJ) and hit:

1. Open `File > Settings > Plugins` dialog.
2. In the `Marketplace` tab, look for `Spring Boot Assistant`, hit the `Install` button.
3. Finally, hit the `OK` button, you're all done!

## Usage

Assuming that you have Spring boot's autoconfiguration jars are present in the classpath, this plugin will automatically
allow you to autocomplete properties as suggestions in your `application.yaml/properties` files.

Suggestions would appear as soon as you type or press `CTRL+SPACE`.

Short form suggestions are also supported such as, `sp.d` will show you `spring.data`, `spring.datasource`, e.t.c as
suggestions that make your typing faster.

In addition to libraries in the classpath, the plugin also allows you to have your own `@ConfigurationProperties`
available as suggestions in your `application.yaml/properties` files.

For this to work, please follow
the [Spring Boot Document](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#configuration-metadata.annotation-processor)
for your project/module.

> If the user need completion on any other files, right-click the file in the Project View, then 'Override File Type' to
> 'Spring Boot Configuration YAML/Properties'.

> If you want to look at a sample project, look inside [plugin-test](plugin-test/) directory where the above setup is
> done. These samples allow properties from `@ConfigurationProperties` to be shown as suggestions

**IMPORTANT**

> After changing your custom `@ConfigurationProperties` files, suggestions would be refreshed only after you trigger the
> build explicitly using keyboard (`Ctrl+F9`)/UI

### Known behaviour in ambiguous cases

> 1. If two groups from different auto configurations conflict with each other, the documentation for the group picked
     is random & undefined
> 2. If a group & property represent the depth, the behaviour of the plugin is undefined.

## Support

For report bugs, or request a new feature, use [Issues](https://github.com/flikas/idea-spring-boot-assistant/issues).

## Changelog

See [here](CHANGELOG.md).

## License

Spring Boot Assistant - IntelliJ-based Plugin is open-sourced software licensed under
the [MPL LICENSE](LICENSE).

## About

This project is based on [Spring Assistant - IntelliJ Plugin](https://github.com/1tontech/intellij-spring-assistant),
thanks to the excellent work by [@1tontech](https://twitter.com/1tontech).
