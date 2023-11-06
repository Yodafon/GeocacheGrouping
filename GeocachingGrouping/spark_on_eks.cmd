kubectl create clusterrolebinding cluster-system-anonymous --clusterrole=cluster-admin --user=system:anonymous
kubectl create clusterrolebinding spark-pod --clusterrole=admin --serviceaccount=default:default
%SPARK_HOME%\bin\spark-submit ^
--master k8s://https://DF32521E946F6BDB41FAAAC7AE602529.gr7.eu-central-1.eks.amazonaws.com ^
--deploy-mode cluster ^
--name GeocachingGrouping ^
--class com.gg.core.AppStart ^
--conf spark.dynamicAllocation.initialExecutors=2 ^
--conf spark.dynamicAllocation.maxExecutors=5 ^
--conf spark.kubernetes.executor.request.cores=300m ^
--conf spark.executor.memoryOverhead=0m ^
--conf spark.executor.memory=800m ^
--conf spark.dynamicAllocation.enabled=true ^
--conf spark.dynamicAllocation.shuffleTracking.enabled=true ^
--conf spark.kubernetes.driver.node.selector.name=spark_driver ^
--conf spark.kubernetes.executor.node.selector.name=spark_executor ^
--conf spark.kubernetes.driver.pod.name=geocachegrouping-driver ^
--conf spark.executor.extraJavaOptions="-XX:+PrintGCDetails -XX:+PrintGCTimeStamps" ^
--conf spark.kubernetes.container.image=281741967817.dkr.ecr.eu-central-1.amazonaws.com/geocachinggroupingrepository:spark ^
--conf spark.kubernetes.container.image.pullSecrets=aws-ecr-cred ^
--conf spark.kubernetes.driver.ownPersistentVolumeClaim=true ^
--conf spark.kubernetes.driver.reusePersistentVolumeClaim=true ^
--conf spark.kubernetes.driver.volumes.persistentVolumeClaim.data.options.claimName=efs-pvc ^
--conf spark.kubernetes.driver.volumes.persistentVolumeClaim.data.mount.path=/GeocacheGroupingApp ^
--conf spark.kubernetes.driver.volumes.persistentVolumeClaim.data.mount.readOnly=false ^
--conf spark.kubernetes.executor.volumes.persistentVolumeClaim.data.options.claimName=efs-pvc ^
--conf spark.kubernetes.executor.volumes.persistentVolumeClaim.data.mount.path=/GeocacheGroupingApp ^
--conf spark.kubernetes.executor.volumes.persistentVolumeClaim.data.mount.readOnly=false ^
--conf spark.kubernetes.file.upload.path=s3a://geocaching-s3-bucket ^
--conf spark.hadoop.fs.s3a.access.key=%AWS_ACCESS_KEY_ID% ^
--conf spark.hadoop.fs.s3a.impl=org.apache.hadoop.fs.s3a.S3AFileSystem ^
--conf spark.hadoop.fs.s3a.fast.upload=true ^
--conf spark.hadoop.fs.s3a.secret.key=%AWS_SECRET_ACCESS_KEY% ^
--verbose ^
build\libs\GeocachingGrouping.jar