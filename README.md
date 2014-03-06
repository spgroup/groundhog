# Groundhog
[![Build Status](https://travis-ci.org/spgroup/groundhog.png?branch=master)](https://travis-ci.org/spgroup/groundhog)

For discussions and suggestions, please refer to the [Groundhog list](https://groups.google.com/forum/#!forum/projeto-groundhog) on Google Groups.

Groundhog is an easy to use framework for crawling raw GitHub data and to extract metrics from it. It leverages the power of the Java language, as well as the Github plataform, to help researchers to better understand software repositories. Groundhog goals are flexibility, extensibility and simplicity.

**WARNING:** Groundhog is currently alpha-software and its API **will** suffer major changes. The current version is an experiment that showcases using the GitHub API and the JavaCompiler.

On the developer side, Groundhog focuses on simplicity by shipping with a bare stack, allowing a research to get started quickly while making it easy to extend the application as and when they see fit.

It is currently alpha software and it supports:

* Partially integrated with GitHub API
* Collect Java code metrics
* Plug-and-play features

Groundhog will become beta, as soon as we fully-integrated it with GitHub API

## Build

Groundhog uses Java 7 features, so you must have it installed before build. Groundhog also uses [Maven](maven.apache.org), so to build the project you will need to download and install the tool.

#### Compiling

You can run the following command:

```
$ mvn package
```

To compile without running the test suite:

```
$ mvn package -DskipTests
```

#### Bulding for Eclipse

In order for it to behave like an Eclipse project, you'll need to run the following command in the command line:

```
$ mvn eclipse:eclipse
```

If you prefer, you can just use the groundhog.jar file from command line. You can generate this file in two ways:

### Generating the JAR via Eclipse

Eclipse users can go to `File > Export > Runnable JAR File` and enter the `CmdMain` class for the option "Launch Configuration".

### Generating the JAR via command line

Maven users can simply type in the root directory:

```
$ mvn package
```

and the jar will be created in the target/ path.


### Running tests

```
$ mvn test
```

## Usage

You can use Groundhog in two ways: as an executable JAR from the command line or as a library in your own Java project.

### Using as a executable JAR

Search GitHub for projects matching "phonegap-facebook-plugin" and place the results (if any) in a folder called metrics:

```shell
$ java -jar groundhog.jar -forge github -out metrics phonegap-facebook-plugin
```

### Using as a third-party library

#### Fetching Metadata

Metadata is fetched from GitHub's API. In order to be able to fetch more objects, you need to  [obtain your GitHub API token](https://github.com/settings/applications) and use it in Groundhog.

#### Project

You can use Groundhog to fetch metadata on a list of projects that attend to a criteria

```java
// Create a GitHub search object
Injector injector = Guice.createInjector(new SearchModule());
SearchGitHub searchGitHub = injector.getInstance(SearchGitHub.class);

// Search for projects named "opencv" starting in page 1 and stoping and going until the 3rd project
List<Project> projects = searchGitHub.getProjects("opencv", 1, 3);
```

Alternatively, you can search for projects without setting the limiting point. In this case Groundhog will fetch projects until your API limit is exceeded.

```java
List<Project> projects = searchGitHub.getProjects("eclipse", 1, SearchGitHub.INFINITY)
```

#### Issues

Issues are objects that only make sense from a Project perspective.

To fetch the Issues of a given project using Groundhog you should first create the Project and then tell Groundhog to hit the API and get the data.

```java
User user = new User("joyent");         // Create the User object
Project pr = new Project(user, "node"); // Create the Project object

// Tell Groundhog to fetch all Issues of that project and assign them the the Project object:
List<Issue> issues = searchGitHub.getAllProjectIssues(pr);

System.out.println("Listing 'em Issues...");
for (Issue issue: issues) {
    System.out.println(issue.getTitle());
}
```

#### Commits

You can easily fetch all commits of a project

```java
User user = new User("gustavopinto");
Project project = new Project(user, "groundhog-case-study");

List<Commit> commits = searchGitHub.getAllProjectCommits(project);

for (Commit com: commits) {
    System.out.println(com);
}
```

#### Milestones

Just like Issues, Groundhog lets you fetch the list of Milestones of a project, too.

```java
List<Milestone> milestones = searchGitHub.getAllProjectMilestones(pr);
```

#### Languages

Software projects are often composed of more than one programming language. Groundhog lets you fetch the list of languages of a project among its LoC (lines of code) count.

```java
// Returns a List of Language objects for each language of project "pr"
List<Language> languages = searchGitHub.fetchProjectLanguages(pr);
```

#### Contributors

You can also get the list of people who contributed to a project on GitHub:

```java
Project project = new Project("rails", "rails"); // project github.com/rails/rails

List<User> contributors = searchGitHub.getAllProjectContributors(project);
```

## Local Data Extraction

In addition to the metadata extraction allowed via the GitHub API, Groundhog covers local data extraction onto repositories via a Git interface

You can, for example, count the number of commits in a project that include a Java file, via a `GitCommitExtractor` object:

```java
GitCommitExtractor extractor = new GitCommitExtractor();
File project = new File("/tmp/elasticsearch");

extractor.numberOfCommitsWithExtension(project, "java");
```

## Database Support

Groundhog comes with built-in support for MongoDB as data storage system. Here's an example of how you can persist objects into a datastore:

```java
// Establishes connection with the local MongoDB server
// declaring the host of Mongo and the name of the DB
GroundhogDB db = new GroundhogDB("127.0.0.1", "myGitHubResearch");

Project project = new Project("yahoo", "samoa");

// Fetches all commits of the project and persists each one of them to the database
List<Commit> commits = searchGitHub.getAllProjectCommits(project);

for (Commit comm: commits) {
    db.save(comm);
    System.out.println(comm);
}
```

Refer to the [full Database Support Guide] in the wiki for more information.

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

* Jesus Silva {jjss@cin.ufpe.br}

* Marlon Reghert {mras@cin.ufpe.br}

* Tomer Simis {tls@cin.ufpe.br}

## Contributions

Want to contribute with code, documentation or bug report? That's great, check out the [Issues] page.

Preferably, your contribution should be backed by JUnit tests.

Stability and maintainability are important goals of the Groundhog project. Well-written, tested code and proper documentation are very welcome.

## License

Groundhog is released under GPL 2.

[GitHub API v3]: http://developer.github.com/
[Wiki]: https://github.com/spgroup/groundhog/wiki
[Issues]: https://github.com/spgroup/groundhog/issues
[full Database Support Guide]: https://github.com/spgroup/groundhog/wiki/Database-Support
