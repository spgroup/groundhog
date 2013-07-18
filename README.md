# Groundhog
[![Build Status](https://travis-ci.org/spgroup/groundhog.png?branch=master)](https://travis-ci.org/spgroup/groundhog)

A framework for crawling raw GitHub data and to extract metrics from it

## Build

Groundhog uses Java 7 features, so you must have it installed before build.

Groundhog was built using Maven, so to build the project you will need to run the tool and let it fetch all the dependencies:

`$ mvn package`

#### Bulding for Eclipse

In order for it to behave like an Eclipse project, you'll need to install the Maven plugin and then run:

`$ mvn eclipse:eclipse`

### Generating the JAR

Generate the JAR file for the Groundhog project.

Eclipse users can go to `File > Export > Runnable JAR File` and enter the `CmdMain` class for the option "Launch Configuration".

## Usage

You can use Groundhog in two ways: as an executable JAR from the command line or as a library in your own Java project.

### Fetching Metadata

Metadata is fetched from GitHub's API. In order to be able to fetch more objects, you need to  [obtain your GitHub API token](https://github.com/settings/applications) and use it in Groundhog.

#### Project

You can use Groundhog to fetch metadata on a list of projects that attend to a criteria

```java
// Create a GitHub search object
Injector injector = Guice.createInjector(new SearchModule());
SearchGitHub searchGitHub = injector.getInstance(SearchGitHub.class);

// Search for projects named "opencv" starting in page 1 and stoping and going until the 3rd project
searchGitHub.getProjects("opencv", 1, 3);
```

Alternatively, you can search for projects without setting the limiting point. In this case Groundhog will fetch projects until your API limit is exceeded.

```java
searchGitHub.getProjects("eclipse", 1, SearchGitHub.INFINITY)
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
for (Issue issue: Issues) {
  System.out.println(issue.getTitle());
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
User user = new User("rails");
Project project = new Project(user, "rails"); // project github.com/rails/rails

List<User> contributors = searchGitHub.getAllProjectContributors(project);
```

### Running Groundhog

Search GitHub for projects matching "phonegap-facebook-plugin" and place the results (if any) in a folder called metrics:

```shell
$ java -jar groundhog.jar -forge github -out metrics phonegap-facebook-plugin
```

### Running tests

```
$ mvn test
```

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

## Contributions

Want to contribute with code, documentation or bug report? That's great, check out the [Issues] page.

Preferably, your contribution should be backed by JUnit tests.

Stability and maintainability are important goals of the Groundhog project. Well-written, tested code and proper documentation are very welcome.

## License

Groundhog is released under GPL 2.

[GitHub API v3]: http://developer.github.com/
[Wiki]: https://github.com/spgroup/groundhog/wiki
[Issues]: https://github.com/spgroup/groundhog/issues
