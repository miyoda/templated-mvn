# Templated
You can copy/link automatically all your common config files of all your projects from a template directory.
You can config to copy parts of files that is common in different projects.
It's util for common config files/parts for all subprojects/microservices like .gitignore, copyright, etc.

## Config your project
Add the dependency in you "pom.xml":
```xml
...
  <build>
    <plugins>
      <plugin>
        <groupId>es.miyoda.mvn</groupId>
        <artifactId>templated-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
      </plugin>
    </plugins>
  </build>
...
```

You must define in your "pom.xml" file with your templated reposiotories source configured:
```xml
...
  <build>
    <plugins>
      <plugin>
        <groupId>es.miyoda.mvn</groupId>
        <artifactId>templated-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
            <sources>
							<param>../templated-example-template</param>
							<param>../templated-example-template2</param>
						</sources>
        </configuration>
      </plugin>
    </plugins>
  </build>
...
```

Automate the execution in any maven goal:
```xml
...
  <build>
    <plugins>
      <plugin>
        <groupId>es.miyoda.mvn</groupId>
        <artifactId>templated-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <executions>
          <execution>
            <phase>compile</phase>
            <goals>
              <goal>templated</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
...
```

## Config your template repository
### Copy files
All files in the templated repository was copied to your project automatically.

### Copy parts
If you want to embed a part of file you can create in your templated repository a file with the name:
file-name-when-you-want-embed|embed-tag.part

For example:
>readme.md|copyright.part

The "content part" was replaced in your file with the position of the embed-tag indicated.
You must indicate in your file where you want put the content with your embed tag.
In a .md/.html file it is with:
```html
<!-- start|embed-tag --> it was replaced <!-- end|embed-tag -->
```

For example:
```html
<!-- start|copyright --> it was replaced <!-- end|copyright -->
```

If you want put in de start or de end of the file you don't need define the embed tag position.
Simply use files with tag "start" or "end" like:
> readme.md|start.part

> readme.md|end.part


You can view an example of use in:
https://github.com/miyoda/templated-example-usage.git

The example use the template:
https://github.com/miyoda/templated-example-template.git