/usr/bin/env sh

mkdir plugins | cp ./target/Demigods-3.0.0-DEVELOPMENT.jar ./plugins/Demigods.jar | wget http://ci.md-5.net/job/Spigot/lastSuccessfulBuild/artifact/Spigot-Server/target/spigot.jar | java -Xmx100M -Xms100M -jar spigot.jar