# newTraMBU

Since creating the initial version of TraMBU a few years have passed. 
I have changed jobs, I moved to a different city, and I have a new girlfriend. 
I am now working in a company that pushes us to learn and grow as technical professionals.
We have regular cross-team meetings where we study different approaches, learn working with new technologies, 
and teach each other from our own experience.  

One of these knowledge sharing activities is reading technical books together. 
Our book of choice at this moment is: Patterns, Principles, and Practices of Domain-Driven Design - Millet, Tune 2015 [link to book](https://www.oreilly.com/library/view/patterns-principles-and/9781118714706/).   
This book talks about how to tackle a lot of frustrations that developers can have while working in an existing domain,
and having to make changes to it.

This project revamp is meant to be a case study in clean project set-up, and usage of good coding practices.
I've talked about this project on my personal blog: [Doji's Colony](http://www.doji.be/2019-05-04-pet-project-redesign/)

## Quality metrics

My SonarQube metrics can be found [here](https://sonarcloud.io/dashboard?id=be.doji.productivity%3Anewtrambu)

Other metrics are less tangible as they are my subjective feeling of happiness when working in this codebase compared
to the old codebase.

## Mission

### Issue
As I am mostly using this application to practise working with different front-end,
I want to have a design that allows me to focuss on either the front-end, back-end or domain logic
without having changes radiate across multiple levels.

### Context of exercise
I have read a few books on architecture and design, and am currently reading the DDD book by *Scott Millet*.
This design approach seems to focus on splitting the *core logic* from the *non-core logic* and claims to provide ways
to make an application resistant to both domain changes as well as technology changes.

### Personal remarks
Approaches like these usually sound very good in theory, but my practical experience shows me that
apply these design frameworks too rigorously end up overcomplicating what in essence is a very simple application (such as this).
However, since this project was started as a way to play with different technological solutions, it seems like a good candidate to 
verify the claims made in the book.

**I want to see if I actually end up feeling more comfortable working in my own codebase, without having the urge to
completely redesign it at every step.**

## General application requirements/desires

### End-user perspective

* As a working professional, I want an application that allows me to keep track of the tasks and activities 
I need or want to perform. I want a portable application that I can use relatively easily anywhere I go, 
and I want to be able to sync my data without too much trouble and without being stuck to a proprietary 
format. 

* I want to be able to track how much time I spend on each task. Since I am required to give daily reports on
what projects I spend my time on at work, I want to be able to generate daily reports of my activities.
I also want to be able to backtrack and get weekly overviews of what I worked on.

* I want to be able to see these times in a human readable format (hours, minutes, seconds) as well as in some
more exotic formats such are used in various spreadsheets and time-registration software packages.
In essence I want to be able to reason in hours and minutes, while being able to simply convert these to 
excel sheets where some manager decided that we will register time as doubles, and that "0.5 hours" is how you  
write down that you spend 30 minutes on a task.

* If my employer asks me to correct an entry I made in the time-registration format, I'd like to be able to look that up quickly
without jumping through hoops, or have a hour-long meditation session to figure out what I was working on on monday afternoon 2 months ago.

* I'd like to be able to group activities/tasks in a logical way.

* I'd like to be able to create and fill in tasks, based on predefined templates.

* I want to be able to see short to-do lists (both daily, monthly, whatever)

* I want to be able to attach notes to my tasks and keep track of when I made the note


### Technical perspective

* I want to be able to change my mind on how my data is shown, and how the UI looks and feels on a whim without having to recode the entire application

* I want to be as independent as possible from technologies, as I tend to see cool apps that do front-end stuff in a certain way, and want to
emulate that within a few days.

## Approach

### Apply DDD methodology

* Upfront high-level design of core/non-core domains
  ** starting from interaction with users (which is me, at this point)
* Design the core domains in details
* Spend less attention to non-core domains
  ** Chances are, I am going to neglect or throw them out in the future anyway
* Incremental workflow 
* Keep track of what I am doing, and what my thoughts are, so I learn something
* One man team, so don't go too crazy on external tooling

## Envisioned structure

**Dependencies from top to bottom**
  * top-level module for infrastructure
    * data storage
    * data representation
    * logging
  * top-level module for business logical
    ** converters, combinations, etc
    ** preprocessors
  * top-level module per domain
    ** Core concepts only => clinically clean
    *** No toString() shizzle in the domain object, as this is a representation concern