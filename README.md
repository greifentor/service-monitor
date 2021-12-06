# service-monitor

A simple tool to observe web services e. g. for health and reachability.


## Requirements

Java 11, Maven 3.


## Build

Build the project via `mvn clean install`.


## Run

The application can be started with

`java -jar target/console/service-monitor-console.jar --file=console/src/main/resources/example-configuration.yml`

and executes the example configuration file with a call to www.google.de.


### Program Parameters

#### file

**(Required)** The file option defines the configuration file which is used for the run.

Example:

`--file=console/src/main/resources/example-configuration.yml`

Starts the application and runs the checks defined in the file `console/src/main/resources/example-configuration.yml`.

#### repeatInSeconds

(Optional) If the parameter is set with a long value in range 0 .. Long.MAX_VALUE, the console runner will repeat all 
the checks any seconds passed with the parameter.

Example:

`--repeatInSeconds=42`

Will start another run of the checks any 42 seconds.


### The Configuration File

The configuration file contains the necessary data for the call. The file is structured in check groups which contains
the lists of single checks.

Each check calls an URL and allows to check the call result by a simple script language.

#### Groups

A group has a name and could contain a list of checks. 

```
groups:
- name: The name of the group
  output:
  - name
    ... see below
  checks:
  - the check data (see below)
```

##### output

The output contains a list of columns for the checks. The fields of the columns are:

| Field           | Description                                        |
| --------------- | -------------------------------------------------- |
| align           | An alignment for the column (CENTER, LEFT, RIGHT). |
| content         | The content of the column (see below).             |
| id              | The id of the column.                              |
| name            | The name of the column.                            |
| width           | The width of the column.                           |

#### Checks

These data contain the configuration of a particular check.

| Field                | Description                                                  |
| -------------------- | ------------------------------------------------------------ |
| authenticationBearer | (optional) An authentication token (without "Bearer" key word) if necessary for URL access. |
| checkExpression      | The expression to check the return (see below).              |
| host                 | The name of the host which is to check.                      |
| name                 | The name of the check.                                       |
| outputAlternatives   | (optional) The output alternative configuration (see below). |
| path                 | (optional) A path as extension for the host.                 |
| port                 | (optional) The port number.                                  |
| returnType           | A type for the awaited return (JSON, STRING, XML).           |

##### outputAlternatives

The output alternatives contains a configuration for an alternate output of this column for the related check. The 
fields of the columns are:

| Field           | Description                                        |
| --------------- | -------------------------------------------------- |
| content         | The content of the alternative column (see below). |
| id              | The id of the column with alternative content.     |

#### Column Contents

Each column can contain a string with the pattern for the columns output. This string can contain place holders as
described below:

| Place Holder Id | Description                                        |
| --------------- | -------------------------------------------------- |
| name            | The name of the check request.                     |
| status          | The status of the check result (OK, FAIL or ERROR. |
| url             | The called url.                                    |

This place holder are to access by having a `${PLACE_HOLDER_ID}` in the content string. Additionally it is possbile to 
have field of the response of the call in this string. This can be addressed by `$F{PATH}`. This is working with XML 
and JSON returns only. For example: `$F{status.value}` for returned JSON

```
{
  "status": {
    "value": "OK"
  }
}
```

would return "OK".


#### Example File

For an example see [example-configuration.yml](console/src/main/resources/example-configuration.yml).


## Components

### Service

This module contains the business logic to access web services and run check request against them.

### GUI

#### Console

Reads a configuration of check requests from a file, runs them by using the service component and prints the result
through the console.

#### Desktop

Same as the console does but having it in a more convenient (Swing) desktop application.