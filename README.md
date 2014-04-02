Example Gradle Project
======================

Example gradle project with Travis-CI compatibility. This project should contain everything you need to get started.

### Get the project

	git clone https://github.com/JAGFin1/example-gradle-project.git

### About

Once you've cloned the repo, run:

	gradle build

If you're using eclipse, you'll need to let gradle set up your project and buildpath:

	gradle eclipse build


Gradle should download the initial dependencies to your machine and add them to your project's buildpath. The current .gitignore file has been set up to ignore any gradle-specific libraries. Every new clone of this repo will need an initial build.

[![Build Status](https://travis-ci.org/JAGFin1/example-gradle-project.png?branch=master)](https://travis-ci.org/JAGFin1/example-gradle-project)
