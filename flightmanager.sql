-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 17, 2013 at 11:52 AM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `flightmanager`
--

-- --------------------------------------------------------

--
-- Table structure for table `airplanes`
--

CREATE TABLE IF NOT EXISTS `airplanes` (
  `airplaneId` int(11) NOT NULL AUTO_INCREMENT,
  `Brand` varchar(100) CHARACTER SET latin1 NOT NULL,
  `Model` varchar(100) CHARACTER SET latin1 NOT NULL,
  `fCapacity` int(11) NOT NULL,
  `sCapacity` int(11) NOT NULL,
  `tCapacity` int(11) NOT NULL,
  `velocity` int(11) NOT NULL COMMENT 'meters per minute',
  `kilometerCost` int(11) NOT NULL,
  PRIMARY KEY (`airplaneId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=15 ;

--
-- Dumping data for table `airplanes`
--

INSERT INTO `airplanes` (`airplaneId`, `Brand`, `Model`, `fCapacity`, `sCapacity`, `tCapacity`, `velocity`, `kilometerCost`) VALUES
(1, 'Airbus', 'A330-200', 0, 293, 253, 50, 1),
(2, 'Airbus', 'A330-300', 0, 335, 295, 50, 1),
(5, 'Airbus', 'A340-500', 0, 359, 313, 50, 1),
(6, 'Airbus', 'A340-600', 180, 182, 0, 50, 1),
(7, 'Boeing', '737-700', 149, 126, 0, 50, 1),
(8, 'Boeing', '757-200', 180, 182, 0, 50, 1),
(9, 'Boeing', '767-200', 255, 224, 181, 50, 1),
(10, 'Boeing', '767-300 ER', 351, 269, 218, 50, 1),
(11, 'Boeing', '777-200 ER', 440, 400, 301, 50, 1),
(12, 'Airbus', 'A319', 124, 134, 0, 50, 1),
(13, 'Boeing', '737-300', 136, 0, 0, 50, 1),
(14, 'SAAB', '344 B', 136, 0, 0, 50, 1);

-- --------------------------------------------------------

--
-- Table structure for table `airports`
--

CREATE TABLE IF NOT EXISTS `airports` (
  `airportId` int(11) NOT NULL AUTO_INCREMENT,
  `airportName` varchar(300) NOT NULL,
  `cityId` int(11) NOT NULL,
  PRIMARY KEY (`airportId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=19 ;

--
-- Dumping data for table `airports`
--

INSERT INTO `airports` (`airportId`, `airportName`, `cityId`) VALUES
(1, 'Smaland Airport', 1),
(2, 'Palma de Mallorca', 2),
(18, 'Arlanda', 3);

-- --------------------------------------------------------

--
-- Table structure for table `bookedflights`
--

CREATE TABLE IF NOT EXISTS `bookedflights` (
  `bookedFlightId` int(11) NOT NULL AUTO_INCREMENT,
  `flightId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `dateOfBooking` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `totalPrice` int(11) NOT NULL,
  `confirmed` tinyint(4) NOT NULL,
  `cancelled` tinyint(4) NOT NULL,
  PRIMARY KEY (`bookedFlightId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `cities`
--

CREATE TABLE IF NOT EXISTS `cities` (
  `cityId` int(11) NOT NULL AUTO_INCREMENT,
  `cityName` varchar(300) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'city is active',
  PRIMARY KEY (`cityId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `cities`
--

INSERT INTO `cities` (`cityId`, `cityName`, `active`) VALUES
(1, 'Växjö', 1),
(2, 'Mallorca', 1),
(3, 'Stockholm', 1);

-- --------------------------------------------------------

--
-- Table structure for table `flights`
--

CREATE TABLE IF NOT EXISTS `flights` (
  `flightId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'flight id',
  `price` int(11) NOT NULL COMMENT 'ticket price',
  `routeId` int(11) NOT NULL,
  `airplaneId` int(11) NOT NULL,
  `departure` datetime NOT NULL COMMENT 'departure',
  PRIMARY KEY (`flightId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=31 ;

--
-- Dumping data for table `flights`
--

INSERT INTO `flights` (`flightId`, `price`, `routeId`, `airplaneId`, `departure`) VALUES
(2, 3, 11, 5, '2013-05-16 09:29:10'),
(3, 12, 11, 2, '2013-05-15 09:27:52'),
(4, 12, 11, 6, '2013-05-15 12:27:52'),
(5, 12, 11, 6, '2013-05-15 14:27:52'),
(6, 12, 11, 6, '2013-05-15 16:27:52'),
(7, 12, 11, 6, '2013-05-15 18:27:52'),
(8, 12, 11, 6, '2013-05-15 01:27:52'),
(9, 12, 11, 6, '2013-05-15 22:27:52'),
(10, 12, 11, 6, '2013-05-15 19:27:52'),
(11, 14, 9, 5, '2013-05-18 15:20:00'),
(12, 14, 9, 5, '2013-05-19 15:20:00'),
(13, 14, 9, 5, '2013-05-20 15:20:00'),
(14, 14, 9, 5, '2013-05-21 15:20:00'),
(15, 14, 9, 5, '2013-05-22 15:20:00'),
(16, 14, 12, 5, '2013-05-18 15:20:00'),
(17, 14, 12, 5, '2013-05-19 15:20:00'),
(18, 14, 12, 5, '2013-05-20 15:20:00'),
(19, 14, 12, 5, '2013-05-21 15:20:00'),
(20, 14, 12, 5, '2013-05-22 15:20:00'),
(21, 14, 9, 5, '2013-05-18 15:20:00'),
(22, 14, 9, 5, '2013-05-19 15:20:00'),
(23, 14, 9, 5, '2013-05-20 15:20:00'),
(24, 14, 9, 5, '2013-05-21 15:20:00'),
(25, 14, 9, 5, '2013-05-22 15:20:00'),
(26, 14, 12, 5, '2013-05-18 15:20:00'),
(27, 14, 12, 5, '2013-05-19 15:20:00'),
(28, 14, 12, 5, '2013-05-20 15:20:00'),
(29, 14, 12, 5, '2013-05-21 15:20:00'),
(30, 14, 12, 5, '2013-05-22 15:20:00');

-- --------------------------------------------------------

--
-- Table structure for table `routes`
--

CREATE TABLE IF NOT EXISTS `routes` (
  `routeId` int(11) NOT NULL AUTO_INCREMENT,
  `startAirport` int(11) NOT NULL,
  `destinationAirport` int(11) NOT NULL,
  `distance` int(11) NOT NULL COMMENT 'in meters',
  PRIMARY KEY (`routeId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13 ;

--
-- Dumping data for table `routes`
--

INSERT INTO `routes` (`routeId`, `startAirport`, `destinationAirport`, `distance`) VALUES
(9, 1, 18, 50000),
(11, 1, 2, 50000),
(12, 2, 1, 500000);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET latin1 NOT NULL,
  `password` varchar(255) CHARACTER SET latin1 NOT NULL,
  `userRank` int(11) NOT NULL,
  `email` varchar(255) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=25 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `userRank`, `email`) VALUES
(23, 'admin', 'admin', 1, "admin@flightmanager.com"),
(24, 'user', 'user', 2, "user@usermail.com");

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
