# Дамп таблицы roles
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
                         `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                         `role` varchar(250) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `id` (`id`),
                         UNIQUE KEY `role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `roles` (`id`, `role`)
VALUES
(1,'ROLE_ADMIN'),
(2,'ROLE_USER'),
(3,'ROLE_GUEST');

# Дамп таблицы users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
                         `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                         `username` varchar(250) DEFAULT NULL,
                         `password` varchar(250) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `id` (`id`),
                         UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `users` (`id`, `username`, `password`)
VALUES
(1,'admin','$2a$10$xuqcvgAaTCHIcS9bXwbH0eE2HcQ6Mpywp83Uq9ZnNgRxkJZRrP6sy'),
(2,'user','$2a$10$3iHwjMIUN6v6kXb7BLndP.xZ4qVulx3Nkkxkq5Qo3renHVifTc6fa');

# Дамп таблицы users_roles
# ------------------------------------------------------------

DROP TABLE IF EXISTS `users_roles`;

CREATE TABLE `users_roles` (
                               `user_id` bigint(20) unsigned DEFAULT NULL,
                               `role_id` bigint(20) unsigned DEFAULT 2,
                               KEY `hasuser` (`user_id`),
                               KEY `hasrole` (`role_id`),
                               CONSTRAINT `hasrole` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                               CONSTRAINT `hasuser` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into users_roles
values
(1, 1),
(1, 2),
(2, 2);