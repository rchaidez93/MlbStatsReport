CREATE DATABASE `TestingTable` /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE TABLE `mlb_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `report_date` int(11) unsigned NOT NULL,
  `status` int(8) unsigned NOT NULL,
  `email_sent` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `mlb_stats` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `epoch_date` int(11) NOT NULL,
  `team1` varchar(45) NOT NULL,
  `team2` varchar(45) NOT NULL,
  `score1` int(11) NOT NULL,
  `score2` int(11) NOT NULL,
  `winner` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110163 DEFAULT CHARSET=latin1;
CREATE TABLE `Users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
