# Contributing

Thanks for being willing to contribute!

## Project setup

Perfiz consists of
* [Perfiz](https://github.com/znsio/perfiz) - Gatling Setup (Maven and Scala), Docker Files, Config Templates etc., Script Files
  * Clone and open with Intellij Idea (our preferred IDE, we have not test it on others) as a Maven Project
  * Set PERFIZ_HOME env variable to the location where you cloned the repo so that it is easy to test your changes quickly
* [Perfiz CLI](https://github.com/znsio/perfiz-cli) - GoLang Cobra Command line interface to orchestrate above project
  * Clone and open with GoLand or your favourite IDE
  * Please trying to make sure this project will work across Platforms. Example: Be watchful about slashes and directly call shell commands. Leverage OS libraries.
  * To test changes quickly, point PERFIZ_HOME to actual Perfiz [install dir](https://perfiz.com/installation.html) or the location where you have cloned [Perfiz](https://github.com/znsio/perfiz)
* [Perfiz Documentation](https://github.com/znsio/perfiz/tree/gh-pages) - Jekyll and JustTheDocs based GitHub pages [website](https://perfiz.com)
  * [Setup Instructions](https://github.com/znsio/perfiz/tree/gh-pages#perfiz-documentation)
* [Perfiz Demo](https://github.com/znsio/perfiz-demo) - Examples
  * There are multiple branches on this repo which are self explanatory
  * Please make sure you keep these up to date

## Committing and Pushing changes

Test your changes with the [Perfiz Demo](https://github.com/znsio/perfiz-demo) branches before pushing

## Releases and Versioning

* The main project and cli projects have independent versioning which follows the standard <major>.<minor>.<patch> format
* To create a release on any of these projects
```shell
git tag -a <release version> -m "<release message>"
git push origin <release version>
```

## Help needed

Please checkout the [the open issues](https://github.com/znsio/perfiz/issues?q=is%3Aopen+is%3Aissue)

Also, please watch the repo and respond to questions/bug reports/feature
requests! Thanks!
