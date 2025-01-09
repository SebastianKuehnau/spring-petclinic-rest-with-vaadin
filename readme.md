# Vaadin Integration into the REST version of Spring PetClinic Sample Application (spring-framework-petclinic) 

This project is based on the [Spring PetClinic application with Rest](https://github.com/spring-petclinic/spring-petclinic-rest) and was used as an example to show how to integrate Vaadin into a Spring project. For information on this project, please refer to the corresponding documentation. Only the integration of Vaadin is shown below.

## Integrate Vaadin into the spring project

This example demonstrates how to integrate Vaadin into a Spring project with step-by-step configuration and implementation instructions.

### Prerequisites
- JDK 17 or later
- Maven 3.5 or later (except for 3.8.2 and 3.8.3)
- Spring Boot 3.0 or later

### Maven Configuration

#### 1. Define Vaadin Version
In your `pom.xml`, define a property for the Vaadin version to simplify updates.

```xml
<properties>
    <vaadin.version>24.6.1</vaadin.version>
</properties>
```

#### 2. Set Spring Boot Starter Parent
Ensure your project uses the `spring-boot-starter-parent`.

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.4.1</version>
    <relativePath />
</parent>
```

#### 3. Use Vaadin BOM (Bill of Materials)
To manage Vaadin dependencies for a specific version, include the Vaadin BOM in the dependency management section.

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.vaadin</groupId>
            <artifactId>vaadin-bom</artifactId>
            <version>${vaadin.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

#### 4. Add Vaadin Dependencies
Add the required dependencies for Vaadin and Spring Boot integration.

```xml
<dependencies>
    <dependency>
        <groupId>com.vaadin</groupId>
        <artifactId>vaadin</artifactId>
    </dependency>
    <dependency>
        <groupId>com.vaadin</groupId>
        <artifactId>vaadin-spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```
#### 5. Add Maven Plugins
Include plugins for Vaadin and Spring Boot in your `build` section.

```xml
<build>
    <plugins>
        ...
        <plugin>
            <groupId>com.vaadin</groupId>
            <artifactId>vaadin-maven-plugin</artifactId>
            <version>${vaadin.version}</version>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-frontend</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        ...
    </plugins>
</build>
```

#### 6. Add Production Build Profile
Add a Maven profile for building the project in production mode.

```xml
<profiles>
    <profile>
        <id>production</id>
        <build>
            <plugins>
                <plugin>
                    <groupId>com.vaadin</groupId>
                    <artifactId>vaadin-maven-plugin</artifactId>
                    <version>${vaadin.version}</version>
                    <executions>
                        <execution>
                            <goals>
                                <goal>build-frontend</goal>
                            </goals>
                            <phase>compile</phase>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

#### 7. Change Packaging to JAR
Check and update your `pom.xml` to use `jar` packaging.

### Project-Specific Configurations

#### Excluding Swagger-UI Requests
Swagger UI is used in the project to document the REST interfaces. This collides with requests for Vaadin and must be explicitly excluded. This setting can be made in the `application.properties` file:

```properties
vaadin.excludeUrls=/swagger-ui/**
```

#### Redirecting Swagger-UI Requests
To keep using Swagger-UI in the project, define a `/swagger-ui` mapping in `RootRestController.java` and forward the requests to `swagger-ui/index.html`:

```java
@RequestMapping(value = "/swagger-ui")
public void redirectToSwagger(HttpServletResponse response) throws IOException {
    response.sendRedirect(this.servletContextPath + "/swagger-ui/index.html");
}
```

### Vaadin Implementation

#### 1. Create a Separate Package for Vaadin Classes and Components
To keep the project structure organized, create a separate package for all Vaadin-specific classes and components. For example:

```
org.springframework.samples.petclinic.
    └── ui
        └── VetView.java
```

#### 2. Create a Veterinarian View Class
Add a simple view to demonstrate a use case. For example, list veterinarians in a table.

```java
@Route("")
@RouteAlias("vets")
public class VetView extends VerticalLayout {
    public VetView(ClinicService clinicService) {
        var grid = new Grid<>(Vet.class);
        grid.setItems(clinicService.findVets());
        grid.setColumns("firstName", "lastName", "specialties");
        grid.setSizeFull();
        add(grid);

        setSizeFull();
    }
}
```

### Build the application

You can build the application with mavan

```bash
mvn clean install
```

To create a production build you need add the appropriate profile name

```bash
mvn clean package -Pproduction
```

### Running the application

To run the application in development mode:

```bash
mvn spring-boot:run
```

and then navigate to `http://localhost:9966/petclinic/` to see the Veterinarian view.

To benefit from a Vaadin Plugin in you IDE take a look in the [documentation of Vaadin](https://vaadin.com/docs/latest/getting-started/import)
