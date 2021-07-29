## 集群节点类型

1. 集群中的每一个节点都保存了所有的队列、交换器、绑定关系、用户、权限和vhost的元数据信息。
2. 集群中的节点分为两类：内存节点【ram】和磁盘节点【disc】。

### 磁盘节点【disc】

1. 单节点的集群中只有磁盘节点；多节点的集群至少有一个磁盘节点。
2. 集群中所有磁盘节点都崩溃时，集群可以继续发送和接收消息，但不能执行创建队列、交换器等修改元数据的操作。
3. 在集群中添加内存节点，要告知所有的磁盘节点。内存节点至少找到一个磁盘节点，就可以加入集群。
4. 为了保证集群信息的可靠性，建议全部使用磁盘节点。

### 内存节点【ram】

1. 加入集群时指定为内存节点

   ```shell
   rabbitmqctl join_cluster rabbit@node1 --ram
   ```

2. 修改集群节点类型

   ```shell
   rabbitmqctl stop_app
   rabbitmqctl change_cluster_node_type {disc|ram}
   rabbitmqctl start_app
   ```

3. 在集群中创建队列，交换器或者绑定关系的时候，这些操作直到所有集群节点都成功提交元数据变更才返回，故内存节点可以提供出色的性能。

4. 内存节点重启后，会连接到预先配置的磁盘节点，下载集群元数据的副本。

5. 在集群中创建队列，交换器或者绑定关系的时候，这些操作直到所有集群节点都成功提交元数据变更才返回，故内存节点可以提供出色的性能。

6. 内存节点重启后，会连接到预先配置的磁盘节点，下载集群元数据的副本。

## 剔除单个节点

以node1、node2、node3集群为例，剔除node2

### 方法一

1. 在node2上执行

   ````shell
   rabbitmqctl stop_app
   ````

2. 在node1或者node3上执行

   ```shell
   rabbitmqctl forget_cluster_node rabbit@node2 [-offline]
   ```

   1. 不加offline：需要节点在运行状态下。
   2. 加offline：节点可以在离线状态下。比如集群关闭顺序为：node3、node2、node1。可以在节点node2上执行forget_cluster_node来剔除node1。

### 方法二

在node2上执行

```
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl start_app
```

### 集群节点的升级

### 单节点集群升级

1. 关闭原来的服务
2. 新节点的mnesia路径指向与原节点相同。
3. 解压新版本运行

### 多节点集群升级

1. 关闭所有的节点的服务，注意采用rabbitmqctl stop命令关闭。
2. 保存各个节点的mnesia数据。
3. 解压新版本的rabbitmq到指定的目录。
4. 指定新版本的mnesia路径为步骤2中保存的mnesia数据路径。
5. 启动新版本的服务，注意先启动原版本中最后关闭的节点。

### 单机多节点

```shell
# 启动
RABBITMQ_NODE_PORT=5672 RABBITMQ_NODENAME=rabbit1 RABBITMQ_SERVER_START_ARGS="-rabbitmq_management listener [{port,15672}]" rabbitmq-server -detached
RABBITMQ_NODE_PORT=5673 RABBITMQ_NODENAME=rabbit2 RABBITMQ_SERVER_START_ARGS="-rabbitmq_management listener [{port,15673}]" rabbitmq-server -detached
RABBITMQ_NODE_PORT=5674 RABBITMQ_NODENAME=rabbit3 RABBITMQ_SERVER_START_ARGS="-rabbitmq_management listener [{port,15674}]" rabbitmq-server -detached

# 加入集群
rabbitmqctl -n rabbit2@zhangshuai24deMacBook-Pro stop_app
rabbitmqctl -n rabbit2@zhangshuai24deMacBook-Pro reset
rabbitmqctl -n rabbit2@zhangshuai24deMacBook-Pro join_cluster rabbit1@zhangshuai24deMacBook-Pro
rabbitmqctl -n rabbit2@zhangshuai24deMacBook-Pro start_app
rabbitmqctl -n rabbit3@zhangshuai24deMacBook-Pro stop_app
rabbitmqctl -n rabbit3@zhangshuai24deMacBook-Pro reset
rabbitmqctl -n rabbit3@zhangshuai24deMacBook-Pro join_cluster rabbit1@zhangshuai24deMacBook-Pro
rabbitmqctl -n rabbit3@zhangshuai24deMacBook-Pro start_app
```