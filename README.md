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

