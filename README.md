## Project description

This project serves as API for an exercise tracker.

When going to the gym, you will be able to record the weight, reps and sets for every exercise.

The focus will be mostly on weightlifting with support for cardio added later down the line.

Users will be able to log in or register.

Once the user is logged in they will be able to create new workouts as well as see their workout history.

## About this document

This document is here to serve as a development journal.

I will include any changes and commits as well as description on how each commit was achieved.

The goal for this project and for this document is to server as a learning guide.

The primary focus for me is to learn as much as possible as well as to make sure this knowledge isn't lost over time.

That's why this document serves both as a development journal as well as future reference I can go back and review.

Since this is my second project with Spring Boot and the second document of this type, I will try and improve any shortfalls.

One improvement I will make this time is name each section the same as the commit name.

This will allow for an easier search in case that is required in the future.

I will also see what else can improve as I continue so that the next project can also be improved.

## Commit 1: Initial commit

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

## Commit 2: Database Setup and Flyway

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

## Commit 3: User, Workout, Exercise and WorkoutExercise entities

This project is all about improving on the previous one. For this reason I want to have more meaningful commit names.

Specifically, I will skip the word added as I was using it all the time before.

For this next part I will simply add entities for my database setup.

The most notable class is the ```User.java```

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters long")
    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @JsonIgnore
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name="enabled")
    private boolean enabled = true;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Workout> workouts = new ArrayList<>();
    
    // constructors
    
    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }
    
    // getters, setters and toString
}
```

The above code is very similar to my previous projects, with few notable changes.

First we use ```@NotBlank``` annotation as it combines both ```@NotNull``` and ```@NotEmpty``` together.

It also checks that the trimmed length is greater than zero. This is fantastic as it means we write less code to validate it later.

Another important annotation is the ```@Email``` which validates for the right email format.

Once again, this helps us greatly in the future.

I also removed ```unique = true``` from the password as this made no sense. You could have users with the same password.

Most importantly, I added ```@JsonIgnore``` for the password. This prevents password exposure when returning a JSON. A critical change.

Another small improvement for the password is not including it inside the toString() method. This increases security.

My favorite addition is the ```onCreate()``` helper method.

This method simply records the time at which the entity got created. This is great as I no longer have to do this inside the Service method.

We also add ```@OneToMany``` since we want to keep track of the workouts that the users have.

I know the database design in theory. However, it is still something I need more practice with.

Setting up Entities so that they match my database is my weakest point at the moment. This is why I took extra time to refine my understanding of it.

The following section has some pointers to make it easier in the future.

#### DATABASE NOTES

In Spring Boot you have a small section at the end of your fields that tells spring how your tables relate to one another.

The main annotations for this are ```@JoinColumn``` as well as, for example, ```@OneToMany```.

You need to figure out which one of the tables is the owning side. Usually the table with the foreign key(s) is the owning side.

You need to put the ```@JoinColumn``` on the owning side. Inside you need to put the foreign key ```@JoinColumn(name = "workout_id"```

Note that the foreign key you need to input is the one from the database.

```@JoinColumn``` often corresponds to a Java field, such as ```private Workout workout```

Note that ```@JoinColumn``` is not enough on its own, and you also need to specify this Java field with a relational annotation such as ```@ManyToOne``` or ```@OneToOne```

Another thing to note is that the foreign keys should not be set as Java fields since ```@JoinColumn``` gives Spring enough information.

On the other side of the relationship (inverse side) you also need to tell Spring Boot how this relationship works.

This time you only need, for example ```@OneToMany``` annotation.

However, inside it, you need to write the following ```@OneToMany(mappedBy = "workout")```

```workout``` represents Java field from the owning side. If you call it something else you need to update mappedBy accordingly.

The field in our example would look something like this ```private List<WorkoutExercise> workoutExercises;```

Note that mappedBy can only be used for ```@OneToMany``` or ```@ManyToMany```

| Annotation  | Which Side  | What It Uses                      | Java field you apply it to              |
|-------------|-------------|-----------------------------------|-----------------------------------------|
| @ManyToOne  | Owning      | @JoinColumn(name = "Foreign key") | ```private Workout workout```           |
| @ManyToMany | Owning      | @JoinTable(...)                   | ```private List<Role> roles;```         |
| @ManyToMany | Inverse     | mappedBy = "Java field name"      | ```private List<User> users;```         |
| @OneToOne   | Inverse     | mappedBy = "Java field name"      | ```private User user```                 |
| @OneToOne   | Owning      | @JoinColumn(name = "Foreign key") | ```private UserProfile profile;```      |
| @OneToMany  | Inverse     | mappedBy = "Java field name"      | ```private List<Exercises> exercises``` |

This brings us to our real world example and our entity setup.

```java
@Entity
@Table(name = "workout_exercises")
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "sets")
    private int sets;

    @Column(name = "reps")
    private int reps;

    @Column(name = "weight")
    private double weight;

    @Column(name = "exercise_order")
    private int exerciseOrder;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;
    
    // constructors, getters, setters and toString
}
```

We start with WorkoutExercise join table class as it's the easiest to explain.

We have two tables and one join table. This in theory is two separate relationships.

One between ```WorkoutExercise``` join table and ```Exercise``` and another between ```WorkoutExercise``` and ```Workout```

I took me a while to realise this, and it's why I took my time writing the notes above.

We also had to create this join table as an entity because of its extra fields such as weight, sets and reps.

Usually join tables do not need to be included if they are just joining two tables together.

Both workout and exercise have @ManyToOne relationship because we can have many workouts and exercises for one row of the join table.

For a deeper understanding, I wanted a table that is flexible and can have one exercise linked to one workout that includes sets, reps and weight.

This will allow us to change the exercises during our workout which is a very important feature. 

```java
@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "exercise")
    private List<WorkoutExercise> workoutExercises;
    
    // constructors, getters, setters and toString
}
```

Exercises entity is very simple. Here is where we use the mappedBy and the ```exercise``` field from out WorkoutExercise class.

```java
@Entity
@Table(name = "workouts")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<WorkoutExercise> workoutExercises;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // constructors, getters, setters and toString
}
```

The Workout class made me realise I forgot something crucial. I forgot about the User class and I had to go back and update this guide.

We have ```@ManyToOne``` for the user since you can have many workouts linking to one user.

This is opposite what we have in the User class as we want one user linking to many workouts, hence @OneToMany.

Best way to remember which one to use is by following this simple guide I made for myself. I will use Workout class as an example.

```@ManyToOne``` annotation has two sides to it, the left side ```Many``` and the right side ```One```

Imagine that you remove all the other fields and keep just the ```private User user``` you then remove all the spaces and formating.

This would leave you with one long line of code.

```Many``` is closer to the ```public class Workout``` while ```One``` is closer to the ```private User user```

The reason why Workout class is the owning side goes back to our table above.

```@ManyToOne``` only has the Owning side and doesn't have the inverse side.

Similarly, in our User class ```@OneToMany``` can only be the inverse side.

This also checks out as Workout class does not have foreign key that would link back to user.

### Commit 4: JpaRepository, refactor to Long IDs and toString() clean up

This is most likely the easiest commit for this project. However, I still wanted to delve deeper and learn more than before.

```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```

```java
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
```

```java
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
}
```

```java
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
}
```

The above interfaces extend JpaRepository which providees us with common CRUD operations that we will need for this project.

I was taught to not put any annotations inside interfaces and only use them for implementation classes.

For JpaRepository you don't create implementation class, this is something Spring Boot creates for you.

However, you can include @Repository annotation for JpaRepository interfaces.

From what I have seen, there are many reason to use it or not to. I will stick with what I was taught for now and not include it.

Another thing I realised at this point is that I always used Integer for my primary keys and this is fine for small projects.

For real world applications, I should be using Long. For this reason I decided to refactor my code and my database.

Luckily we are using Flyway and the process for this is easy. I simply create this file:

```V2.0__id_refactor_to_bigint.sql```

```sql
ALTER TABLE users MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE exercises MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE workouts MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE workouts MODIFY COLUMN user_id BIGINT NOT NULL;

