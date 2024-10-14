insert into end_user (id, email, name, CREATED_DATE) values (1000,'user1000@user1000.com', 'user1000', '2024-10-3');
insert into end_user (id, email, name, CREATED_DATE) values (2000,'user2000@user2000.com', 'user2000', '2024-10-4');
insert into end_user (id, email, name, CREATED_DATE) values (3000,'user3000@user3000.com', 'user3000', '2024-10-5');

insert into post (id, content, end_user_id, CREATED_DATE) values (1000,'POST 1000', 1000, '2024-10-2');
insert into post (id, content, end_user_id, CREATED_DATE) values (2000,'POST 2000', 2000, '2024-10-3');
insert into post (id, content, end_user_id, CREATED_DATE) values (3000,'POST 3000', 3000, '2024-10-5');

insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (1000, 2000, null, 1000, 'Good', '2024-10-1');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (2000, 3000, null, 1000, 'Not Good', '2024-10-1');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (3000, 1000, null, 1000, 'Whateveer', '2024-10-2');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (4000, 2000, 2000, 1000, 'Why?', '2024-10-5');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (5000, 2000, 3000, 1000, 'Not possible', '2024-10-6');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (6000, 1000, null, 2000, 'Happy morning', '2024-10-11');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (7000, 2000, 6000, 2000, 'Not Good', '2024-10-13');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (8000, 3000, 7000, 2000, 'Happy to help', '2024-10-15');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (9000, 2000, 8000, 2000, 'Happy', '2024-10-15');