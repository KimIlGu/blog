# 캐릭터SET 설정
SET NAMES utf8mb4;

# DB 생성
DROP DATABASE IF EXISTS site22;
CREATE DATABASE site22;
USE site22;

DROP TABLE IF EXISTS article
# 게시물 
CREATE TABLE article (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    cateItemId INT(10) UNSIGNED NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    displayStatus TINYINT(1) UNSIGNED NOT NULL,
    `title` CHAR(200) NOT NULL,
    `body` TEXT NOT NULL,
    hit  INT(255) UNSIGNED NOT NULL
);

DROP TABLE IF EXISTS articleReply
# 게시물 댓글
CREATE TABLE articleReply (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    displayStatus TINYINT(1) UNSIGNED NOT NULL,
    articleId INT(10) UNSIGNED NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    `body` TEXT NOT NULL
);

## 특정 조건을 만족하는 회원 또는 게시물(기타 데이터)를 빠르게 찾기 위해서
ALTER TABLE `articleReply` ADD INDEX (`relTypeCode`, `relId`); 

DROP TABLE IF EXISTS cateItem
# 카테고리
CREATE TABLE cateItem (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    `name` CHAR(100) NOT NULL UNIQUE
);

# 카테고리 아이템
INSERT INTO cateItem SET regDate = NOW(), `name` = '자격증';
INSERT INTO cateItem SET regDate = NOW(), `name` = '개인관심';
INSERT INTO cateItem SET regDate = NOW(), `name` = '웹 개발';
INSERT INTO cateItem SET regDate = NOW(), `name` = 'DATABASE';
INSERT INTO cateItem SET regDate = NOW(), `name` = 'Linux';
INSERT INTO cateItem SET regDate = NOW(), `name` = '개발도구';

DROP TABLE IF EXISTS `member`
# 회원
CREATE TABLE `member` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(255) NOT NULL , 
    loginPw CHAR(100) NOT NULL,
    `name` CHAR(100) NOT NULL,
    nickname CHAR(100) NOT NULL,
    email CHAR(255) NOT NULL,
    `level` INT(10) UNSIGNED NOT NULL DEFAULT 0,
    `mailAuthStatus` INT(10) UNSIGNED NOT NULL
);

# attr(부가정보) 테이블 추가
DROP TABLE IF EXISTS attr;
CREATE TABLE attr (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    `name` CHAR(100) NOT NULL UNIQUE,
    `value` TEXT NOT NULL
);

# attr 테이블에서 name 을 4가지 칼럼으로 나누기
ALTER TABLE `attr` DROP COLUMN `name`,
ADD COLUMN `relTypeCode` CHAR(20) NOT NULL AFTER `updateDate`,
ADD COLUMN `relId` INT(10) UNSIGNED NOT NULL AFTER `relTypeCode`,
ADD COLUMN `typeCode` CHAR(30) NOT NULL AFTER `relId`,
ADD COLUMN `type2Code` CHAR(30) NOT NULL AFTER `typeCode`,
CHANGE `value` `value` TEXT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL AFTER `type2Code`,
DROP INDEX `name`; 

# attr 유니크 인덱스 걸기
## 중복변수 생성금지
## 변수찾는 속도 최적화
ALTER TABLE `attr` ADD UNIQUE INDEX (`relTypeCode`, `relId`, `typeCode`, `type2Code`); 

## 특정 조건을 만족하는 회원 또는 게시물(기타 데이터)를 빠르게 찾기 위해서
ALTER TABLE `attr` ADD INDEX (`relTypeCode`, `typeCode`, `type2Code`); 

SELECT * FROM `member`  
TRUNCATE `member`

SELECT * FROM `article`
TRUNCATE `article`

SELECT * FROM `articleReply`
TRUNCATE `articleReply`

TRUNCATE TABLE cateItem;
SELECT * FROM cateItem;

SELECT * FROM `attr`
TRUNCATE `attr`

INSERT INTO `member`
SET
loginId = "user1",
loginPW = "0a041b9462caa4a31bac3567e0b6e6fd9100787db2ab433d96f6d178cabfce90",
`name` = "user1",
`nickname` = "user1",
`email` = "email",
`level` = 0,
`mailAuthStatus` = 1;
