insert into end_user (id, email, name, CREATED_DATE) values (1,'user1@user1.com', 'user1', '2024-10-3');
insert into end_user (id, email, name, CREATED_DATE) values (2,'user2@user2.com', 'user2', '2024-10-4');
insert into end_user (id, email, name, CREATED_DATE) values (3,'user3@user3.com', 'user3', '2024-10-5');

insert into post (id, content, end_user_id, CREATED_DATE) values (1,'POST 1', 1, '2024-10-2');
insert into post (id, content, end_user_id, CREATED_DATE) values (2,'POST 2', 2, '2024-10-3');
insert into post (id, content, end_user_id, CREATED_DATE) values (3,'POST 3', 3, '2024-10-5');


/*insert into POST_USER_INTERACTION (is_active, is_liked, end_user_id, post_id) values (true, true, 2, 1 );
insert into POST_USER_INTERACTION (is_active, is_liked, end_user_id, post_id) values (true, false, 3, 1);
insert into POST_USER_INTERACTION (is_active, is_liked, end_user_id, post_id) values (true, true, 1, 2);
insert into POST_USER_INTERACTION (is_active, is_liked, end_user_id, post_id) values (true, true, 3, 2 );
insert into POST_USER_INTERACTION (is_active, is_liked, end_user_id, post_id) values (true, false, 1, 3);
insert into POST_USER_INTERACTION (is_active, is_liked, end_user_id, post_id) values (true, false, 2, 3);*/

insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (1, 2, null, 1, 'Good', '2024-10-1');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (2, 3, null, 1, 'Not Good', '2024-10-1');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (3, 1, null, 1, 'Whateveer', '2024-10-2');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (4, 2, 2, 1, 'Why?', '2024-10-5');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (5, 2, 3, 1, 'Not possible', '2024-10-6');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (6, 1, null, 2, 'Happy morning', '2024-10-11');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (7, 2, 6, 2, 'Not Good', '2024-10-13');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (8, 3, 7, 2, 'Happy to help', '2024-10-15');
insert into comment (id, end_user_id, parent_id, post_id, content, CREATED_DATE) values (9, 2, 8, 2, 'Happy', '2024-10-15');