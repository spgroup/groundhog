# Groundhog
[![Build Status](https://travis-ci.org/spgroup/groundhog.png?branch=master)](https://travis-ci.org/spgroup/groundhog)

A Java project to crawl raw GitHub data and to extract metrics from it

## Usage

Groundhog uses Java 7 features, so you must have it installed before build.

### Building in your machine

Groundhog was built using Maven, so to build the project you will need to run the tool and let it fetch all the dependencies:

```
$ mvn package
```

### Bulding for Eclipse

In order for it to behave like an Eclipse project, you'll need to install the Maven plugin and then run:

```
$ mvn eclipse:eclipse
```

### Generating the JAR

Generate the JAR file for the Groundhog project.

Eclipse users can go to `File > Export > Runnable Jar File` and enter the `CmdMain` class for the option "Launch Configuration".

### Running Groundhog

Search GitHub for projects matching "phonegap-facebook-plugin" and place the results (if any) in a folder called metrics:

```shell
$ java -jar groundhog.jar -forge github -out metrics phonegap-facebook-plugin
```

### Running tests

```
$ mvn test
```

## Info

### Supported Forges

* GitHub
* Google Code
* SourceForge

### Crawlers

### Supported Programming Languages

* Java, parsing only (more to be added later)

### Forge Search

* **GitHub**:
Groundhog uses the [GitHub API v3] to search for repositories on GitHub

* **Google Code**:
to be written

* **SourceForge**:
to be written

## Documentation

Groundhog features a [Wiki], where you can browse for more information.

You can generate the Javadoc files with the following command:

```
$ cd src/
$ javadoc -d src/src/groundhog br.cin.ufpe.groundhog
```

## Core team

* Flávio Junior {fjsj@cin.ufpe.br}

* Gustavo Pinto {ghlp@cin.ufpe.br}

* Rodrigo Alves {rav2@cin.ufpe.br}

* Danilo Neves Ribeiro {dnr2@cin.ufpe.br}

* Fernando Castor {myfamilyname@cin.ufpe.br}

## How to contribute

Want to contribute with code, documentation or bug report? That's great, check out the [Issues] page.

Preferably, your contribution is followed by JUnit tests and the submitted code is in agreement

Stability and maintainability are important goals of the Groundhog project. Well-written, tested code and proper documentation are very welcome.

## License

Groundhog is released under GPL 2.

[GitHub API v3]: http://developer.github.com/
[Wiki]: https://github.com/spgroup/groundhog/wiki
[Issues]: https://github.com/spgroup/groundhog/issues
