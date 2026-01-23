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