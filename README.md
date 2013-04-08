# Epona

A simple pure Java tool to crawl Java projects from popular forges and to extract metrics from them.

## Usage

### Building in your machine

Epona was built using Maven. In order for it to behave like an Eclipse project, you'll need to install [1] Maven, and then run:

	mvn eclipse:eclipse

### Supported Forges

* GitHub
* Google Code
* SourceForge

### Crawlers

### Forge Search

* **GitHub**:
Epona uses the [GitHub API v3] to search for repositories on GitHub

* **Google Code**:
to be written

* **SourceForge**:
to be written

## How to contribute

Want to contribute with code, documentation or bug report?

Just add an issue and fix them! :)
Try the [existing ones](https://github.com/fjsj/epona/issues)

[1]: Consider installing the [m2e] plugin for Eclipse

[GitHub API v3]: http://developer.github.com/
[m2e]: http://eclipse.org/m2e/