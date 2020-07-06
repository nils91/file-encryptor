# MathParser

Interface definition in [interface/interface/interface.yaml](./interface/interface/interface.yaml). Use [https://editor.swagger.io](https://editor.swagger.io) for syntax highlighting and codegen.

## Projects

The server implementation is divided into 5 projects. All projects are maven projects. The scope of each project is as follows:

### interface

Contains the interface definition. The interface definition ([interface/interface/interface.yaml](./interface/interface/interface.yaml)) is used to generate several java classes, which are then used to implement the server.

### grammar

Contains the antlr4 grammar file ([latexmath.g4](./grammar/src/main/antlr4/de/dralle/generated/antlr4/latexmath.g4)). This is used to generate the parser.

### term

Implementation of the term rewrite system used for symbolic checks. Also contains the numerical implementation and several utility classes. Has the grammar project as dependency.

### services

Contains all the server specific implementation. Has the interface and term projects as dependency.

### deploy-debug

Takes the server from the base project and packages it with some xml descriptor files in a war container, which can be deployed in a tomcat server. The war file can be found in [deploy-debug/target/MathParserDev.war](./deploy-debug/target/MathParserDev.war) after compilation.

## Running the Server

The server can be run in a tomcat (download [here](https://tomcat.apache.org/download-90.cgi)) by downloading the .war file from releases and copying it into the /webapps/ subfolder of the tomcat root directory. The server can be started by running /bin/startup.[bat|sh]. It will run on port 8080 by default, this can be changed in /conf/server.xml.
An installed JRE/JDK is required to run the server. Tomcat uses the JAVA_HOME environment variable, so make sure thats set to the correct location.

## Building the Server

If you want to build the server, you need an installed [maven](https://maven.apache.org/download.cgi) and an installed JDK (JRE isn´t enough), for example [OpenJdk](https://openjdk.java.net/) 12. Then just run ```mvn clean install``` in the root directory of this project. After all submodules are built, the compiled .war can be found in [deploy-debug/target/MathParserDev.war](./deploy-debug/target/MathParserDev.war).

[Github](https://github.com/nils91/mathparser/)