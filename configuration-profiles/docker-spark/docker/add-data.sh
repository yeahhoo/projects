#!/bin/bash

#echo "uploading data to HDFS"
#hdfs dfs -put /mydata/test1.txt  hdfs://namenode:8020/
#hdfs dfs -put /mydata/test2.txt  hdfs://namenode:8020/
#echo "files uploaded"


echo "uploading data to HDFS"

function upload_file_to_hdfs {
   file=$1
   if [ -z "$2" ]
     then
       echo "No number_of_tries argument supplied, setting default value equal to 1"
       number_of_tries=1
     else
       echo "number_of_tries argument supplied"
       number_of_tries=$2
   fi

   for (( i=1; i<=$number_of_tries; i++ ))
   do
      hdfs dfs -put $file  hdfs://namenode:8020/
      if [ $? -eq 0 ]
      then
        echo "Successfully uploaded file: $file"
        return 0
      else
        echo "Could not upload file: $file, try $i / $number_of_tries"
        sleep 1
      fi
   done
   return -1
}

upload_file_to_hdfs /mydata/test1.txt 10
if [ $? -eq 0 ]
  then
    upload_file_to_hdfs /mydata/test2.txt
    echo "files uploaded successfully"
  else
    echo "failed to upload files to HDFS"
fi
