SET NAMES utf8mb4;

# DB 생성
DROP DATABASE IF EXISTS site22;
CREATE DATABASE site22;
USE site22;

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

# 게시물 댓글
CREATE TABLE articleReply (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    cateItemId INT(10) UNSIGNED NOT NULL,
    articleId INT(10) UNSIGNED NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    `body` TEXT NOT NULL
);

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

# 회원
CREATE TABLE `member` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(255) NOT NULL , 
    loginPw CHAR(100) NOT NULL,
    `name` CHAR(100) NOT NULL,
    nickname CHAR(100) NOT NULL,
    email CHAR(255) NOT NULL
);

# 테스트 쿼리 스트링
DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS articleReply;
DROP TABLE IF EXISTS cateItem;
DROP TABLE IF EXISTS `member`;

TRUNCATE TABLE article;
TRUNCATE TABLE articleReply;
TRUNCATE TABLE cateItem;
TRUNCATE TABLE `member`;

SELECT * FROM article;
SELECT * FROM articleReply;
SELECT * FROM cateItem;
SELECT * FROM `member`;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
cateItemId = 1,
displayStatus = 1,
title = '제목',
`body` = '내용'; 