ALTER TABLE workout_exercises MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE workout_exercises MODIFY COLUMN workout_id BIGINT NOT NULL;
ALTER TABLE workout_exercises MODIFY COLUMN exercise_id BIGINT NOT NULL;
```

Along with database changes we need to change our entity classes to Long wrapper class and not the long primitive.

The reason is that the long primitive has the default value as 0 while Long wrapper class has it as null.

If our id is null we know something, and we will most likely get null pointer exception.

Another thing we need to change is all the getters and setters for the id fields.

While I was here I also cleaned up some toString() methods as you should not have any relational data in them.

### Commit 5: Fixing Flyway error by dropping foreign keys

Big mistake I made in the last commit was not running the application before commiting changes.

Lesson for next time is to always make sure I run the code before I make the commit.

To fix this we need to drop the foreign keys first where appropriate, change the tables and then re-assign the foreign keys:

```sql
-- Drop Foreign Keys first
ALTER TABLE workouts DROP FOREIGN KEY workouts_ibfk_1;
ALTER TABLE workout_exercises DROP FOREIGN KEY workout_exercises_ibfk_1;
ALTER TABLE workout_exercises DROP FOREIGN KEY workout_exercises_ibfk_2;

-- Change to BIGINT
ALTER TABLE users MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE exercises MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE workouts MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE workouts MODIFY COLUMN user_id BIGINT NOT NULL;

