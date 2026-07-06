/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.46 : Database - homestay_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`homestay_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `homestay_db`;

/*Table structure for table `order_info` */

DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `order_id` varchar(36) NOT NULL COMMENT '订单编号',
  `room_id` bigint NOT NULL COMMENT '房间id',
  `room_name` varchar(50) NOT NULL COMMENT '房间名称',
  `room_img` varchar(100) DEFAULT NULL COMMENT '房间图片',
  `start_time` varchar(20) NOT NULL COMMENT '入住日期',
  `end_time` varchar(20) NOT NULL COMMENT '退房日期',
  `room_count` int NOT NULL COMMENT '房间数量',
  `people_count` int NOT NULL COMMENT '入住人数',
  `contact_phone` varchar(11) NOT NULL COMMENT '预留手机号',
  `total_price` int NOT NULL COMMENT '总价',
  `pay_method` varchar(20) DEFAULT NULL COMMENT '支付方式 wechat/alipay/cash',
  `pay_status` tinyint DEFAULT '0' COMMENT '支付状态0未付1已付',
  `order_status` tinyint DEFAULT '0' COMMENT '订单状态 0待入住 1已入住 2需打扫 3已取消 4已完成',
  `create_time` varchar(30) DEFAULT NULL COMMENT '下单时间',
  `user_id` varchar(36) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL COMMENT '客户姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `order_info` */

insert  into `order_info`(`id`,`order_id`,`room_id`,`room_name`,`room_img`,`start_time`,`end_time`,`room_count`,`people_count`,`contact_phone`,`total_price`,`pay_method`,`pay_status`,`order_status`,`create_time`,`user_id`,`user_name`) values 
(1,'QS1780474959867',210,'海景露台房','lutai.png','2026-06-03','2026-06-07',1,1,'12345678901',1992,'alipay',1,4,'2026/6/3 16:22:39','9ed071ce5f2111f183582c7ba0d8d163',NULL),
(2,'QS1780475248373',207,'日式榻榻米房','tatami.png','2026-06-03','2026-06-07',1,1,'12345647897',1192,'wechat',1,3,'2026/6/3 16:27:28','e6f433ce5f2511f183582c7ba0d8d163',NULL),
(3,'QS1780475552208',210,'海景露台房','lutai.png','2026-06-14','2026-06-21',1,1,'12345678901',3486,'wechat',1,1,'2026/6/3 16:32:32','e6f433ce5f2511f183582c7ba0d8d163','张三'),
(4,'QS1780546541364',301,'露台观海大床房','/upload/room/8d3224c2-ec6c-431f-b73f-74667e6124dd.png','2026-06-04','2026-06-13',1,1,'12345667899',5112,'alipay',1,4,'2026/6/4 12:15:41','e6f433ce5f2511f183582c7ba0d8d163','曹'),
(5,'QS1780555685189',208,'轻奢套房','qinshe.png','2026-06-20','2026-06-23',1,1,'12345678901',1794,'alipay',1,1,'2026/6/4 14:48:05','9ed071ce5f2111f183582c7ba0d8d163','张三'),
(6,'QS1780555773438',205,'复式loft房','loft.png','2026-06-25','2026-06-27',1,1,'12312312312',916,'cash',0,0,'2026/6/4 14:49:33','6e54b1e95fe111f183582c7ba0d8d163','李四'),
(7,'QS1780555869331',209,'花园观景房','huayuan.png','2026-07-01','2026-06-07',1,1,'12345645645',0,'alipay',1,4,'2026/6/4 14:51:09','a183bef45fe111f183582c7ba0d8d163','王五'),
(8,'QS1780555978746',208,'轻奢套房','qinshe.png','2026-06-30','2026-07-03',1,1,'12378978978',1794,'wechat',1,4,'2026/6/4 14:52:58','de83192b5fe111f183582c7ba0d8d163','赵六'),
(9,'ORDER1780619661772',106,'海景大床房','haijin.png','2026-06-05','2026-06-07',1,1,'17283988594',856,'cash',0,4,'2026-06-05 08:34:21','1','王'),
(10,'QS1780627334928',207,'日式榻榻米房','tatami.png','2026-06-28','2026-06-30',1,1,'12345678901',596,'alipay',1,0,'2026/6/5 10:42:14','de83192b5fe111f183582c7ba0d8d163',NULL),
(11,'QS1780642627971',204,'亲子房','qinzi.png','2026-06-21','2026-06-24',1,1,'12312312312',1194,'wechat',1,0,'2026/6/5 14:57:07','202610001',NULL),
(12,'QS1780730089398',210,'海景露台房','lutai.png','2026-06-13','2026-06-16',1,1,'12378994566',1494,'alipay',1,4,'2026/6/6 15:14:49','202610008',NULL),
(13,'ORDER1780732086114',301,'露台观海大床房','/upload/room/8d3224c2-ec6c-431f-b73f-74667e6124dd.png','2026-06-07','2026-06-09',1,1,'15698523654',1136,'cash',0,4,'2026-06-06 15:48:06','202610008','杨'),
(14,'ORDER1780732086114',301,'露台观海大床房','/upload/room/8d3224c2-ec6c-431f-b73f-74667e6124dd.png','2026-06-07','2026-06-09',1,1,'15698523654',1136,'cash',0,3,'2026-06-06 15:48:06','202610008','杨'),
(15,'ORDER1780964835028',103,'庭院大床房','tingyuan.png','2026-06-13','2026-06-16',1,1,'15678542563',1074,'cash',0,0,'2026-06-09 08:27:15','202610008','王'),
(16,'QS1780975639149',208,'轻奢套房','qinshe.png','2026-06-09','2026-06-11',1,1,'12345678901',1196,'alipay',1,1,'2026/6/9 11:27:19','202610008',NULL),
(17,'ORDER1781052687893',107,'日式榻榻米房','tatami.png','2026-06-11','2026-06-13',1,1,'15698452503',596,'cash',0,1,'2026-06-10 08:51:27','2','曹'),
(18,'QS1781052787589',206,'海景大床房','haijin.png','2026-06-25','2026-06-27',1,1,'15998562356',856,'alipay',1,4,'2026/6/10 08:53:07','202610007',NULL),
(19,'QS1781052882373',208,'轻奢套房','qinshe.png','2026-07-02','2026-07-06',1,1,'14725883699',2392,'wechat',1,0,'2026/6/10 08:54:42','202610006',NULL),
(20,'QS1781054993968',302,'云顶 360° 全景阁','/upload/room/3a97d448-1eee-4592-964d-edb07c66f3d7.png','2026-06-11','2026-06-13',1,1,'12345667899',1536,'alipay',1,3,'2026/6/10 09:29:53','202610009',NULL),
(21,'QS1781055027712',301,'露台观海大床房','/upload/room/8d3224c2-ec6c-431f-b73f-74667e6124dd.png','2026-06-10','2026-06-11',1,1,'12345667899',568,'wechat',1,1,'2026/6/10 09:30:27','202610009',NULL),
(22,'QS1781056655629',301,'露台观海大床房','/upload/room/8d3224c2-ec6c-431f-b73f-74667e6124dd.png','2026-06-10','2026-06-13',1,1,'12345667899',1704,'alipay',1,3,'2026/6/10 09:57:35','202610009',NULL),
(23,'QS1781056864207',203,'庭院大床房','tingyuan.png','2026-06-10','2026-06-12',1,1,'12345678901',716,'wechat',1,3,'2026/6/10 10:01:04','202610010',NULL),
(24,'QS1781056896219',210,'海景露台房','lutai.png','2026-06-11','2026-06-13',1,1,'12345678901',996,'alipay',1,4,'2026/6/10 10:01:36','202610010',NULL),
(25,'QS1781057635941',302,'云顶 360° 全景阁','/upload/room/3a97d448-1eee-4592-964d-edb07c66f3d7.png','2026-06-19','2026-06-21',1,1,'12345667899',1536,'alipay',1,3,'2026/6/10 10:13:55','202610007',NULL),
(26,'ORDER1781057883638',204,'亲子房','qinzi.png','2026-06-19','2026-06-21',1,1,'15698754268',796,'cash',0,0,'2026-06-10 10:18:03','20261002','王'),
(27,'QS1781058834320',209,'花园观景房','huayuan.png','2026-06-10','2026-06-12',1,1,'12345678901',996,'alipay',1,0,'2026/6/10 10:33:54','202610010',NULL),
(28,'ORDER1781059020965',302,'云顶 360° 全景阁','/upload/room/3a97d448-1eee-4592-964d-edb07c66f3d7.png','2026-06-11','2026-06-13',1,1,'15698425637',1536,'cash',0,0,'2026-06-10 10:37:00','20261002','王');

/*Table structure for table `room` */

DROP TABLE IF EXISTS `room`;

CREATE TABLE `room` (
  `room_id` int NOT NULL,
  `room_name` varchar(50) NOT NULL COMMENT '房间名称',
  `price` int NOT NULL COMMENT '价格/晚',
  `img` varchar(100) NOT NULL COMMENT '图片名称',
  `desc` varchar(500) DEFAULT NULL COMMENT '房间描述',
  `category_id` varchar(32) DEFAULT NULL COMMENT '分类ID',
  `room_type` varchar(50) DEFAULT NULL COMMENT '房型',
  `status` tinyint DEFAULT '1' COMMENT '1上架 0下架',
  `room_status` varchar(20) DEFAULT 'empty' COMMENT 'empty空闲 booked已预订 occupied已入住',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签',
  `code` varchar(50) DEFAULT NULL COMMENT '房间编号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `room` */

insert  into `room`(`room_id`,`room_name`,`price`,`img`,`desc`,`category_id`,`room_type`,`status`,`room_status`,`tags`,`code`,`create_time`) values 
(101,'山景大床房',368,'dachuang.png','背靠青山，推窗见绿',NULL,'标准房型',1,NULL,NULL,NULL,'2026-05-29 20:23:16'),
(102,'温馨双床房',288,'shuangchuang.png','简约温馨，干净舒适',NULL,'普通房型',1,NULL,NULL,NULL,'2026-05-26 16:14:07'),
(103,'庭院大床房',358,'tingyuan.png','独立小庭院，安静惬意',NULL,'标准房型',1,'booked',NULL,NULL,'2026-05-26 16:14:07'),
(104,'亲子房',398,'qinzi.png','童趣主题，适合家庭入住',NULL,'family',1,'booked',NULL,NULL,'2026-05-26 16:14:07'),
(105,'复式loft房',458,'loft.png','上下两层，空间宽敞',NULL,'loft',1,'booked',NULL,NULL,'2026-05-26 16:14:07'),
(106,'海景大床房',428,'haijin.png','一线海景，视野开阔',NULL,'标准房型',1,'booked',NULL,NULL,'2026-05-26 16:14:07'),
(107,'日式榻榻米房',298,'tatami.png','原木和风，简约舒适',NULL,'标准房型',1,'occupied',NULL,NULL,'2026-05-26 16:14:07'),
(108,'轻奢套房',598,'qinshe.png','轻奢装修，高端体验',NULL,'套房',1,'occupied',NULL,NULL,'2026-05-26 16:14:07'),
(109,'花园观景房',498,'huayuan.png','花园景观，环境优美',NULL,'标准房型',1,'empty',NULL,NULL,'2026-05-26 16:14:07'),
(110,'海景露台房',498,'lutai.png','超大露台，海景一览无余',NULL,'标准房型',1,'empty',NULL,NULL,'2026-05-26 16:14:07'),
(201,'山景大床房',328,'dachuang.png','背靠青山，推窗见绿',NULL,'标准房型',1,'empty',NULL,NULL,'2026-05-27 15:20:56'),
(202,'温馨双床房',268,'shuangchuang.png','简约温馨，干净舒适',NULL,'普通房型',1,'empty',NULL,NULL,'2026-05-27 15:20:56'),
(203,'庭院大床房',358,'tingyuan.png','独立小庭院，安静惬意',NULL,'标准房型',1,'empty',NULL,NULL,'2026-05-27 15:20:56'),
(204,'亲子房',398,'qinzi.png','童趣主题，适合家庭入住',NULL,'family',1,'empty',NULL,NULL,'2026-05-27 15:20:56'),
(205,'复式loft房',458,'loft.png','上下两层，空间宽敞',NULL,'loft',1,'empty',NULL,NULL,'2026-05-27 15:20:56'),
(206,'海景大床房',428,'haijin.png','一线海景，视野开阔',NULL,'标准房型',1,'empty',NULL,NULL,'2026-05-27 15:20:56'),
(207,'日式榻榻米房',298,'tatami.png','原木和风，简约舒适',NULL,'标准房型',1,'empty',NULL,NULL,'2026-05-27 15:20:56'),
(208,'轻奢套房',598,'qinshe.png','轻奢装修，高端体验',NULL,'套房',1,'empty',NULL,NULL,'2026-05-27 15:20:56'),
(209,'花园观景房',498,'huayuan.png','花园景观，环境优美',NULL,'标准房型',1,'empty',NULL,NULL,'2026-05-27 15:20:56'),
(301,'露台观海大床房',568,'/upload/room/8d3224c2-ec6c-431f-b73f-74667e6124dd.png','自带露天观景露台，晨起看海',NULL,'大床房',1,'empty',NULL,NULL,'2026-06-04 12:11:36'),
(302,'云顶 360° 全景阁',768,'/upload/room/3a97d448-1eee-4592-964d-edb07c66f3d7.png','高空视野，尽览天地壮阔。',NULL,'套房',1,NULL,NULL,NULL,'2026-06-10 09:03:26'),
(304,'星空穹顶套房',588,'/upload/room/ddefd432-f90b-41e7-bc95-650a7129cca0.png','卧看星河，璀璨夜空伴您入睡。',NULL,'套房',1,'empty',NULL,NULL,'2026-06-10 10:16:55'),
(306,'海滩房',566,'/upload/room/ce827bc4-bc41-4cbd-b651-5188fcfca4b2.png','出门见沙滩，空间开阔。',NULL,'套房',1,'empty',NULL,NULL,'2026-06-10 10:35:55');

/*Table structure for table `staff` */

DROP TABLE IF EXISTS `staff`;

CREATE TABLE `staff` (
  `staff_id` int NOT NULL AUTO_INCREMENT,
  `staff_name` varchar(50) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` varchar(20) DEFAULT 'staff',
  `position` varchar(30) DEFAULT '普通员工',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`staff_id`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=20261014 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `staff` */

