
#!/usr/bin/env bash
echo "#######################################"
echo "PlayGroundService will be start in 3 seconds"

#-------------------------------
sleep 3
dir=$(pwd)
echo "current path:$dir"
dir="$dir/PlayGroundService/target"
cd $dir
echo "current path:$dir"
echo "->PlayGroundService is running.."
sleep 2
java -jar PlayGroundServer-1.0-SNAPSHOT-jar-with-dependencies.jar
