![Trambu Header](./docs/images/trambu_header.png "Screenshot of Trambu")

|Metrics||
|:---------|---------|
|[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=be.doji.productivity%3Anewtrambu&metric=alert_status)](https://sonarcloud.io/dashboard?id=be.doji.productivity%3Anewtrambu)| [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=be.doji.productivity%3Anewtrambu&metric=coverage)](https://sonarcloud.io/dashboard?id=be.doji.productivity%3Anewtrambu)| 
 



## About 

The history and general idea (*salutes*) of this project can be found on our [About Page](docs/ABOUT.md)



## How to run

Either check out the latest source code, or download one of our packaged releases.

### Packaged releases

Go to [Our releases page](https://github.com/justDoji/TraMBU/releases(, and download the latest version.
Be sure you have the Java runtime environment installed on your computer.
Try this by running

> java -version

If the Java Runtime is installed, you should see ouput containing the version and installation location. 
If you get a 'command not found' message, head over to the [Oracle site](https://www.oracle.com/technetwork/java/javase/downloads/index.html) and download the Java Runtime Environment.
TRAMBU works best with the Java 8 runtime environment.

To run TRAMBU, navigate to the folder containing the downloaded *'.jar'* file, open a command prompt and execute:

> java -jar trambu-1.1.0-ALPHA.jar

You should see a bunch of lines being printed to your terminal.
When the output line *'Started TrambuWebApplication in XXX seconds'* is displayed,
TRAMBU will have started a web application that you can used by navigating your browser to:

> http://localhost:8080/index.xhtml00


## Contributing

We are always looking for your input!  
Take a look at our [Contribution Guide](CONTRIBUTING.md)

# Issue tracker labels and flow

## User labels

**Feature Request** : general idea on a new feature you would like added to the project  
**Bug Report** : Something that is going wrong  
**Enhancement** : Improvements on existing features  

## Team labels

**Story** : Accepted requests  
**XS**,**S**,**M**,**L**,**XL** : Effort needed to complete the story

# License

TraMBU is licened under the AGPL license. Basically: You are free to use, fork, and modify the code.
However, the original author must be accredited. Any code changes must be made public under  
the the same license. 