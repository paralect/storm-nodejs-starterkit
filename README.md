Storm-Node.js Starter Kit
=========================

Starter Kit for Apache Storm and Node.js projects. Tested with Apache Storm 0.9.2.

### Prerequisites

1. Maven 3. On Ubuntu install it with:

    ```
    $ sudo apt-get install maven
    ```

2. Node.js

3. If you want to submit your topologies to real Storm cluster, you need to deploy Storm and add `bin` folder of Storm to
your `PATH` variable.

### Project structure

| Folder                  | Description                   |
| ----------------------- |-------------------------------|
| `multilang/resources`   | JavaScript files (Node.js)    |
| `src/main/java`         | Java files                    |


### Run topology locally (development mode)

    $ cd storm-nodejs-starterkit
    $ bin/run-simple-topology.sh

### Submit topology to the Storm cluster

    $ cd storm-nodejs-starterkit
    $ bin/submit-simple-topology.sh

### Open project with Intellij IDEA

`File` -> `Open Project` and browse to `pom.xml` file.

### Run topology within Intellij IDEA

Open topology java file (i.e. `src/main/java/com/paralect/TestTopology.java`), right-click in the editor and select
`Run TestTopology.main()`. Or hit <kbd>Ctrl-Shift-F10</kbd>. This will add Build Configuration to your Intellij project. After that you can run (<kbd>Shift+F10</kbd>) or debug (<kbd>Shift+F9</kbd>) project.


## How to Deploy Storm locally


Read this if you need to quickly install Storm on your local machine

#### Setup Zookeeper


1. Download Zookeeper:

    ```
    http://ftp.byfly.by/pub/apache.org/zookeeper/zookeeper-3.4.6/zookeeper-3.4.6.tar.gz
    ```

2. Unpack and `cd` to this directory

3. Create config file in (config.cfg) `conf` folder:

    ```
    tickTime=2000
    dataDir=/home/dmitry/apps/zookeeper/data
    clientPort=2181
    ```

4. Start Zookeeper:

    ```
    bin/zkServer.sh start conf/config.cfg
    ```

5. Test connection to Zookeeper

    ```
    bin/zkCli.sh -server 127.0.0.1:2181
    ```

Here you can write commands like ls, get, set. Type `help` to see all commands. Type `quit` to exit from zookeeper shell.


#### Setup Storm

1. Download from:

    ```
    http://ftp.byfly.by/pub/apache.org/incubator/storm/apache-storm-0.9.2-incubating/apache-storm-0.9.2-incubating.tar.gz
    ```

2. Unpack and `cd` to this directory.

3. Edit `conf/storm.yaml`:

    ```
    storm.zookeeper.servers:
        - "localhost"

    storm.zookeeper.port: 2181
    ```

4. Run Storm daemons:

    ```
    bin/storm nimbus
    bin/storm supervisor
    bin/storm ui
    ```

(Run each command in different shell window)

5. Open Storm UI:

    ```
    http://localhost:8080/index.html
    ```

