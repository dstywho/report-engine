
```xml
<project>
    ...
    ...
    <repositories>
        ...
        <repository>
            <id>httpz</id>
            <url>https://raw.github.com/dstywho/report-engine/mvn-repo/snapshots</url>
        </repository> 
        ...
        ...

```

```xml

<dependency>
    <groupId>com.report.engine</groupId>¬
    <artifactId>report-engine-server</artifactId>¬
    <version>0.0.1-SNAPSHOT</version>¬    
    <packaging>war</packaging>
</dependency>

<dependency>
    <groupId>com.report.engine</groupId>
    <artifactId>report-engine-client</artifactId>
    <version>0.0.4</version>
</dependency>
```

created using:
mvn -DaltDeploymentRepository=snapshot-repo::default::file:$PWD/snapshots clean deploy -DskipTests
mvn -f otherpom -DaltDeploymentRepository=snapshot-repo::default::file:$PWD/snapshots clean deploy -DskipTests

