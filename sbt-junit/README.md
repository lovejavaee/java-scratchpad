# JUnit Test Support in sbt

_31 Jan 2012_

This is an example project to demonstrate adding JUnit test support and report generation to sbt.

## Quick start

First, add JUnit test support by including `junit-interface` in the list of project dependencies (e.g. `build.sbt`):

    libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test->default"

## Optional: JUnit reports

JUnit report generation support is optional, and can be added by adding the `junit_xml_listener` plugin (e.g. `project/plugins.sbt`):

    addSbtPlugin("eu.henkelmann" % "junit_xml_listener" % "0.3")

Include the `TestsListener` implementation in the project settings (e.g. `build.sbt`):

    testListeners <<= target.map(t => Seq(new eu.henkelmann.sbt.JUnitXmlTestsListener(t.getAbsolutePath)))

You may need to install `junit_xml_listener` to your local Ivy repository:

First, grab the source code:

    $ git clone git://github.com/JamesEarlDouglas/junit_xml_listener.git

Second, install it to your local Ivy repository:

    $ cd junit_xml_listener ; sbt publish-local

Third, repair the location of the locally-published Ivy configuration:

    $ mv .ivy2/local/eu.henkelmann/junit_xml_listener/scala_2.9.1/sbt_0.11.2/0.3/ivys/ .ivy2/local/eu.henkelmann/junit_xml_listener/

## Usage

Now running the `test` command will run any JUnit tests, and publish the JUnit test reports as XML files under `target/test-reports/`.

