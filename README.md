# Spring Boot Hazelcast Cache

It is a **Spring Boot application** that demonstrates in‑memory caching with Hazelcast to speed up reads, reduce database load, and keep responses consistent after updates/deletes.

The example uses a simple User domain with CRUD APIs and applies caching at the service layer using Spring Cache annotations.

* **@Cacheable**
* **@CachePut**
* **@CacheEvict**

## 📁 Project Structure

```text
spring-boot-hazelcast-cache
├─ src/main/java/com/example/hazelcast
│      ├── SpringBootHazelcastCacheApplication.java
│      └── controller
│      |     └── UserController.java
|      └── entity
│      |     └── User.java
|      └── repository
│      |     └── UserRepository.java
|      └── service
│      |     └── UserService.java
├─ src/main/resources
|      └── application.yaml
|      └── hazelcast.yaml
├─ src/test/java/com/example/hazelcast
│      └── SpringBootHazelcastCacheApplicationTests.java
│── build.gradle
│── settings.gradle
└── gradle/wrapper
└── README.md
```

## 🛠️ Technologies Used

| Layer              | Technology              | 
| :----------------- | -----------------------:| 
| Language           | Java 21                 | 
| Backend Framework  | Spring Boot 4.0.3       |
| Caching            | Spring Cache, Hazelcast |
| Build Tool         | Gradle                  | 
| Database           | H2 (in-memory)          |
| ORM                | Spring Data JPA         |
| Tests              | JUnit 5                 |

## Explanation

To use Hazelcast cache we must need below dependency.
```gradle
implementation 'com.hazelcast:hazelcast'
```
Then, we need to add Hazelcast configuration using one of the below options:
* Add hazelcast.yaml configuration **OR**
* Add hazelcast.xml configuration **OR**
* Define **@Bean** with Hazelcast configuration in the source code.

Create **hazelcast.yaml** file under **src/main/resources** directory.
```yaml
hazelcast:
  network:
    join:
      multicast:
        enabled: true
```

Add below dependencies in **build.gradle** file.
```gradle
implementation 'org.springframework.boot:spring-boot-starter-cache'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-webmvc'
implementation 'com.hazelcast:hazelcast'
implementation 'com.h2database:h2'
```
Add below configuration in the **application.yaml** file.
  ```yaml
  server:
    port: 8080
  spring:
    application:
      name: spring-boot-hazelcast-cache 
  database:
    datasource:
      url: jdbc:h2:mem:userdb;MODE=LEGACY;DB_CLOSE_DELAY=-1
      driverClassName: org.h2.Driver
      username: sa
      password: 
  ```

Add **@EnableCaching** annotation on class level.
```java
package com.example.hazelcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootHazelcastCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootHazelcastCacheApplication.class, args);
	}

}
```

## 🧩 Caching Patterns Used
* **Cache‑Aside** (Read‑Through) for entity lookups (@Cacheable on getUserById).
* **Cache Eviction** on mutations (@CacheEvict for delete; @CachePut for create/update).

## 🔍 How Caching Works Here
* **Reads**: @Cacheable("users") caches a user by ID after the first DB hit.
* **Updates**: @CachePut("users") writes the updated User back to cache so subsequent reads are immediately fresh.
* **Deletes**: @CacheEvict("users") removes the key from cache after deletion to prevent stale reads.
* **Key**: We use key = "#id" to cache by user ID.
* **Scope**: In a single app instance, it’s in‑process. If you scale horizontally, Hazelcast shares this map across nodes (distributed cache).

## 🧪 Verifying Cache Behavior (Manual)
1. Start the app.
2. Create one user (POST /users with request body).
3. Call GET /users/{id} twice—observe:
   * First call → slower (DB hit) (Add some log statements and verify)
   * Second call → faster (cache hit) (Add some log statements and verify)
4. Update the same user—then immediately call GET and see updated values (thanks to @CachePut).
5. Delete the user — GET should return 404 (and cache is evicted).