ALTER TABLE workout_exercises MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE workout_exercises MODIFY COLUMN workout_id BIGINT NOT NULL;
ALTER TABLE workout_exercises MODIFY COLUMN exercise_id BIGINT NOT NULL;

-- Add the Foreign keys again
ALTER TABLE workouts ADD CONSTRAINT workouts_ibfk_1 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE workout_exercises ADD CONSTRAINT workout_exercises_ibfk_1 FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE;
ALTER TABLE workout_exercises ADD CONSTRAINT workout_exercises_ibfk_2 FOREIGN KEY (exercise_id) REFERENCES exercises(id);
```

Simply running this after the failed attempt will not work.

I simply deleted the ```V2.0__id_refactor_to_bigint.sql``` row from the ```flyway_schema_history``` table

I did this by using MySQL Workbench. I will have to try a different approach next time, but wanted to see if doing this will work first.

I'm sure there is an easier way to make this work.

Inside MySQL Workbench I was able to easily find the ```workout_exercises_ibfk_1``` and ```workout_exercises_ibfk_2```

Very useful way to make sure the changes worked is to use, for example ```DESCRIBE users;``` sql query.

This gives you basic information about the columns inside your database.

### Commit 6: UserService and SecurityConfig for password encryption

I decided to improve the service layer structure this time around.

In order to achieve this I created two packages, ```service``` and ```service.impl```

I decided to make a handy table for the most useful JpaRepository methods in order to help me with CRUD operations.

#### JPA Repository default methods

| Method                      | Description                                                           |
|-----------------------------|-----------------------------------------------------------------------|
| S save(S entity)            | Saves the entity unless it already exists, then it updates it instead |
| Optional<T> findById(ID id) | Retrieves an entity by its id.                                        |
| void delete(T entity)       | Deletes a given entity                                                |
| void deleteById(ID id)      | Deletes the entity with given id                                      |
| long count()                | Returns the number of entities available                              |
| Iterable<T> findAll()       | Returns all instances of the type                                     |
| boolean exists(S example)   | Checks if the given data exists                                       |

The above table will be very useful for creating future service classes.

```java
public interface UserService {

