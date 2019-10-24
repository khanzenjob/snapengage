

## How to run


1. Download and install [Java JDK ](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. Download and install Maven using brew. If you don't have brew, [here's a handy tutorial to install it](https://www.howtogeek.com/211541/homebrew-for-os-x-easily-installs-desktop-apps-and-terminal-utilities/). 
To install Maven, follow [this tutorial](https://github.com/rajivkanaujia/alphaworks/wiki/Installing-Maven)
- If you don't have brew, you can install Maven downloading it directly from [here](https://maven.apache.org/download.cgi) and [following the instructions](https://www.baeldung.com/install-maven-on-windows-linux-mac) to configure it.
3. Once you have mvn running, go to the qa-automation folder, where you can:
 - Run all of the tests with `mvn test`
 - Run a specific test with `mvn -Dtest=testName test`
