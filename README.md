# Publish
- mkdir -p build/repos
- git clone -b releases git@github.com:aldsistemas/eztest-data-hibernate5.git build/repos/releases
- ./gradlew -i publish
- cd build/repos/releases
- git add *
- git commit -m "[release] Commit release ${V}"
- git push origin