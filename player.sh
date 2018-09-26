

echo "#######################################"
echo "Player will be start in 3 seconds"
echo "params port:$1 name:$2 numberOfMessage:$3 messageFrequency:$4 message:$5"
#-------------------------------
sleep 3
dir=$(pwd)
echo "current path:$dir"
dir="$dir/Player/target"
cd $dir
echo "current path:$dir"
echo "->Player is running.."
sleep 2
java -jar Player-1.0-SNAPSHOT-jar-with-dependencies.jar $1 $2 $3 $4 $5