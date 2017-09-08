-- MySQL dump 10.13  Distrib 5.7.16, for Win64 (x86_64)
--
-- Host: localhost    Database: contact_book
-- ------------------------------------------------------
-- Server version	5.7.16-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attachment` (
  `attachment_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `file_path` varchar(260) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `upload_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `comment` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deletion_date` timestamp NULL DEFAULT NULL,
  `contact_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`attachment_id`),
  KEY `fk_attachment_contact` (`contact_id`),
  CONSTRAINT `fk_attachment_contact` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`contact_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment`
--

LOCK TABLES `attachment` WRITE;
/*!40000 ALTER TABLE `attachment` DISABLE KEYS */;
INSERT INTO `attachment` VALUES (3,'C:\\contact book\\contact files\\231\\file4268183523302365716.mp4','hehe','2017-08-11 17:12:22','',NULL,231),(4,'C:\\contact book\\contact files\\235\\file8208489027425690580.mp4','vidos.mp4','2017-08-15 11:57:50','',NULL,235),(5,'C:\\contact book\\contact files\\235\\file2806784221527445468.mp4','Donald Trump on Climate Change.mp4','2017-08-15 11:57:50','',NULL,235);
/*!40000 ALTER TABLE `attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact` (
  `contact_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `surname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `patronymic` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `birth` date DEFAULT NULL,
  `gender` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `citizenship` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `marital_status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `website` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `job` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `city` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `street` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `postal_code` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deletion_date` timestamp NULL DEFAULT NULL,
  `profile_picture` varchar(260) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`contact_id`)
) ENGINE=InnoDB AUTO_INCREMENT=284 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
INSERT INTO `contact` VALUES (230,'Alex','Petrov','unknown',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2017-08-16 10:17:47',NULL),(231,'Ivan','Groznyj','unknown',NULL,'','','','','','','','','','','2017-08-16 10:17:47',NULL),(232,'Petr','Velikij','unknown',NULL,'','','','','','','','','','12314312431323413541534','2017-08-16 10:17:47',NULL),(233,'Федор','Емельяненко','',NULL,'','','','','','','','','','','2017-08-16 10:17:47',NULL),(234,'Алексей','Малейко','Сергеевич',NULL,'','Belarus','','','alexx2x3@gmail.com','','','','','',NULL,NULL),(235,'Новый','Контакт','',NULL,'','','','','','','','','','','2017-08-16 10:17:29',NULL),(236,'asdfsadfsadf','asdfasdfsadf','',NULL,'','','','','','','','','','','2017-09-06 17:40:12',NULL),(237,'asdfasdf','asdfasfd','',NULL,'','','','','','','','','','','2017-09-06 19:15:54',NULL),(238,'asdfasdf','asdfasdg','',NULL,'','','','','','','','','','','2017-09-06 19:15:54',NULL),(239,'asdgsfdg','sdfgsdfgsdfg','',NULL,'','','','','','','','','','','2017-09-06 19:50:17',NULL),(240,'agsdfgsdfg','sdfgsdfgsdfgs','',NULL,'','','','','','','','','','','2017-09-06 19:50:17',NULL),(241,'sadfsdgsdfg','sdfgsdfgsfdg','',NULL,'','','','','','','','','','','2017-09-06 19:50:17',NULL),(242,'фывайукпйукп','цукпцукп','',NULL,'','','','','','','','','','','2017-09-06 19:50:17',NULL),(243,'йцуйцупйукп','цукпцукпцукп','',NULL,'','','','','','','','','','','2017-09-06 19:50:22',NULL),(244,'йцууйцкпцукп','кеуоенокео','',NULL,'','','','','','','','','','','2017-09-06 19:50:17',NULL),(245,'фывацкуп3кп','цукпцукпцукп','',NULL,'','','','','','','','','','','2017-09-06 19:50:22',NULL),(246,'цукпцукпцук','пцукпцукпцуп','',NULL,'','','','','','','','','','','2017-09-06 19:50:17',NULL),(247,'цукрцукрцукр','выарварыавр','',NULL,'','','','','','','','','','','2017-09-06 19:50:17',NULL),(248,'выапывапыавп','выапывапывап','',NULL,'','','','','','','','','','','2017-09-06 19:50:17',NULL),(249,'укенукенуекн','врпварпварп','',NULL,'','','','','','','','','','','2017-09-06 19:50:17',NULL),(250,'Илья','Пархейчук','Олегович',NULL,'','','','','','','','','','',NULL,NULL),(251,'Александр','Вашина','Александрович',NULL,'','','','','','','','','','',NULL,NULL),(252,'Игорь','Лететский','Андреевич',NULL,'','','','','','','','','','',NULL,NULL),(253,'Богдан','Паремский','Олегович',NULL,'','','','','','','','','','',NULL,NULL),(254,'Александр','Беляев','Александрович',NULL,'','','','','','','','','','',NULL,NULL),(255,'Татьяна','Денисова','Дмитриевна',NULL,'f','','','','','','','','','',NULL,NULL),(256,'Наталья','Станишевская','Юрьевна',NULL,'','','','','','','','','','',NULL,NULL),(257,'Артем','Семериков','Игоревич',NULL,'','','','','','','','','','',NULL,NULL),(258,'Антон','Некрасов','',NULL,'','','','','','','','','','',NULL,NULL),(259,'Виталий','Воропай','',NULL,'','','','','','','','','','',NULL,NULL),(260,'Адрей','Никитин','',NULL,'','','','','','','','','','',NULL,NULL),(261,'Илья','Вамуш','',NULL,'','','','','','','','','','',NULL,NULL),(262,'Петр','Черняк','',NULL,'','','','','','','','','','',NULL,NULL),(263,'Андрей','Трацевский','',NULL,'','','','','','','','','','',NULL,NULL),(264,'Борис','Рукавицын','Николаевич',NULL,'','','','','','','','','','',NULL,NULL),(265,'Сюзанна','Гвадера','',NULL,'','','','','','','','','','','2017-09-07 18:25:19',NULL),(266,'Эвелина','Мартинкевич','',NULL,'f','','','','','','','','','',NULL,NULL),(267,'Владик','Нерсесянц','',NULL,'','','','','','','','','','',NULL,NULL),(268,'Григорий','Фихтенгольц','Михайлович',NULL,'','','','','','','','','','',NULL,NULL),(269,'Огюстен','Коши','',NULL,'','','','','','','','','','',NULL,NULL),(270,'Мария','Кюри','',NULL,'f','','','','','','','','','',NULL,NULL),(271,'Герберт','Шилдт','',NULL,'','','','','','','','','','',NULL,NULL),(272,'Роуэн','Аткинсон','',NULL,'','','','','','','','','','',NULL,NULL),(273,'Эмма','Уотсон','',NULL,'f','','','','','','','','','',NULL,NULL),(274,'Дэниел','Рэдклиф','',NULL,'','','','','','','','','','',NULL,NULL),(275,'Руперт','Гринт','',NULL,'','','','','','','','','','',NULL,NULL),(276,'Рик','Санчез','',NULL,'','','','','','','','','','',NULL,NULL),(277,'Бет','Смит','',NULL,'f','','','','','','','','','',NULL,NULL),(278,'Мелани','Лоран','',NULL,'f','','','','','','','','','',NULL,NULL),(279,'Диана','Крюгер','',NULL,'f','','','','','','','','','',NULL,NULL),(280,'Анна','Азотова','',NULL,'f','','','','','','','','','',NULL,NULL),(281,'Эдди','Мерфи','',NULL,'','','','','','','','','','',NULL,NULL),(282,'Кококо','Кококок','',NULL,'','','','','','','','','','','2017-09-07 20:20:07',NULL),(283,'Вася','Крюшер','',NULL,'','','','','','','','','','','2017-09-07 20:20:34',NULL);
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone_number`
--

DROP TABLE IF EXISTS `phone_number`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone_number` (
  `number_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `country_code` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `operator_code` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `number` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comment` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deletion_date` timestamp NULL DEFAULT NULL,
  `contact_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`number_id`),
  KEY `fk_phone_number_contact` (`contact_id`),
  CONSTRAINT `fk_phone_number_contact` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`contact_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_number`
--

LOCK TABLES `phone_number` WRITE;
/*!40000 ALTER TABLE `phone_number` DISABLE KEYS */;
/*!40000 ALTER TABLE `phone_number` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-08 19:47:43
