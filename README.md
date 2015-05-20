# Cassandra Tester
Load tester for Cassandra, written in Java

## Dependencies
* Java 1.8+
* A Cassandra cluster supporting CQL
* An existing keyspace on the Cassandra cluster

## Usage
Executing the jar from the commandline will give the following:
```
sam$ java -jar CassTest.jar
Usage: CassTest.jar <host> <keyspace> <mode> <threads>
```
* &lt;host&gt; is the resolvable (FQDN, IP or DNS) address of the cluster
* &lt;keyspace&gt; is the keyspace to use on the cluster
* &lt;mode&gt; is one of create, get or put (see below)
* &lt;threads&gt; is how many operation threads to spawn

Example: ```java -jar CassTest.jar 127.0.0.1 performancetest get 10```

### Creating required tables
To create the requried tables, execute the following (where the keyspace is performancetest):

```java -jar CassTest.jar 127.0.0.1 performancetest create 0```

### Operation Modes
#### Create
This will create the required tables on the cluster and then exit
#### Get
This will perform read operations on the cluster
#### Put
This will perform write operations on the cluster

## Output
When starting up, each thread will print its connection time: ```thread0 connected in 306ms```

Every 5 seconds, each thread will print the number of operations it has done per second: ```thread0 : 1071 reads/s```

Once a thread is complete, it will shut down: ```thread0 shutting down```
### Thread naming
Threads are named automatically, in sequential order. 10 threads would be named ```thread0``` to ```thread9```

## Data storage
Each write thread has a unique name (see above), and will generate row keys starting with that name, and ending with a random number between 0 and 1000000. For example, ```thread0-row-413964```.

Each of these rows will have three columns written to them: ```value1```, ```value2``` and ```value3```
