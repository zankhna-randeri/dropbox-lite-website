echo -n "Starting starting webapp "
java8 -jar -Dspring.profiles.active=prod /home/ec2-user/target/website-1.0-SNAPSHOT.jar >> /home/ec2-user/webserver.log 2>&1  &
RETVAL=$?
PID=$!
[ $RETVAL -eq 0 ] && echo $PID > /home/ec2-user/website.pid