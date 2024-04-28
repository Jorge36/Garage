-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: garage
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appoinment`
--

DROP TABLE IF EXISTS `appoinment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appoinment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_email` varchar(50) NOT NULL,
  `date` date DEFAULT NULL,
  `comment` varchar(200) DEFAULT NULL,
  `status_id` int NOT NULL,
  `type_app_id` int NOT NULL,
  PRIMARY KEY (`id`,`user_email`),
  KEY `fk_Appoinment_status1_idx` (`status_id`),
  KEY `fk_Appoinment_type_app1_idx` (`type_app_id`),
  KEY `fk_Appoinments_user1` (`user_email`),
  CONSTRAINT `fk_Appoinment_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  CONSTRAINT `fk_Appoinment_type_app1` FOREIGN KEY (`type_app_id`) REFERENCES `type_app` (`id`),
  CONSTRAINT `fk_Appoinments_user1` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appoinment`
--

LOCK TABLES `appoinment` WRITE;
/*!40000 ALTER TABLE `appoinment` DISABLE KEYS */;
/*!40000 ALTER TABLE `appoinment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appoinment_has_shift`
--

DROP TABLE IF EXISTS `appoinment_has_shift`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appoinment_has_shift` (
  `appoinment_id` int NOT NULL,
  `appoinment_user_email` varchar(50) NOT NULL,
  `shift_id` int NOT NULL,
  PRIMARY KEY (`appoinment_id`,`appoinment_user_email`,`shift_id`),
  KEY `fk_appoinment_has_shift_shift1_idx` (`shift_id`),
  KEY `fk_appoinment_has_shift_appoinment1_idx` (`appoinment_id`,`appoinment_user_email`),
  CONSTRAINT `fk_appoinment_has_shift_appoinment1` FOREIGN KEY (`appoinment_id`, `appoinment_user_email`) REFERENCES `appoinment` (`id`, `user_email`),
  CONSTRAINT `fk_appoinment_has_shift_shift1` FOREIGN KEY (`shift_id`) REFERENCES `shift` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appoinment_has_shift`
--

LOCK TABLES `appoinment_has_shift` WRITE;
/*!40000 ALTER TABLE `appoinment_has_shift` DISABLE KEYS */;
/*!40000 ALTER TABLE `appoinment_has_shift` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cost`
--

DROP TABLE IF EXISTS `cost`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cost` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'It could be the invoice number',
  `Appoinment_user_email` varchar(50) NOT NULL,
  `Appoinment_id` int NOT NULL,
  `total` decimal(15,2) NOT NULL,
  `currency` varchar(3) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`,`Appoinment_user_email`,`Appoinment_id`),
  KEY `fk_Cost_Appoinment1` (`Appoinment_user_email`,`Appoinment_id`),
  CONSTRAINT `fk_Cost_Appoinment1` FOREIGN KEY (`Appoinment_user_email`, `Appoinment_id`) REFERENCES `appoinment` (`user_email`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cost`
--

LOCK TABLES `cost` WRITE;
/*!40000 ALTER TABLE `cost` DISABLE KEYS */;
/*!40000 ALTER TABLE `cost` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detail`
--

DROP TABLE IF EXISTS `detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `Cost_Appoinment_user_email` varchar(50) NOT NULL,
  `Cost_Appoinment_id` int NOT NULL,
  `Cost_id` int NOT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  `task` varchar(50) DEFAULT NULL,
  `Item_id` int DEFAULT NULL,
  PRIMARY KEY (`id`,`Cost_Appoinment_user_email`,`Cost_Appoinment_id`,`Cost_id`),
  KEY `fk_Detail_Item1_idx` (`Item_id`),
  KEY `fk_Detail_Cost1` (`Cost_Appoinment_user_email`,`Cost_Appoinment_id`,`Cost_id`),
  CONSTRAINT `fk_Detail_Cost1` FOREIGN KEY (`Cost_Appoinment_user_email`, `Cost_Appoinment_id`, `Cost_id`) REFERENCES `cost` (`Appoinment_user_email`, `Appoinment_id`, `id`),
  CONSTRAINT `fk_Detail_Item1` FOREIGN KEY (`Item_id`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail`
--

LOCK TABLES `detail` WRITE;
/*!40000 ALTER TABLE `detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `engine_type`
--

DROP TABLE IF EXISTS `engine_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `engine_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `engine_type`
--

LOCK TABLES `engine_type` WRITE;
/*!40000 ALTER TABLE `engine_type` DISABLE KEYS */;
INSERT INTO `engine_type` VALUES (1,'diesel'),(2,'petrol'),(3,'hybrid'),(4,'electric');
/*!40000 ALTER TABLE `engine_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `price` decimal(15,2) DEFAULT NULL,
  `currency` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (1,'wing mirror',100.00,'EUR'),(2,'tyre',200.00,'EUR'),(3,'exhaust pipe',500.00,'EUR'),(4,'reflector',50.00,'EUR'),(5,'Seat',900.00,'EUR');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `make`
--

DROP TABLE IF EXISTS `make`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `make` (
  `id` int NOT NULL AUTO_INCREMENT,
  `make` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `make`
--

LOCK TABLES `make` WRITE;
/*!40000 ALTER TABLE `make` DISABLE KEYS */;
INSERT INTO `make` VALUES (1,'Volkswagen'),(2,'BMW'),(3,'Audi'),(4,'Mitsubishi'),(5,'Ford'),(6,'Toyota'),(7,'Maserati'),(8,'Honda'),(9,'Subaru'),(10,'Hyundai'),(11,'	Power Vehicle Innovation'),(12,'Ikarus'),(13,'Fiat'),(14,'Alfa Romeo'),(15,'Volvo'),(16,'Nissan'),(17,'Other');
/*!40000 ALTER TABLE `make` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mechanic`
--

DROP TABLE IF EXISTS `mechanic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mechanic` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mechanic`
--

LOCK TABLES `mechanic` WRITE;
/*!40000 ALTER TABLE `mechanic` DISABLE KEYS */;
/*!40000 ALTER TABLE `mechanic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shift`
--

DROP TABLE IF EXISTS `shift`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shift` (
  `id` int NOT NULL,
  `time` time DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shift`
--

LOCK TABLES `shift` WRITE;
/*!40000 ALTER TABLE `shift` DISABLE KEYS */;
INSERT INTO `shift` VALUES (1,'08:00:00','Morning 1'),(2,'10:00:00','Morning 2'),(3,'13:00:00','Afternoon 1'),(4,'15:00:00','Afternoon 2');
/*!40000 ALTER TABLE `shift` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shift_has_mechanic`
--

DROP TABLE IF EXISTS `shift_has_mechanic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shift_has_mechanic` (
  `shift_id` int NOT NULL,
  `Mechanic_id` int NOT NULL,
  PRIMARY KEY (`shift_id`,`Mechanic_id`),
  KEY `fk_shift_has_Mechanic_Mechanic1_idx` (`Mechanic_id`),
  KEY `fk_shift_has_Mechanic_shift1_idx` (`shift_id`),
  CONSTRAINT `fk_shift_has_Mechanic_Mechanic1` FOREIGN KEY (`Mechanic_id`) REFERENCES `mechanic` (`id`),
  CONSTRAINT `fk_shift_has_Mechanic_shift1` FOREIGN KEY (`shift_id`) REFERENCES `shift` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shift_has_mechanic`
--

LOCK TABLES `shift_has_mechanic` WRITE;
/*!40000 ALTER TABLE `shift_has_mechanic` DISABLE KEYS */;
/*!40000 ALTER TABLE `shift_has_mechanic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` varchar(12) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'booked'),(2,'in service'),(3,'fixed'),(4,'collected'),(5,'unrepairable');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_app`
--

DROP TABLE IF EXISTS `type_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_app` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(14) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_app`
--

LOCK TABLES `type_app` WRITE;
/*!40000 ALTER TABLE `type_app` DISABLE KEYS */;
INSERT INTO `type_app` VALUES (1,'annual service'),(2,'major service'),(3,'repair'),(4,'major repair');
/*!40000 ALTER TABLE `type_app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_car`
--

DROP TABLE IF EXISTS `type_car`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_car` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_car`
--

LOCK TABLES `type_car` WRITE;
/*!40000 ALTER TABLE `type_car` DISABLE KEYS */;
INSERT INTO `type_car` VALUES (1,'motorbike'),(2,'car'),(3,'small van'),(4,'small bus');
/*!40000 ALTER TABLE `type_car` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_user`
--

DROP TABLE IF EXISTS `type_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(5) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_user`
--

LOCK TABLES `type_user` WRITE;
/*!40000 ALTER TABLE `type_user` DISABLE KEYS */;
INSERT INTO `type_user` VALUES (1,'admin'),(2,'user');
/*!40000 ALTER TABLE `type_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `email` varchar(50) NOT NULL,
  `password` varchar(45) NOT NULL,
  `name` varchar(50) NOT NULL,
  `mobilePhone` varchar(20) DEFAULT NULL,
  `type_user_id` int NOT NULL,
  `plateNumber` varchar(15) DEFAULT NULL,
  `make` varchar(30) DEFAULT NULL,
  `engine_type_id` int NOT NULL,
  `type_car_id` int NOT NULL,
  PRIMARY KEY (`email`),
  KEY `fk_user_type_user1_idx` (`type_user_id`),
  KEY `fk_user_engine_type1_idx` (`engine_type_id`),
  KEY `fk_user_type_car1_idx` (`type_car_id`),
  CONSTRAINT `fk_user_engine_type1` FOREIGN KEY (`engine_type_id`) REFERENCES `engine_type` (`id`),
  CONSTRAINT `fk_user_type_car1` FOREIGN KEY (`type_car_id`) REFERENCES `type_car` (`id`),
  CONSTRAINT `fk_user_type_user1` FOREIGN KEY (`type_user_id`) REFERENCES `type_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('admin@gmail.com','7f04ebc02afc7b7100b99672beca300233b10210','admin','+3530869046607',1,'','',2,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-08 15:34:03
