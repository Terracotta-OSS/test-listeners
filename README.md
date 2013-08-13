# Junit listeners
This module contains a set of Junit listeners :
  * CacheManagerProperlyShutdownListener : is a listener that writes a report listing all the tests that did not shutdown all the cache managers (to target/CacheManagerProperlyShutdownListener-results.txt)
  * SystemExitListener : is a listener that writes surefire-like reports for tests that crashed the VM running them (to target/surefire-reports); this listener is a workaround for [SUREFIRE-910](https://jira.codehaus.org/browse/SUREFIRE-910?focusedCommentId=330467&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-330467)

## Example :

  * Add to your pom :

under dependencies :
```xml
    <dependency>
      <groupId>org.terracotta.junit</groupId>
      <artifactId>test-listeners</artifactId>
      <version>1.0.0-SNAPSHOT</version>
    </dependency>
```

under build/plugins :
```xml
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>2.15</version>
      <executions>
        <execution>
          <id>default-test</id>
          <phase>test</phase>
          <goals>
            <goal>test</goal>
          </goals>
          <configuration>
            <properties>
              <property>
                <name>listener</name>
                <value>net.sf.ehcache.SystemExitListener</value>
              </property>
            </properties>
          </configuration>
        </execution>
      </executions>
    </plugin>
```

and run your tests with mvn clean test

## How to build :
Simply clone this repo and run mvn clean install

## Is it in Maven central ? :
No, you will have to add Terracotta maven repositories to your pom.xml :

```xml
  <pluginRepositories>
    <pluginRepository>
      <id>terracotta-snapshots</id>
      <url>http://www.terracotta.org/download/reflector/snapshots</url>
    </pluginRepository>
    <pluginRepository>
      <id>terracotta-releases</id>
      <url>http://www.terracotta.org/download/reflector/releases</url>
    </pluginRepository>
  </pluginRepositories>
```

## Authors :
This plugin was developed at [Terracotta](http://www.terracotta.org), by

- [Anthony Dahanne](https://github.com/anthonydahanne/)