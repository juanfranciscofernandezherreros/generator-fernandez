# clp-bck-products-q

### Version: 0.1.0-SNAPSHOT

## Generate docker image

For generate docker image, uses [jib-maven-plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin).

*Example:*

``` xml
<plugins>
  <plugin>
    <groupId>com.goolge.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <configuration>
      <container>
        <mainClass>com.bme.clp.bck.products.q.infrastructure.spring.Application</mainClass>
      </container>
    </configuration>
  </plugin>
</plugins> 
```

Run docker Build & publish:
 
``` shell
$ cd <artifact-id>-service

$ mvn jib:build -Djib.from.image=<image-from(jdk)> -Djib.from.auth.username=<username> -Djib.from.auth.password=<passord> \
-Djib.to.image=<image-to> -Djib.to.auth.username=<username> -Djib.to.auth.password=<password>
```
* `<image-from>`: pulled imagen to generate Dockerfile
* `<username>`: Docker login username
* `<password>`: Docker login password
* `<image-to>`: pushed imagen to generate
 
Run image
``` shell
$ docker pull <image-to>

$ docker run -p 8080 <image-to>
```

## Control version

Uses **npm** auto-generate version [semantic-versioning](https://semver.org/spec/v2.0.0.html)
``` jshelllanguage
 * Patch releases: 1.0 or 1.0.x or ~1.0.4
 * Minor releases: 1 or 1.x or ^1.0.4
 * Major releases: * or x
```

