
# Surveillance Data Platform Content Based Routing (SDP-CBR)
This Github organization was created for use by [CDC](http://www.cdc.gov) programs to collaborate on public health surveillance related projects in support of the [CDC Surveillance Strategy](http://www.cdc.gov/surveillance). This third party web application is not hosted by the CDC, but is used by CDC and its partners to share information and collaborate on software.

This repository serves as a template for other repositories to follow in order to provide the appropriate notices for users in regards to privacy protection, contribution, licensing, copyright, records management and collaboration.


## Public Domain:
This project constitutes a work of the United States Government and is not subject to domestic copyright protection under 17 USC § 105. This project is in the public domain within the United States, and copyright and related rights in
the work worldwide are waived through the [CC0 1.0 Universal public domain dedication](https://creativecommons.org/publicdomain/zero/1.0/). All contributions
to this project will be released under the CC0 dedication. By submitting a pull request you are agreeing
to comply with this waiver of copyright interest.

## License
The project utilizes code licensed under the terms of the Apache Software License and therefore is licensed under ASL v2 or later.

This program is free software: you can redistribute it and/or modify it under the terms of the Apache Software License version 2, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Apache Software License for more details.

You should have received a copy of the Apache Software License along with this program. If not, see http://www.apache.org/licenses/LICENSE-2.0.html

## Privacy
This project contains only non-sensitive, publicly available data and information. All material and community participation is covered by the Surveillance Platform [Disclaimer](https://github.com/CDCgov/template/blob/master/DISCLAIMER.md) and [Code of Conduct](https://github.com/CDCgov/template/blob/master/code-of-conduct.md). For more information about CDC's privacy policy, please visit [http://www.cdc.gov/privacy.html](http://www.cdc.gov/privacy.html).

## Contributing
Anyone is encouraged to contribute to the project by [forking](https://help.github.com/articles/fork-a-repo) and submitting a pull request. (If you are new to GitHub, you might start with a [basic tutorial](https://help.github.com/articles/set-up-git).)
By contributing to this project, you grant a world-wide, royalty-free, perpetual, irrevocable, non-exclusive, transferable license to all users under the terms of the [Apache Software License v2](http://www.apache.org/licenses/LICENSE-2.0.html) or later.

All comments, messages, pull requests, and other submissions received through CDC including this GitHub page are subject to the [Presidential Records Act](http://www.archives.gov/about/laws/presidential-records.html) and may be archived. Learn more at [http://www.cdc.gov/other/privacy.html](http://www.cdc.gov/other/privacy.html).

## Records
This project is not a source of government records, but is a copy to increase collaboration and collaborative potential. All government records will be published through the [CDC web site](http://www.cdc.gov.)

## Notices
Please refer to [CDC's Template Repository](https://github.com/CDCgov/template) for more information about [contributing to this repository](https://github.com/CDCgov/template/blob/master/CONTRIBUTING.md), [public domain notices and disclaimers](https://github.com/CDCgov/template/blob/master/DISCLAIMER.md), and [code of conduct](https://github.com/CDCgov/template/blob/master/code-of-conduct.md).

## Hat-tips
Thanks to [18F](https://18f.gsa.gov/)'s [open source policy](https://github.com/18F/open-source-policy) and [code of conduct](https://github.com/CDCgov/code-of-conduct/blob/master/code-of-conduct.md) that were very useful in setting up this GitHub organization. Thanks to CDC's [Informatics Innovation Unit](https://www.phiresearchlab.org/index.php/code-of-conduct/) that was helpful in modeling the code of conduct.

# SDP-CBR

This example demonstrates how to configure Camel routes in Spring Boot via a Spring XML configuration file.

The application utilizes the Spring [`@ImportResource`](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/annotation/ImportResource.html) annotation to load a Camel Context definition via a [camel-context.xml](src/main/resources/spring/camel-context.xml) file on the classpath.

### Building

The application can be built with the following command

    mvn clean package
	
### Databases

The application depends on a number of databases and connections in order to run.  All databases are defined in the application properties, which should be configured for the environment.  

Required databases for deployment are the phinMsDataSource and sdpqDataSource.  The phinMsDataSource should have the table message_inq, which can be created in SQLServer using the script at main/db/MSSQL_message_inq.sql.  The other tables will be created as needed by the application.
	
### Testing

The application unit tests can be run with the following command

	mvn test
	
In order for the tests to succeed, the test datasources must be properly configured.  All test datasources are specified in the test properties file at src/test/resources/application.properties.  If any of the datasources are not configured, the tests will fail.

Required databases for tests are nndssDataSource, sdpqDataSource, and phinMsDataSource.  The phinMsDataSource should have the table message_inq, which can be created in SQLServer using the script at main/db/MSSQL_message_inq.sql.  The other tables will be created as needed by the application.

### Running the application in OpenShift

It is assumed that:
- OpenShift platform is already running, if not you can find details how to [Install OpenShift at your site](https://docs.openshift.com/container-platform/3.3/install_config/index.html).
- Your system is configured for Fabric8 Maven Workflow, if not you can find a [Get Started Guide](https://access.redhat.com/documentation/en/red-hat-jboss-middleware-for-openshift/3/single/red-hat-jboss-fuse-integration-services-20-for-openshift/)

The application can be built and run on OpenShift using a single goal:

    mvn fabric8:deploy

To list all the running pods:

    oc get pods

Then find the name of the pod that runs this quickstart, and output the logs from the running pods with:

    oc logs <name of pod>

You can also use the OpenShift [web console](https://docs.openshift.com/container-platform/3.3/getting_started/developers_console.html#developers-console-video) to manage the running pods, and view logs and much more.

### Running via an S2I Application Template

Application templates allow you deploy applications to OpenShift by filling out a form in the OpenShift console that allows you to adjust deployment parameters.  This template uses an S2I source build so that it handle building and deploying the application for you.

First, import the Fuse image streams:

    oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/GA/fis-image-streams.json

Then create the quickstart template:

    oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/GA/quickstarts/spring-boot-camel-xml-template.json

Now when you use "Add to Project" button in the OpenShift console, you should see a template for this quickstart.


### Running the application locally

The maven command to build the application described above produces an all-in-one jar file that can be run locally.  The jar file is created in the target directory and can be run with the following comand.

     java -jar target/sdp-cbr-1.0.0-SNAPSHOT.jar

When running locally the application can be configured through extenal properties files according as provided by the spring boot framework.  Information on this external configuration can be found at the following location. https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-profile-specific-properties

The base properties for the application are contained in the src/main/resources/application.properties file.  This file is basically a template for fields that will need to be filled out in order for the routes to run.  Please review the file in order to understand the configuration parameters that are required.

# Filtering on HL7 message contents

The HL7Terser is designed to allow for complex filter parameters to be defined via the routes in the [camel-context.xml](src/main/resources/spring/camel-context.xml) file.  For more information on filter syntax, please review the syntax instruction in [Definitions.txt](Filter Definitions.txt).