
#!/usr/bin/env bash
echo "#######################################"
echo "PlayerGroundService and Player will be built in 3 seconds."
globalDir=$(pwd)
echo "current dir:$globalDir"
#-------------------------------
sleep 3
dir=$(pwd)

dir="$dir/Player/"
echo "->path: $dir"

cd $dir
echo "->Player is building.."
mvn clean install -q 
#-------------------------------
sleep 3

cd $globalDir
dir=$(pwd)
dir="$dir/PlayGroundService/"
echo "->path: $dir"

cd $dir
echo "->PlayGroundService is building.."
mvn clean install -q 
echo "finished.."
#-------------------------------
