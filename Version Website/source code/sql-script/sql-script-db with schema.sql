-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema garage
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `garage` ;

-- -----------------------------------------------------
-- Schema garage
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `garage` DEFAULT CHARACTER SET utf8 ;
USE `garage` ;

-- -----------------------------------------------------
-- Table `garage`.`type_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`type_user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(5) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`engine_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`engine_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(10) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`type_car`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`type_car` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`user` (
  `email` VARCHAR(50) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `mobilePhone` VARCHAR(20) NULL,
  `type_user_id` INT NOT NULL,
  `plateNumber` VARCHAR(15) NULL,
  `make` VARCHAR(30) NULL,
  `engine_type_id` INT NOT NULL,
  `type_car_id` INT NOT NULL,
  PRIMARY KEY (`email`),
  INDEX `fk_user_type_user1_idx` (`type_user_id` ASC) VISIBLE,
  INDEX `fk_user_engine_type1_idx` (`engine_type_id` ASC) VISIBLE,
  INDEX `fk_user_type_car1_idx` (`type_car_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_type_user1`
    FOREIGN KEY (`type_user_id`)
    REFERENCES `garage`.`type_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_engine_type1`
    FOREIGN KEY (`engine_type_id`)
    REFERENCES `garage`.`engine_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_type_car1`
    FOREIGN KEY (`type_car_id`)
    REFERENCES `garage`.`type_car` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`Mechanic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`Mechanic` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(12) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`type_app`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`type_app` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(14) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`appoinment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`appoinment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_email` VARCHAR(50) NOT NULL,
  `date` DATE NULL,
  `comment` VARCHAR(200) NULL,
  `status_id` INT NOT NULL,
  `type_app_id` INT NOT NULL,
  PRIMARY KEY (`id`, `user_email`),
  INDEX `fk_Appoinment_status1_idx` (`status_id` ASC) VISIBLE,
  INDEX `fk_Appoinment_type_app1_idx` (`type_app_id` ASC) VISIBLE,
  CONSTRAINT `fk_Appoinments_user1`
    FOREIGN KEY (`user_email`)
    REFERENCES `garage`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Appoinment_status1`
    FOREIGN KEY (`status_id`)
    REFERENCES `garage`.`status` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Appoinment_type_app1`
    FOREIGN KEY (`type_app_id`)
    REFERENCES `garage`.`type_app` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`cost`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`cost` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'It could be the invoice number',
  `Appoinment_user_email` VARCHAR(50) NOT NULL,
  `Appoinment_id` INT NOT NULL,
  `total` DECIMAL(15,2) NOT NULL,
  `currency` VARCHAR(3) NOT NULL,
  `date` DATETIME NOT NULL,
  PRIMARY KEY (`id`, `Appoinment_user_email`, `Appoinment_id`),
  CONSTRAINT `fk_Cost_Appoinment1`
    FOREIGN KEY (`Appoinment_user_email` , `Appoinment_id`)
    REFERENCES `garage`.`appoinment` (`user_email` , `id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`item` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `price` DECIMAL(15,2) NULL,
  `currency` VARCHAR(3) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`detail` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Cost_Appoinment_user_email` VARCHAR(50) NOT NULL,
  `Cost_Appoinment_id` INT NOT NULL,
  `Cost_id` INT NOT NULL,
  `amount` DECIMAL(15,2) NULL,
  `task` VARCHAR(50) NULL,
  `Item_id` INT NULL,
  PRIMARY KEY (`id`, `Cost_Appoinment_user_email`, `Cost_Appoinment_id`, `Cost_id`),
  INDEX `fk_Detail_Item1_idx` (`Item_id` ASC) VISIBLE,
  CONSTRAINT `fk_Detail_Cost1`
    FOREIGN KEY (`Cost_Appoinment_user_email` , `Cost_Appoinment_id` , `Cost_id`)
    REFERENCES `garage`.`cost` (`Appoinment_user_email` , `Appoinment_id` , `id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Detail_Item1`
    FOREIGN KEY (`Item_id`)
    REFERENCES `garage`.`item` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`make`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`make` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `make` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`shift`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`shift` (
  `id` INT NOT NULL,
  `time` TIME NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`appoinment_has_shift`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`appoinment_has_shift` (
  `appoinment_id` INT NOT NULL,
  `appoinment_user_email` VARCHAR(50) NOT NULL,
  `shift_id` INT NOT NULL,
  PRIMARY KEY (`appoinment_id`, `appoinment_user_email`, `shift_id`),
  INDEX `fk_appoinment_has_shift_shift1_idx` (`shift_id` ASC) VISIBLE,
  INDEX `fk_appoinment_has_shift_appoinment1_idx` (`appoinment_id` ASC, `appoinment_user_email` ASC) VISIBLE,
  CONSTRAINT `fk_appoinment_has_shift_appoinment1`
    FOREIGN KEY (`appoinment_id` , `appoinment_user_email`)
    REFERENCES `garage`.`appoinment` (`id` , `user_email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_appoinment_has_shift_shift1`
    FOREIGN KEY (`shift_id`)
    REFERENCES `garage`.`shift` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `garage`.`shift_has_Mechanic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `garage`.`shift_has_Mechanic` (
  `shift_id` INT NOT NULL,
  `Mechanic_id` INT NOT NULL,
  PRIMARY KEY (`shift_id`, `Mechanic_id`),
  INDEX `fk_shift_has_Mechanic_Mechanic1_idx` (`Mechanic_id` ASC) VISIBLE,
  INDEX `fk_shift_has_Mechanic_shift1_idx` (`shift_id` ASC) VISIBLE,
  CONSTRAINT `fk_shift_has_Mechanic_shift1`
    FOREIGN KEY (`shift_id`)
    REFERENCES `garage`.`shift` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_shift_has_Mechanic_Mechanic1`
    FOREIGN KEY (`Mechanic_id`)
    REFERENCES `garage`.`Mechanic` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `garage`.`type_user`
-- -----------------------------------------------------
START TRANSACTION;
USE `garage`;
INSERT INTO `garage`.`type_user` (`id`, `type`) VALUES (1, 'admin');
INSERT INTO `garage`.`type_user` (`id`, `type`) VALUES (2, 'user');

COMMIT;


-- -----------------------------------------------------
-- Data for table `garage`.`engine_type`
-- -----------------------------------------------------
START TRANSACTION;
USE `garage`;
INSERT INTO `garage`.`engine_type` (`id`, `type`) VALUES (1, 'diesel');
INSERT INTO `garage`.`engine_type` (`id`, `type`) VALUES (2, 'petrol');
INSERT INTO `garage`.`engine_type` (`id`, `type`) VALUES (3, 'hybrid');
INSERT INTO `garage`.`engine_type` (`id`, `type`) VALUES (4, 'electric');

COMMIT;


-- -----------------------------------------------------
-- Data for table `garage`.`type_car`
-- -----------------------------------------------------
START TRANSACTION;
USE `garage`;
INSERT INTO `garage`.`type_car` (`id`, `type`) VALUES (1, 'motorbike');
INSERT INTO `garage`.`type_car` (`id`, `type`) VALUES (2, 'car');
INSERT INTO `garage`.`type_car` (`id`, `type`) VALUES (3, 'small van');
INSERT INTO `garage`.`type_car` (`id`, `type`) VALUES (4, 'small bus');

COMMIT;


-- -----------------------------------------------------
-- Data for table `garage`.`status`
-- -----------------------------------------------------
START TRANSACTION;
USE `garage`;
INSERT INTO `garage`.`status` (`id`, `status`) VALUES (1, 'booked');
INSERT INTO `garage`.`status` (`id`, `status`) VALUES (2, 'in service');
INSERT INTO `garage`.`status` (`id`, `status`) VALUES (3, 'fixed');
INSERT INTO `garage`.`status` (`id`, `status`) VALUES (4, 'collected');
INSERT INTO `garage`.`status` (`id`, `status`) VALUES (5, 'unrepairable');

COMMIT;


-- -----------------------------------------------------
-- Data for table `garage`.`type_app`
-- -----------------------------------------------------
START TRANSACTION;
USE `garage`;
INSERT INTO `garage`.`type_app` (`id`, `type`) VALUES (1, 'annual service');
INSERT INTO `garage`.`type_app` (`id`, `type`) VALUES (2, 'major service');
INSERT INTO `garage`.`type_app` (`id`, `type`) VALUES (3, 'repair');
INSERT INTO `garage`.`type_app` (`id`, `type`) VALUES (4, 'major repair');

COMMIT;


-- -----------------------------------------------------
-- Data for table `garage`.`item`
-- -----------------------------------------------------
START TRANSACTION;
USE `garage`;
INSERT INTO `garage`.`item` (`id`, `name`, `price`, `currency`) VALUES (1, 'wing mirror', 100, 'EUR');
INSERT INTO `garage`.`item` (`id`, `name`, `price`, `currency`) VALUES (2, 'tyre', 200, 'EUR');
INSERT INTO `garage`.`item` (`id`, `name`, `price`, `currency`) VALUES (3, 'exhaust pipe', 500, 'EUR');
INSERT INTO `garage`.`item` (`id`, `name`, `price`, `currency`) VALUES (4, 'reflector', 50, 'EUR');
INSERT INTO `garage`.`item` (`id`, `name`, `price`, `currency`) VALUES (5, 'Seat', 900, 'EUR');

COMMIT;


-- -----------------------------------------------------
-- Data for table `garage`.`make`
-- -----------------------------------------------------
START TRANSACTION;
USE `garage`;
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (1, 'Volkswagen');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (2, 'BMW');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (3, 'Audi');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (4, 'Mitsubishi');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (5, 'Ford');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (6, 'Toyota');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (7, 'Maserati');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (8, 'Honda');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (9, 'Subaru');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (10, 'Hyundai');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (11, '	Power Vehicle Innovation');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (12, 'Ikarus');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (13, 'Fiat');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (14, 'Alfa Romeo');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (15, 'Volvo');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (16, 'Nissan');
INSERT INTO `garage`.`make` (`id`, `make`) VALUES (17, 'Other');

COMMIT;


-- -----------------------------------------------------
-- Data for table `garage`.`shift`
-- -----------------------------------------------------
START TRANSACTION;
USE `garage`;
INSERT INTO `garage`.`shift` (`id`, `time`, `description`) VALUES (1, '08:00:00', 'Morning 1');
INSERT INTO `garage`.`shift` (`id`, `time`, `description`) VALUES (2, '10:00:00', 'Morning 2');
INSERT INTO `garage`.`shift` (`id`, `time`, `description`) VALUES (3, '13:00:00', 'Afternoon 1');
INSERT INTO `garage`.`shift` (`id`, `time`, `description`) VALUES (4, '15:00:00', 'Afternoon 2');

COMMIT;

