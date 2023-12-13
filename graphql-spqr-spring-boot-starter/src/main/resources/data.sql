insert into `board` (id, title, content, created_at, updated_at, created_by, updated_by)
values (1, 'board title', 'board content', now(), now(), '', ''),
       (2, 'board title 2', 'board content 2', now(), now(), '', ''),
       (3, 'board title 3', 'board content 3', now(), now(), '', '');
ALTER TABLE board
    AUTO_INCREMENT = 4;

insert into `comment`(id, board_id, content, parent_id, created_at, updated_at, created_by, updated_by)
values (1, 1, 'comment content 1', null, now(), now(), '', ''),
       (2, 1, 'comment content 2', null, now(), now(), '', ''),
       (3, 2, 'comment content 3', null, now(), now(), '', ''),
       (4, 2, 'comment content 4', null, now(), now(), '', ''),
       (5, 3, 'comment content 5', null, now(), now(), '', ''),
       (6, 3, 'comment content 6', null, now(), now(), '', ''),
       (7, 3, 'comment content 7', null, now(), now(), '', ''),
       (8, 3, 'comment content 8', null, now(), now(), '', ''),
       (9, 3, 'comment content 9', null, now(), now(), '', '');

ALTER TABLE comment
    AUTO_INCREMENT = 10;

insert into `user` (id, user_id, name, password, created_at, updated_at, created_by, updated_by)
values (1, 'user id 1', 'name 1', 'password 1', now(), now(), '', ''),
       (2, 'user id 2', 'name 2', 'password 2', now(), now(), '', '');

ALTER TABLE `user`
    AUTO_INCREMENT = 3;
