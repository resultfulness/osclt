# maze solving displaying editing

## building

```bash
mvn compile
```

alternatively, to package into a jar:
```bash
mvn package
```

## running

use `_JAVA_OPTIONS='-Dawt.useSystemAAFontSettings=on -Dswing.aatext=true'`
for nicer font

from class files:
```bash
_JAVA_OPTIONS='-Dawt.useSystemAAFontSettings=on -Dswing.aatext=true' java -cp target/classes lmp2.oscillate.App
```

from a jar file
```bash
_JAVA_OPTIONS='-Dawt.useSystemAAFontSettings=on -Dswing.aatext=true' java -jar target/oscillate-VERSION.jar
```
