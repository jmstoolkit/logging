mvn deploy:deploy-file \
  -DgroupId=org.jfree \
  -DartifactId=orson \
  -Dversion=0.6.0 \
  -Durl=file:./orson/ \
  -DrepositoryId=orson \
  -DupdateReleaseInfo=true \
  -Dfile=orson-0.6.0.jar
