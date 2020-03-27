# ApolloCore

A Bukkit plugin framework by Brett Bender

This library aims to make writing a plugin quicker and easier.

## Installation


### Maven
```xml
<repository>
    <id>brettb-repo</id>
    <url>https://repo.brettb.xyz</url>
</repository>
```

```xml
<dependency>
    <groupId>xyz.brettb</groupId>
    <artifactId>ac</artifactId>
    <version>1.2.0</version>
    <scope>provided</scope>
</dependency>
```

### Gradle
```gradle
repositories {
    maven {
        name = 'brettb-repo'
        url = 'https://repo.brettb.xyz'
    }
}

dependencies {
    compileOnly group: 'xyz.brettb', name: 'ac', version: '1.2.0'
}
```

After that, just make sure to include a built ApolloCore in your `plugins` folder.

## Usage

Documentation is W.I.P.