insert  into `staff`(`staff_id`,`staff_name`,`phone`,`password`,`role`,`position`,`create_time`) values 
(20261002,'胡','12345678912','111111','staff','店长','2026-06-10 08:48:55'),
(20261003,'杨','12378994566','111111','staff','管理员','2026-06-10 08:49:20'),
(20261004,'木白','12378945698','111111','staff','前台','2026-06-10 08:49:45'),
(20261005,'晓丽','12378965456','111111','staff','保洁','2026-06-10 08:50:15'),
(20261006,'曹','12345667899','123456','staff','管理员','2026-06-10 10:05:14'),
(20261013,'何','12345678901','111111','staff','管理员','2026-06-10 10:37:30');

/*Table structure for table `t_evaluate` */

DROP TABLE IF EXISTS `t_evaluate`;

CREATE TABLE `t_evaluate` (
  `eva_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `score` int NOT NULL,
  `content` text NOT NULL,
  `eva_img` varchar(255) DEFAULT NULL,
  `eva_time` datetime NOT NULL,
  `room_name` varchar(50) DEFAULT NULL COMMENT '评价房型名称',
  `order_id` varchar(36) DEFAULT NULL COMMENT '订单编号',
  `user_name` varchar(50) DEFAULT NULL COMMENT '评价用户姓名',
  `reply` text COMMENT '商家回复',
  PRIMARY KEY (`eva_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `t_evaluate` */

insert  into `t_evaluate`(`eva_id`,`user_id`,`score`,`content`,`eva_img`,`eva_time`,`room_name`,`order_id`,`user_name`,`reply`) values 
('1119023c82b4458da1e5f8f7f6c85f0e','202610010',5,'环境舒适。','http://localhost:8080/ssm_springmvc/upload/1781059115003.png','2026-06-10 10:38:35','海景露台房','QS1781056896219','刘六','感谢入住。'),
('7ebfc6bde33f4a99b58557de3c81de2b','202610007',5,'环境舒适，下次再来。','http://localhost:8080/ssm_springmvc/upload/1781057654042.png','2026-06-10 10:14:27','海景大床房','QS1781052787589','何','感谢您的入住，期待您的下次光临！'),
('d8f08ddc429d432a9b4bc8d035c4a195','1',5,'前台服务周到，环境舒适，无打扰，下次还会再来的。','http://localhost:8080/ssm_springmvc/upload/1780640505569.png','2026-06-05 14:21:46','海景大床房','ORDER1780619661772','胡','感谢您的好评。'),
('de082ea8a84043bdbd365da0c0828494','de83192b5fe111f183582c7ba0d8d163',5,'环境舒适，靠海','http://localhost:8080/ssm_springmvc/upload/1780622332923.png','2026-06-05 09:19:03','轻奢套房',NULL,'赵六','感谢您的入住，期待您的下次光临~');

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `user_id` varchar(36) NOT NULL COMMENT '用户主键UUID',
  `username` varchar(30) NOT NULL COMMENT '姓名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `phone` varchar(11) NOT NULL COMMENT '手机号',
  `role` varchar(10) DEFAULT 'user' COMMENT '角色 user顾客/admin管理员',
  `avatar` varchar(100) DEFAULT 'images/avatar.png' COMMENT '头像地址',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `t_user` */

insert  into `t_user`(`user_id`,`username`,`password`,`phone`,`role`,`avatar`) values 
('202610004','赵六','456456','12378978978','user','images/avatar.png'),
('202610005','曹','111111','12345647897','user','images/avatar.png'),
('202610006','李','123456','14725883699','user','images/avatar.png'),
('202610007','何','000000','12345667899','user','images/avatar.png'),
('202610008','陈','000000','12378994566','user','images/avatar.png'),
('202610009','李一','111111','12345667899','user','images/avatar.png'),
('202610010','刘六','111111','12345678901','user','images/avatar.png'),
('202610011','李斯','123456','12345667899','user','images/avatar.png'),
('202610012','吴一','111111','12345678901','user','images/avatar.png'),
('202610013','王','111111','12345678901','user','images/avatar.png');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
