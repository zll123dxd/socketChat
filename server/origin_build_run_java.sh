#!/bin/sh
#create build dir/file
mkdir -p bin/project/
find src/com/home/server/ -name *.java > source.txt

#build  : 
#import more jar file  >  javac -cp lib/mysql-connector-j-8.0.31.jar:lib/xxx.jar @source.txt -d bin/project/
javac -cp lib/mysql-connector-j-8.0.31.jar @source.txt -d bin/project/
java -cp  bin/project/ com.home.server.ChatServer

#clean
rm -rf bin/
rm source.txt
