# Kafka Topics:
- PizzaReqTopic - Ordering a pizza
- PizzaInfoTopic - Pizza information
- DeliveryInfoTopic - Delivery information
- PizzaOrderingFailTopic - Failed pizza ordering
- finalize - Finalize order and ste status COMPLETED


# State Machine
- Processing State: *NONE, STARTED, FINISHED, CANCELLED, COMPLETED* - 
Stan przetwarzania danej instancji usługi, czyli węzeł grafu zmiany stanów

- Processign Event ** - 
zdarzenie, które zmienia stan danej instancji usługi, zyli krawędź skierowana grafu zmiany stanów

![State Diagram](images/states.png)
*State Diagram*

### Kafka setup for Windows
1. Download Kafka:  
```wersja: kafka_2.12-2.6.0.tgz```
2. Extract the Archive:  
```
tar -xf kafka_2.12-2.6.0.tgz
Move-Item -Path "kafka_2.12-2.6.0" -Destination "C:\kafka"
```
3.Download Configuration Files:  
```
Invoke-WebRequest -Uri "http://www.cs.put.poznan.pl/jjezierski/AZNUv2/zookeeper.service" -OutFile "C:\kafka\zookeeper.service"
Invoke-WebRequest -Uri "http://www.cs.put.poznan.pl/jjezierski/AZNUv2/kafka.service" -OutFile "C:\kafka\kafka.service"
```
4. Start Zookeeper and Kafka:  
```
Start-Process -FilePath "C:\kafka\bin\windows\zookeeper-server-start.bat" -ArgumentList "C:\kafka\config\zookeeper.properties"
Start-Process -FilePath "C:\kafka\bin\windows\kafka-server-start.bat" -ArgumentList "C:\kafka\config\server.properties"
```

![Build Image Configuration](images/build_image_config.png)
*Build Image Configuration*