    User findById(Long id);

    User create(User user);

    void deleteById(Long id);

    List<User> getAll();

    User update(Long id, User user);
}
```

The above interface is our first service class.

I wanted to create the basic methods we will most likely use in the future.

If we need anything else we can simply add it without any issues.

```java
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find User with the id: " + id));
    }

    @Override
    public User create(User user) {

        User newUser = new User();

        newUser.setUserName(user.getUserName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());

        return userRepository.save(newUser);
    }

    @Override
    public void deleteById(Long id) {

        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(Long id, User user) {

        User existingUser = this.findById(id);

        existingUser.setUserName(user.getUserName());
        existingUser.setEmail(user.getEmail());

        return userRepository.save(existingUser);
    }
}
```

The above code is the implementation of the interface.

The ```findById()``` method allowed me to use ```.orElseThrow()``` method.

This makes the code simpler and has the added benefit of returning User object instead of ```Optional<User>```

I will definitely use it more when dealing with Optional.

Originally I just had the ```save()``` method, but decided to split it into ```create()``` and ```update()```

The reason for this is because of password encryption. I only want to encrypt the password when create new user.

We might also add a method to update password in the future, but we are not focusing on security just yet, the focus for now is simple CRUD.

```deleteById()``` and ```getAll()``` are self-explanatory and use the default JpaRepository methods.

If the need arises I will create more complex logic.

I learned my lesson from previous commit and when I ran the app, Spring could not find ```BCryptPasswordEncoder``` Bean.

For this reason I created this very simple class:

```java
@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
```

It's just to satisfy this dependency. I will create more detailed security implementation once the app has some basic functions.

I also want to make my commits a little bit shorter. I realised it takes me too long between every commit.

### Commit 7: ExerciseService

I started this commit by organising my methods and picking a format to stick with.

I moved the update method inside UserService higher up so that it is below the create method.

It made no sense for it being all the way at the bottom.

```java
public class ExerciseServiceImpl implements ExerciseService {
    
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }


    @Override
    public Exercise findById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find Exercise with the id: " + id));
    }

    @Override
    public Exercise create(Exercise exercise) {
        
        Exercise newExercise = new Exercise();
        
        newExercise.setName(exercise.getName());
        
        return exerciseRepository.save(newExercise);
    }

    @Override
    public Exercise update(Long id, Exercise exercise) {
        
        Exercise existingExercise = this.findById(id);
        
        existingExercise.setName(exercise.getName());
        
        return exerciseRepository.save(existingExercise);
    }

    @Override
    public void deleteById(Long id) {
        
        exerciseRepository.deleteById(id);
    }

    @Override
    public List<Exercise> getAll() {
        
        return exerciseRepository.findAll();
    }
}
```

The above code, as well as the interface, is very similar to our ```UserServiceImpl```

These are just standard CRUD methods to get us started with this project.

### Commit 8: WorkoutService

```java
public class WorkoutServiceImpl implements WorkoutService {
    
    private final WorkoutRepository workoutRepository;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Override
    public Workout findById(Long id) {
        
        return workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find Workout with the id: " + id));
    }

    @Override
    public Workout create(Workout workout) {
        
        Workout newWorkout = new Workout();
        
        newWorkout.setName(workout.getName());
        newWorkout.setNotes(workout.getNotes());
        newWorkout.setUser(workout.getUser());
        newWorkout.setWorkoutExercises(workout.getWorkoutExercises());
        
        return workoutRepository.save(newWorkout);
    }

    @Override
    public Workout update(Long id, Workout workout) {
        
        Workout exisitngWorkout = this.findById(id);
        
        exisitngWorkout.setName(workout.getName());
        exisitngWorkout.setNotes(workout.getNotes());
        exisitngWorkout.setWorkoutExercises(workout.getWorkoutExercises());
        
        return workoutRepository.save(exisitngWorkout);
    }

