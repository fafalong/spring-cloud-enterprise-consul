#!/bin/sh
logf() {
  TP="+%H:%M:%S.%-3N"
  TS=`date $TP|sed 's/ /0/g'`
    TYPE="DBG"
    [ $# -gt 1 ] && \
        TYPE="$1" && shift
    echo "$TS:$TYPE:$1" | tee -a /tmp/start_up_envs.log
}



logf "auth server:$AUTH_SERVER\n\n" 

logf "zipkin server:$ZIPKIN_SERVER, consul server:$CONSUL_SERVER \n\n" 

logf "Start to run application..."
java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar
