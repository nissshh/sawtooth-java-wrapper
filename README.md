# sawtooth-java-wrapper
The repository will contain a framework  providing helper classes and methods to be used by sawtooth clients. 

Introcuction
In traditional appliation development, API ,Framework and SDKs help a lotin abstracting the underlying complexities for a system. Sawtooth does provide an SDK , but till date (Apr'18) I havent found any soluton specific to JAVA that could ease the development by abstracting the diffeculties and tight coding rquired to work with Sawtooth . Hence , this project was developed as to provide a framework which will
1. Help Developers in Crating a Transaction Processor quiet Easily.
2. Simplify and enforce addressing mechanism used by sawtooth and to evenly shareit with client ans server side code.
3. Simplify Transaction creation and submission using batches, this feature I see very important as its very rigid to submit a transaction
4. Visualize data as entities and create components that can work on entities.
5. Simplyfing or minimizing data marshalling and unmarshalling, encoding decoding etc.
6. Implementation is in Java.


In thsi readme I will also include the steps requried to implement your solution. 

Project Hierarchy.
The whole project is a maven project containing below modules
1. Model - Contains all the entities (protos) that are defined in your project . 
2. Server/Services  - Transaction Family Code
3. Client - Client Event handlers and transaction submissions.
4. Client Application.- Sample client application , a spring boot application REST services used by a UI Interface.


Implementation Guidelines
1. Model CLasses - While implementing a busniess solution , its important to visualize and pull out entities that exist in the business domain. The very first step you may need to do is to find all entities. The solution would then provide Transaction Processors to work on the entites which will then be placed in Blockchaing. Since sawtooth has addressing mechanism that based on keys, we would also define keys as part of the entities that will identify ehtn in Sawtooh State, 
Google Protobuf , was chosen to model the entities as these objects are portable and we may not need them to parse and marshall them to JSON. Also while storing and retiesing , since sawtooth required the data to be opaque to core sawtooth system , protos do support bninary format.

Entity Payloads - Along with entity , you woudl define entity payload as well which would be based on command pattern,data and operation in the payload itself. Clients will send actual data embedded in the payloads and the payload attributes will define the operations to be performed on the entity.

2. Wrtitni Transaction Families.
Once you define the Entity Model above, the design lets you write and define transaction processors, validator and addressing w.r.t. the entity you define. So if you have an Entity as Account, then you will wrte below for Account
1. A transaciotn processor
2. A Transcation Handler
3. A transaction data Validator
4. A payload Parser
5. 

The project provided framework classes to define each component above and also abstract away the design compexitiess.


3. Writing Client
Client code is the code that will be running on the client applicaiton side. Remember - client application is different that the sawtooth clients here . The Sawtooth client will submit transactions/batches to sawtootha and subscribe and respond to events. While the actuall business logis will reside in the client applicaiton.
The framework abstracts/ eases the submisssion of the transactions to the sawtooth which otherwise is very strict process. Currently , if you do any mistake while sending transaction , it rejectd by the sawtooth netowork and very difficult to find the reason.
In order to write a client , the desgn is though make it easier in a way for implementer that they only need to create the Payload and Submit the payload and then check response.Thats it!To accomplish all thsebelow ar ethe framework compoenets provided

1. Generic Transaciton Builder
2. ZMQ Template
3. Generic Batch Builder
4. Simple Service Absctractio, that implemeters can extend and implement.
