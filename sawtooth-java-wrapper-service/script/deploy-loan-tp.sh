top_dir=$(cd $(dirname $(dirname $0)) && pwd)
echo $top_dir
cd $top_dir
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1047 -cp target/sawtooth-java-wrapper-service-0.0.1-SNAPSHOT.jar com.mycompany.blockchain.sawtooth.core.service.loan.LoanProcessor tcp://localhost:4004
