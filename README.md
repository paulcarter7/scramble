# Scramble

A word puzzle solver that finds all valid words on a letter grid (similar to Boggle).

## Requirements

- Java 21+
- Gradle 9.x (wrapper included)

## Building and Testing

```bash
# Run tests
./gradlew test

# Build the project
./gradlew build
```

## Usage

```bash
java -cp build/classes/java/main com.pac.scramble.Scramble <16-character-board>
```

Example:
```bash
java -cp build/classes/java/main com.pac.scramble.Scramble lenonsimstasergv
```

Use `!` for letter multipliers and `*` for word multipliers (e.g., `3!e` for 3x letter multiplier on 'e').

## Project Structure

- `src/com/pac/scramble/` - Main scramble solver classes
- `src/com/pac/trie/` - Trie data structure for efficient word lookup
- `src/test/java/` - JUnit 5 tests
- `resources/words.txt` - Dictionary file

## Recent Modernizations

- Migrated from Ant to Gradle build system
- Migrated tests from custom framework to JUnit 5
- Added input validation for Trie (lowercase letters only)
- Modernized file handling with try-with-resources
- Improved Trie traversal performance with StringBuilder

---

*This project was dusted off and modernized through vibe coding with
[Claude Code](https://claude.ai/claude-code) - because refactoring legacy Java
on a lazy afternoon is way more fun with an AI pair programmer who never
judges your decade-old commit messages.*
