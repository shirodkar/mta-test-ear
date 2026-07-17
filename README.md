export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home

mvn clean install -f acme-stubs/pom.xml

mvn dependency:tree > mvn-dependency-tree.txt