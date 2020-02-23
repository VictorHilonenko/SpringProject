-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: localhost    Database: spring_scheduler
-- ------------------------------------------------------
-- Server version	8.0.18

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
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES (241,'2020-02-16',11,_binary '','HAIRDRESSING',29,16),(242,'2020-02-17',9,_binary '','MAKEUP',29,18),(243,'2020-02-18',14,_binary '','COSMETOLOGY',29,28),(244,'2020-02-19',9,_binary '','MAKEUP',29,18),(245,'2020-02-20',10,_binary '','COSMETOLOGY',29,28),(246,'2020-02-21',12,_binary '','HAIRDRESSING',29,16),(247,'2020-02-22',15,_binary '','MAKEUP',29,18),(248,'2020-02-16',14,_binary '','HAIRDRESSING',23,16),(249,'2020-02-13',13,_binary '\0','MAKEUP',23,18),(250,'2020-02-16',13,_binary '','MAKEUP',23,18),(251,'2020-02-11',11,_binary '\0','COSMETOLOGY',23,28),(252,'2020-02-18',11,_binary '','COSMETOLOGY',23,28),(253,'2020-02-19',13,_binary '','MAKEUP',23,18),(254,'2020-02-20',15,_binary '','MAKEUP',23,18),(255,'2020-02-22',10,_binary '\0','HAIRDRESSING',23,16),(256,'2020-02-17',17,_binary '','HAIRDRESSING',15,16),(257,'2020-02-17',9,_binary '','MAKEUP',15,26),(258,'2020-02-19',16,_binary '','HAIRDRESSING',15,16),(259,'2020-02-23',9,_binary '\0','HAIRDRESSING',15,16),(260,'2020-02-10',10,_binary '\0','HAIRDRESSING',29,16),(261,'2020-02-12',15,_binary '\0','MAKEUP',29,18),(262,'2020-02-22',20,_binary '','COSMETOLOGY',29,28),(263,'2020-02-10',12,_binary '\0','HAIRDRESSING',29,16),(264,'2020-04-02',13,_binary '\0','HAIRDRESSING',29,16),(265,'2020-10-02',14,_binary '\0','MAKEUP',29,18),(266,'2020-03-02',8,_binary '\0','COSMETOLOGY',29,28),(267,'2020-03-02',8,_binary '\0','HAIRDRESSING',29,16),(269,'2020-03-02',11,_binary '\0','MAKEUP',29,18),(270,'2020-02-03',11,_binary '','MAKEUP',29,18),(271,'2020-02-03',11,_binary '\0','MAKEUP',29,26),(272,'2020-02-16',10,_binary '\0','HAIRDRESSING',29,16),(274,'2020-02-15',8,_binary '\0','HAIRDRESSING',29,16),(275,'2020-02-15',9,_binary '\0','MAKEUP',29,18),(276,'2020-02-15',9,_binary '\0','COSMETOLOGY',29,28),(277,'2020-02-15',10,_binary '','MAKEUP',29,18),(278,'2020-02-02',8,_binary '','MAKEUP',29,18),(279,'2020-02-03',9,_binary '','COSMETOLOGY',29,28),(280,'2020-02-02',11,_binary '\0','HAIRDRESSING',29,16),(281,'2020-05-02',15,_binary '\0','HAIRDRESSING',29,16),(282,'2020-05-02',15,_binary '\0','MAKEUP',29,18),(283,'2020-02-05',15,_binary '\0','HAIRDRESSING',29,16),(284,'2020-02-09',15,_binary '\0','HAIRDRESSING',29,16),(285,'2020-02-05',9,_binary '\0','MAKEUP',29,18),(286,'2020-02-07',17,_binary '\0','HAIRDRESSING',29,16),(287,'2020-02-06',16,_binary '\0','MAKEUP',29,18),(288,'2020-02-06',8,_binary '\0','COSMETOLOGY',29,28);
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `email_messages`
--