    @Override
    public void deleteById(Long id) {
        
        workoutRepository.deleteById(id);

    }

    @Override
    public List<Workout> getAll() {
        
        return workoutRepository.findAll();
    }
}
```

The only consideration for the above code is how we will set the user.

Since the user will already be logged in, we can simply grab the user from the session.

However, I'm not sure what's the best way to do this and I will have to think about it when implementing it.

For now this code will be sufficient.

### Commit 9: WorkoutExerciseService and copyEntity() helper method

When working on this commit, I realised there was a lot of repetition with copying and pasting entities from one to the other.

The solution is the helper method inside each of the entities. Here is the WorkoutExerciseServiceImpl class:

```java
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final WorkoutExerciseRepository workoutExerciseRepository;

    public WorkoutExerciseServiceImpl(WorkoutExerciseRepository workoutExerciseRepository) {
        this.workoutExerciseRepository = workoutExerciseRepository;
    }

    @Override
    public WorkoutExercise findById(Long id) {

        return workoutExerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find WorkoutExercise with id: " + id));
    }

    @Override
    public WorkoutExercise create(WorkoutExercise workoutExercise) {

        WorkoutExercise newWorkoutExercise = new WorkoutExercise();
        newWorkoutExercise.copyEntity(workoutExercise);
        return workoutExerciseRepository.save(newWorkoutExercise);
    }

    @Override
    public WorkoutExercise update(Long id, WorkoutExercise workoutExercise) {

        WorkoutExercise existingWorkoutExercise = this.findById(id);
        existingWorkoutExercise.copyEntity(workoutExercise);
        return workoutExerciseRepository.save(existingWorkoutExercise);
    }

    @Override
    public void deleteById(Long id) {

        workoutExerciseRepository.deleteById(id);
    }

    @Override
    public List<WorkoutExercise> getAll() {

        return workoutExerciseRepository.findAll();
    }
}
```

In the above code we no longer write lines such as ```exisitngWorkoutExercises.setWeight(workoutExercise.getWeight());```

Instead, we are using a helper method from our ```WorkoutExercise``` class:

```java
    public void copyEntity(WorkoutExercise copy){

        this.setReps(copy.getReps());
        this.setSets(copy.getSets());
        this.setWeight(copy.getWeight());
        this.setExerciseOrder(copy.getExerciseOrder());
        this.setExercise(copy.getExercise());
        this.setWorkout(copy.getWorkout());
    }
```

This makes the code a lot cleaner and easier to add more fields in the future.

I implemented this method for all of our entities with few important caveats.

```java
    public void copyEntity(Workout workout){

        this.setName(workout.getName());
        this.setNotes(workout.getNotes());
        this.setWorkoutExercises(workout.getWorkoutExercises());
    }
```

The above code is inside ```Workout.java``` class

For the workout, we don't include the user. This is because I'm still unsure about the implementation.

I will most likely grab the user from the session for this assignment to make it easier and more reliable.

```java
    @Override
    public Workout create(Workout workout) {

        Workout newWorkout = new Workout();
        newWorkout.copyEntity(workout);
        newWorkout.setUser(workout.getUser());
        return workoutRepository.save(newWorkout);
    }

    @Override
    public Workout update(Long id, Workout workout) {

        Workout exisitngWorkout = this.findById(id);
        exisitngWorkout.copyEntity(workout);
        return workoutRepository.save(exisitngWorkout);
    }
```

The above code is inside ```WorkoutServiceImpl.java``` class

We simply add the user for the ```create()``` method separate. This will allow us to add a custom implementation of this later.

For the ```update()``` we don't need to assign the user so we simply skip it.

```java
    public void copyEntity(Exercise exercise){

        this.setName(exercise.getName());
    }
```

The above code is inside ```Exercise.java``` class

Even tho this class has a single field for now, we still include it so that it's consistent and easier to add more fields later.

```java
    public void copyEntity(User user){

        this.setUserName(user.getUserName());
        this.setEmail(user.getEmail());
    }
