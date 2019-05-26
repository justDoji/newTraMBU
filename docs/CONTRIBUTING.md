# Introduction

> Thank you for considering contributing to TraMBU. We really appreciate your interest in our project, and would love to welcome you as a contributor.

### Please read TraMBU's contribution guidelines

Following these guidelines helps to communicate that you respect the time of the people managing and developing this open source project. 
In return, they should reciprocate that respect in addressing your issue, assessing changes, and helping you finalize your pull requests.

### How can you help?

There are many ways you can help TraMBU to become better. Even if you do not feel comfortable writing and sharing code just yet. 
You can help by writing tutorials or blog posts, improving the documentation, submitting bug reports and feature requests or writing code which can be incorporated into TraMBU.


### Explain contributions you are NOT looking for (if any).

Please, don't use the issue tracker for support questions. If you have a question about setting up or running the code,
check whether our documentation provides an answer for you. If you are still struggling, feel free to send a message to TraMBU's maintainers.

# Ground Rules

 * Ensure that code that goes into core passes SonarCloud's verifications. We appreciate it if any Code smells that are still present are explained.   
 * Create issues for any major changes and enhancements that you wish to make. Discuss things transparently and get community feedback.
 * Don't add any classes to the codebase unless absolutely needed. Err on the side of using functions.
 * Keep feature versions as small as possible, preferably one new feature per version.
 * Be welcoming to newcomers and encourage diverse new contributors from all backgrounds.
 * Adhere to our core values of *Transparency*, *Mutual respect*, *Courage*, and *Constructiveness* 

# Your First Contribution
 Unsure where to begin contributing to TraMBU? You can start by looking through the help-wanted issues:.

* **Help wanted issues** - issues which are a good place to start to get to know our codebase. These issues are generally low in complexity and effort (XS - S tag).

The processes followed on github can be daunting to new contributors.
To help you overcome this hurdle, some friendly people have created a whole bunch of resources on how to do it.
Here are a couple of tutorials that can help you get started on your journey of contributing to Open Source projects: 

* [Guide on contributing to open source projects](https://opensource.guide/how-to-contribute/)
* [Make a Pull Request -- egghead tutorial series](http://makeapullrequest.com/) 

>If a maintainer asks you to "rebase" your Pull Rrequest (PR), they're saying that a lot of code has changed since you started you work, 
> and that you need to update your branch so it's easier to merge, and to avoid the increased possibility of bugs crawling into our code.

# Getting started

## General guidelines

* When submitting a PR, please fill out the [Pull request template](../.github/PULL_REQUEST_TEMPLATE/pull_request_template.md).
* When submitting a pull request, you certify to be in agreement with the [DCO](DCO.md)
* Before you submit a PR, be sure all tests in the application code pass. Do this by running the gradle task 'gradlew build'.
* Follow the [Google Java Code Style](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml) for your code
  * You can easily do this by regulary pressing the 'reformat code' hotkey in your IDE (ctrl + alt + l)

## For something that is bigger than a one or two line fix:

>1. Create your own fork of the code
>2. Do the changes in your fork
>3. If you like the change and think the project could use it:
    * Be sure you have followed the code style for the project.
    * Sign the Contributor License Agreement
    * Note the jQuery Foundation Code of Conduct.
    * Send a pull request indicating that you have a CLA on file.

## Whoopsy-daisy fixes

These fixes may be pushed to master if they meet the general requirements.
If you do not have commit access to our repository, let one of the maintainers know your fix,
and ask them to commit it for you.

>* Spelling / grammar fixes
>* Typo correction, white space and formatting changes
>* Comment clean up
>* Bug fixes that change default return values or error codes stored in constants
>* Adding logging messages or debugging output
>* Changes to ‘metadata’ files like Gemfile, .gitignore, build scripts, etc.


# How to report a bug

* For general bug reports, follow the template provided when creating an issue ticket.
* If you find a security vulnerability, do NOT open an issue. Send us a message through github instead.


# Code review process

The core team will review any Pull Requests regularly, and provide feedback where applicable.

# Community

[Stijn](https://github.com/justDoji) - *Lead dev*, *creator*   
[Ireen](https://github.com/IreenVL) -  *Product owner*, *assistant front-end designer*