top_dir=$(cd $(dirname $(dirname $0)) && pwd)
echo $top_dir
cd $top_dir
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044 -cp target/sawtooth-idempotent-tp-0.0.1-SNAPSHOT.jar com.mycompany.blockchain.sawtooth.IdempotentProcessor $*