```

The above method is inside User entity.

We skip the password for this one as it's encrypted, and we don't want to be copying.

We also don't want to modify it as we will do that in a future update.

### Commit 10: Learning and implementing User and Exercise DTOs

I didn't learn about DTOs and have no idea how they work. I came about them while doing research for my app.

The very first step is to learn what they are and how they will help my app. I want to have a solid understanding first.

The best analogy I can come up with is how classes work in RPG. You can have a main class such as Mage.

Mage can specialise into two subclasses such as Fire Mage and Healer. They both specialise in different aspects.

You need both damage and healing to win an encounter effectively and having two base class Mages would not be enough.

The same goes to DTOs. You might have specific functions that you need that you might want to perform.

For example, when sending response with the User class, you might not want to include password for security reason.

You can instead make UserResponseDTO that only has id, Username, Email fields.

The same goes when creating a user. You don't have id field as this is something that database generates for us.

We can create UserCreateDTO that has Username, Password and Email.

Both UserResponseDTO and UserCreateDTO are important to create a user. One to set up data and one to confirm it to the user.

DTOs are POJO classes and don't have any annotations and are not linked to database like the base Entity classes do.

```java
public class UserCreateDTO {

    private String userName;
    private String password;
    private String email;

    public UserCreateDTO() {
    }

    public UserCreateDTO(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

The above code is the ```UserCreateDTO``` including all the constructors, getters and setters.

This is the theme when it comes to DTO and most of them will look like this.

```java
public class UserResponseDTO {

    private Long id;
    private String userName;
    private String email;
    
    // Constructors, getters and setters
}
```

```java
public class ExerciseCreateDTO {

    private String name;
    
    // Constructors, getters and setters
}
```

```java
public class ExerciseResponseDTO {

    private Long id;
    private String name;
    
    // Constructors, getters and setters
}
```

I followed the same structure for the Exercise DTOs. Exercise entity is very simple for now but I included DTOs for consistency.

### Commit 11: Workout and WorkoutExercise DTOs

This commit is very similar to the previous one, finishing the remaining DTOs.

```java
public class WorkoutCreateDTO {

    private String name;
    private String notes;

    // Constructors, getters and setters
}
```

```java
public class WorkoutResponseDTO {

    private Long id;
    private String name;
    private String notes;
    private List<WorkoutExerciseResponseDTO> exercises;

    // Constructors, getters and setters
}
```

The ```WorkoutCreateDTO``` is very simple and similar to our Exercise DTOs.

The change from our standard DTO design is with ```WorkoutResponseDTO```

The key to DTOs is not to expose raw data that might include information that is not necessary for the front end.

However, we still want to know what kind of exercises are inside every workout.

For this reason we create a List of ```WorkoutExerciseResponseDTO``` this way the font end can see this list without exposing the entity.

```java
public class WorkoutExerciseCreateDTO {

    private Long workoutId;
    private Long exerciseId;
    private int sets;
    private int reps;
    private double weight;
    private int exerciseOrder;

    // Constructors, getters and setters
}
```

The above code also includes ids for workout and exercise in the Long format.

This is once again to prevent front end from seeing unnecessary information.

```java
public class WorkoutExerciseResponseDTO {

    private Long id;
    private Long workoutId;
    private ExerciseResponseDTO exercise;
    private int sets;
    private int reps;
    private double weight;
    private int exerciseOrder;

    // Constructors, getters and setters
}
```

The above code includes ```ExerciseResponseDTO``` but it doesn't include ```WorkoutResponseDTO```

This is because we want to avoid circular dependency and this is what I have concluded from my research.

We will most likely already know which workout we are in when queuing this data so adding it here is redundant.

I'm not very confident about DTOs and I will find out if I have implemented them correctly once I start writing my controller classes.

For now this is a good start with DTOs that gives me a solid foundation to improve upon.

