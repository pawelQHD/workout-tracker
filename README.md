### Project description

This project serves as API for an exercise tracker.

When going to the gym, you will be able to record the weight, reps and sets for every exercise.

The focus will be mostly on weightlifting with support for cardio added later down the line.

Users will be able to log in or register.

Once the user is logged in they will be able to create new workouts as well as see their workout history.

### About this document

This document is here to serve as a development journal.

I will include any changes and commits as well as description on how each commit was achieved.

The goal for this project and for this document is to server as a learning guide.

The primary focus for me is to learn as much as possible as well as to make sure this knowledge isn't lost over time.

That's why this document serves both as a development journal as well as future reference I can go back and review.

Since this is my second project with Spring Boot and the second document of this type, I will try and improve any shortfalls.

One improvement I will make this time is name each section the same as the commit name.

This will allow for an easier search in case that is required in the future.

I will also see what else can improve as I continue so that the next project can also be improved.

### Initial commit

The very first step in this project is using the Spring Initializr.

This time around we have the following attributes:
```
Project: Maven
Language: Java
Spring Boot: 4.0.1
Group: com.pawelqhd
Artifact: workout-timer
Name: WorkoutTimer
Description: Rest API for tracking gym workouts and exercises
Package name: com.pawelqhd.workout-tracker
Packaging: Jar
Configuration: Properties
Java: 21
Dependencies: Spring Web, Spring Security, Spring Data JPA, MySQL Driver, Validation, Spring Boot DevTools
```

I then save this file in my local directory and then launch IntelliJ.

Go into Settings -> Build, Execution, Deployment -> Compiler -> Build projects automatically and make sure this setting is checked.

This allows our DevTools to work properly. IDE will restart the app automatically if we make any changes to the file we are working on.

Create a package called ```controller``` and inside it create ```WorkoutsRestController.java``` This class will contain all the workout endpoints.

Here is the class so far:

```java
@RestController
@RequestMapping("/workouts")
public class WorkoutsRestController {

    @GetMapping("")
    public String getAllWorkouts(){
        return "Workout REST API currently in development";
    }
}
```

The problem is that we do not have a database. This will be added in later commits but without database connection the application crashes.

For the moment the solution is to simply create a schema we are going to use inside MySQL workbench.

We can then add this schema inside out ```application.properties``` file:

```
spring.datasource.url=jdbc:mysql://localhost:3306/workout-tracker
spring.datasource.username=root
spring.datasource.password=root
```

This provides us with very basic database connection right to our empty schema.

We can focus on database design in a future commit once we verify that this application is working first.

The next issue is that we are not able to test this application as it requires a login.

We don't have any users in our database, we don't even have a table for users.

For now, we will simply use the default login information that spring provides.

We simply use user as the username and copy the generated password from the run logs.

Once we are logged in we can access our message by going to the http://localhost:8080/workouts 

I used the browser for this step. However, with REST API it will not be possible to simply user the browser for some of the functions.

To do this we use Postman. Simply create a new GET request for the http://localhost:8080/workouts

You then need to go into Authorization tab and select the Type as Basic Auth.

You then simply put in the Username as user and then copy and paste the generated key into the Password field.

Once you click send you should see the message displayed inside the Postman.

In order to share this project with GitHub we first need to enable Git.

Got VCS -> Enable Version Control Integration

Once this is done you need to make sure you are logged in with your GitHub account then go Git -> GitHub -> Share Project on GitHub

### Database Setup and Flyway

After reviewing my previous project, there was one clear area that had I had to improve upon and that's how I handle database creation.

Up until now I used MySQL Workbench as this was something I was already familiar with.

The problem is that making changes to the database was difficult to track and implement.

To fix it this time around I wanted to research a better way on handling this.

Flyway offers simple way to version control your database.

This is something I wanted to do before and I already included my .sql files inside my project structure.

This feels like a natural evolution for this, and any future projects.

This is a new tool that I have never used before and I will trial it with this project.

To get started we need to add the following dependencies into our pom.xml file:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-flyway</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```

The above dependency is different for Spring Boot 3 and Spring Boot 4.

The only thing that stays the same is the flyway-mysql which is needed for Spring Boot 3 and 4.

Inside our ```application.properties``` file we can add the following optional line:

```
spring.flyway.enabled=true
```

Flyway is turned on by default when adding the dependency.

I'm adding this line just in case I need to quickly switch flyway off in the future.

The good news is that we have already created an empty workout-tracker schema using MySQL Workbench.

We need this in order to connect to the database and allow Flyway to start creating our database tables.

Before we start writing our first .sql files, we need to update our project structure to store them.

For Flyway to work correctly, we need to create them under ```src/main/resources/db/migration/```

With this file structure, Flyway knows where to look for our .sql files as this is the default.

We are now ready to start creating our .sql file. However, there are few requirements for the naming.

Each file that Flyway uses needs to have the following:

```
Start with capital V followed by a number. This can be any number like 1, 2, 1.1, 4.5. For example V1.1
After this prefix you need two underscores. For example V1.1__
Next is the description of the file. For example V1.1__create-users-table
Last is the .sql extension. For example V1.1__create-users-table.sql
```

The above gives you the basic naming convention for the file names that Flyway can understand and read.

If this is not followed you might get an error or the files could be skipped completely.

I created the following database files:

```V1.0__create_users_table.sql```
```sql
CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    email VARCHAR(100) UNIQUE,
    enabled TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
```

```V1.1__create_exercises_table.sql```
```sql
CREATE TABLE exercises (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    PRIMARY KEY(id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
```

```V1.2__create_workouts_table.sql```
```sql
CREATE TABLE workouts (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    notes TEXT,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
```

```V1.3__create_workout_exercises_join_table.sql```
```sql
CREATE TABLE workout_exercises (
    id INT NOT NULL AUTO_INCREMENT,
    workout_id INT NOT NULL,
    exercise_id INT NOT NULL,
    sets INT,
    reps INT,
    weight DECIMAL (7,3),
    exercise_order INT,
    PRIMARY KEY (id),
    UNIQUE KEY unique_workout_exercise (workout_id, exercise_id),
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE,
    FOREIGN KEY (exercise_id) REFERENCES exercises(id)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8mb4;
```

At the moment this is a good starting point for our database.

I can always expand it in the future as I develop this API. Flyway will make it easier to do so.

