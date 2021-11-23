# service-monitor

A simple tool to observe web services e. g. for health and reachability.


## Requirements

Java 11, Maven 3.


## Build

Build the project via `mvn clean install`.


## Components

### Service

This module contains the business logic to access web services and run check request against them.

### GUI

#### Console

Reads a configuration of check requests from a file, runs them by using the service component and prints the result
through the console.

#### Desktop

Same as the console does but having it in a more convenient (Swing) desktop application.