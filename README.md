# sawtooth-java-wrapper
The repository will contain a framework  providing helper classes and methods to be used by sawtooth clients. 

Introcuction
In traditional appliation development, API ,Framework and SDKs help a lotin abstracting the underlying complexities for a system. Sawtooth does provide an SDK , but till date (Apr'18) I havent found any soluton specific to JAVA that could ease the development by abstracting the diffeculties and tight coding rquired to work with Sawtooth . Hence , this project was developed as to provide a framework which will
1. Help Developers in Crating a Transaction Processor quiet Easily.
2. Simplify and enforce addressing mechanism used by sawtooth and to evenly shareit with client ans server side code.
3. Simplify Transaction creation and submission using batches, this feature I see very important as its very rigid to submit a transaction
4. Visualize data as entities and create components that can work on entities.
5. Simplyfing or minimizing data marshalling and unmarshalling, encoding decoding etc.