LOCK TABLES `email_messages` WRITE;
/*!40000 ALTER TABLE `email_messages` DISABLE KEYS */;
INSERT INTO `email_messages` VALUES (1,'2020-02-21','caitlin@mail.com','3a66a9ad-7428-4bcd-aa55-eaab6648d4e2','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/252/3a66a9ad-7428-4bcd-aa55-eaab6648d4e2',_binary '',NULL),(2,'2020-02-21','lindsey@mail.com','','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/243/c5cbe245-0e56-4a4f-aa22-6586c684faff',_binary '',NULL),(3,'2020-02-21','lindsey@mail.com','','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/245/dccba8da-1f04-408a-9135-02e7c14fec78',_binary '',NULL),(4,'2020-02-21','jennie@mail.com','f822bfdd-0891-43ff-8374-e5b3450885f3','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/257/f822bfdd-0891-43ff-8374-e5b3450885f3',_binary '',NULL),(5,'2020-02-21','lindsey@mail.com','f2e41cb2-f5c8-42b0-8e2f-0a89c532ee7f','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/242/f2e41cb2-f5c8-42b0-8e2f-0a89c532ee7f',_binary '',NULL),(6,'2020-02-21','caitlin@mail.com','78d11318-279f-4d1c-83a8-0f81effbed20','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/250/78d11318-279f-4d1c-83a8-0f81effbed20',_binary '',NULL),(7,'2020-02-21','caitlin@mail.com','8a3a6fa6-494f-4ec5-9055-ff36dccdea6f','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/253/8a3a6fa6-494f-4ec5-9055-ff36dccdea6f',_binary '',NULL),(8,'2020-02-21','lindsey@mail.com','b359be9d-3020-4d8c-879a-24b39d3d588f','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/244/b359be9d-3020-4d8c-879a-24b39d3d588f',_binary '',NULL),(9,'2020-02-21','caitlin@mail.com','f3f39f17-c336-4333-aaf3-f27092ac55f7','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/254/f3f39f17-c336-4333-aaf3-f27092ac55f7',_binary '',NULL),(10,'2020-02-21','lindsey@mail.com','ada9f34f-2a26-4cf0-b479-d84d29b0bae9','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/241/ada9f34f-2a26-4cf0-b479-d84d29b0bae9',_binary '',NULL),(11,'2020-02-21','caitlin@mail.com','','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/248/420f139b-a6a6-48ac-ac2c-420b79738ddb',_binary '',NULL),(12,'2020-02-21','jennie@mail.com','','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/258/036668e1-0ee4-461c-bb36-92717ce85df4',_binary '',NULL),(13,'2020-02-21','jennie@mail.com','','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/256/ff7fcf24-501f-41f5-bd08-600d6847b5bf',_binary '',NULL),(14,'2020-02-21','lindsey@mail.com','','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/246/f0a2291d-de16-4fc7-990c-6221fcf6142f',_binary '',NULL),(15,'2020-02-22','lindsey@mail.com','','Залиште відгук, будь ласка','Будь ласка, залиште Ваш відгук тут: http://localhost:8989/feedbacks/247/6fc21529-dd4a-4479-8217-854235367a65',_binary '',NULL),(16,'2020-02-22','lindsey@mail.com','dcca3335-e0cd-459d-932c-e146aa8906ac','leave feedback, please','please, leave your feedback here: http://localhost:8989/feedbacks/262/dcca3335-e0cd-459d-932c-e146aa8906ac',_binary '',NULL),(17,'2020-02-22','lindsey@mail.com','c85ecf7a-7691-4a08-a228-04641b1b8316','leave feedback, please','please, leave your feedback here: http://localhost:8989/feedbacks/262/c85ecf7a-7691-4a08-a228-04641b1b8316',_binary '',NULL),(18,'2020-02-22','lindsey@mail.com','907c666a-416e-46cf-821b-663fc15bfb7c','leave feedback, please','please, leave your feedback here: http://localhost:8989/feedbacks/262/907c666a-416e-46cf-821b-663fc15bfb7c',_binary '',NULL),(19,'2020-02-22','lindsey@mail.com','6f975e70-42bc-4fe7-9d50-02243adcc351','leave feedback, please','please, leave your feedback here: http://localhost:8989/feedbacks/262/6f975e70-42bc-4fe7-9d50-02243adcc351',_binary '',NULL),(20,'2020-02-22','lindsey@mail.com','8e05c5bd-1309-4773-9ef3-258f10f03f19','leave feedback, please','please, leave your feedback here: http://localhost:8989/feedbacks/262/8e05c5bd-1309-4773-9ef3-258f10f03f19',_binary '',NULL),(21,'2020-02-23','lindsey@mail.com','','leave feedback, please','please, leave your feedback here: http://localhost:8989/feedbacks/278/346ae034-a798-437d-add5-6fc32c555a1e',_binary '',NULL),(22,'2020-02-23','admin_email_test@ukr.net','','leave feedback, please','please, leave your feedback here: http://localhost:8989/feedbacks/270/21531a81-f257-4c47-84cc-4975135617d2',_binary '',NULL),(24,'2020-02-23','admin_email_test@ukr.net','','leave feedback, please','please, leave your feedback here: http://localhost:8889/feedbacks/279/dc38224a-578f-43e7-8825-53e77eef5bf2',_binary '',NULL),(25,'2020-02-23','lindsey@mail.com','','leave feedback, please','please, leave your feedback here: http://localhost:8889/feedbacks/277/17a19be1-2386-44ba-b10e-ec4484ea5908',_binary '',NULL);
/*!40000 ALTER TABLE `email_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `feedbacks`
--

LOCK TABLES `feedbacks` WRITE;
/*!40000 ALTER TABLE `feedbacks` DISABLE KEYS */;
INSERT INTO `feedbacks` VALUES (1,10,'Дуже дякую за прекрасний сервіс!!!',243),(2,10,'Дуже дякую! Незабутні враження від вашого підходу до клієнтів!',245),(3,10,'The best service ever! Great thanks!',248),(4,10,'The best service ever! Great thanks!',250),(5,10,'The best service ever! Great thanks!',252),(6,10,'The best service ever! Great thanks!',253),(7,10,'The best service ever! You are real professionals!',254),(10,10,'Найкращий сервіс і найкращий салон! Рекомендуватиму вас всім друзям!',256),(11,10,'Найкращий сервіс і найкращий салон! Рекомендуватиму вас всім друзям!',257),(12,10,'Найкращий сервіс і найкращий салон! Рекомендуватиму вас всім друзям!',258),(13,10,'Дуже дякую! Незабутні враження від вашого підходу до клієнтів!',241),(14,10,'Дуже дякую! Незабутні враження від вашого підходу до клієнтів!',242),(15,10,'Дуже дякую! Незабутні враження від вашого підходу до клієнтів!',244),(16,10,'Ви зробили мені свято!',246),(20,10,'Thank you!!!',247),(21,10,'Дякую!!!',262),(22,10,'Дуже дякую за прекрасний сервіс!!!',270),(23,10,'Дуже дякую за прекрасний сервіс!!!',278),(24,10,'Best service!!!',279),(25,10,'Дуже дякую за прекрасний сервіс!!!',277);
/*!40000 ALTER TABLE `feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (14,'admin@mail.com','$2a$10$it5GyDjgwDCzSQdvZNzfIOwq1TAm4mf0phWLvZXMFtvTM6.4z5rm.','ROLE_ADMIN','NULL','+380671234567','Admin','Admin'),(15,'jennie@mail.com','$2a$10$dGkGLJal4NnWugv4IYcTkueQEOxAjMbu0U9loOBQuDjTsGueR8zUG','ROLE_USER','NULL','+380661596897','Jennie','Johnson'),(16,'julia@mail.com','$2a$10$w1aZ.IvvQcRCIuFiVv4ooONdgI69KRDbNYUp56idGZUG7LoCcnW7G','ROLE_MASTER','HAIRDRESSING','+380675796428','Julia','Skrypka'),(18,'tanya@mail.com','$2a$10$w1aZ.IvvQcRCIuFiVv4ooONdgI69KRDbNYUp56idGZUG7LoCcnW7G','ROLE_MASTER','MAKEUP','+380693796425','Tetyana','Bila'),(23,'caitlin@mail.com','$2a$10$dGkGLJal4NnWugv4IYcTkueQEOxAjMbu0U9loOBQuDjTsGueR8zUG','ROLE_USER','NULL','+380686154512','Caitlin','Ville'),(26,'nata@mail.com','$2a$10$w1aZ.IvvQcRCIuFiVv4ooONdgI69KRDbNYUp56idGZUG7LoCcnW7G','ROLE_MASTER','MAKEUP','+380676517777','Natalya','Krasko'),(28,'josy@mail.com','$2a$10$w1aZ.IvvQcRCIuFiVv4ooONdgI69KRDbNYUp56idGZUG7LoCcnW7G','ROLE_MASTER','COSMETOLOGY','+380672574563','Josephine','Jennings'),(29,'lindsey@mail.com','$2a$10$dGkGLJal4NnWugv4IYcTkueQEOxAjMbu0U9loOBQuDjTsGueR8zUG','ROLE_USER','NULL','+380672561819','Lindsey','Star');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-23 16:15:41
