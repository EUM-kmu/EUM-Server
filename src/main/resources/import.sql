INSERT INTO market_category (category_id,contents) VALUES (1,"이동"),(2,"심부름"),(3,"교육"),(4,"청소"),(5,"돌봄"),(6,"수리"),(7,"기타");

INSERT INTO branch_bank_account (branch_bank_account_id,account_name,owner,password) VALUES (1,"[햇살마을] 시작 햇살","ADMIN","admin");

INSERT INTO regions (region_id,name,region_type,parent_id) values (1,"서울특별시","SI",null);
INSERT INTO regions (region_id, name, region_type, parent_id) VALUES (2, "강남구", "GU", 1), (3, "강동구", "GU", 1), (4, "강서구", "GU", 1), (5, "강북구", "GU", 1), (6, "관악구", "GU", 1), (7, "광진구", "GU", 1), (8, "구로구", "GU", 1), (9, "금천구", "GU", 1), (10, "노원구", "GU", 1), (11, "동대문구", "GU", 1), (12, "도봉구", "GU", 1), (13, "동작구", "GU", 1), (14, "마포구", "GU", 1), (15, "서대문구", "GU", 1), (16, "성동구", "GU", 1), (17, "성북구", "GU", 1), (18, "서초구", "GU", 1), (19, "송파구", "GU", 1), (20, "영등포구", "GU", 1), (21, "용산구", "GU", 1), (22, "양천구", "GU", 1), (23, "은평구", "GU", 1), (24, "종로구", "GU", 1), (25, "중구", "GU", 1), (26, "중랑구", "GU", 1);
INSERT INTO regions (region_id, name, region_type, parent_id) VALUES (27, "성북동", "DONG", 17), (28, "삼선동", "DONG", 17), (29, "동선동", "DONG", 17), (30, "돈암1동", "DONG", 17), (31, "돈암2동", "DONG", 17), (32, "안암동", "DONG", 17), (33, "보문동", "DONG", 17), (34, "정릉1동", "DONG", 17), (35, "정릉2동", "DONG", 17), (36, "정릉3동", "DONG", 17), (37, "정릉4동", "DONG", 17), (38, "길음1동", "DONG", 17), (39, "길음2동", "DONG", 17), (40, "종암동", "DONG", 17), (41, "월곡1동", "DONG", 17), (42, "월곡2동", "DONG", 17), (43, "장위1동", "DONG", 17), (44, "장위2동", "DONG", 17), (45, "장위3동", "DONG", 17), (46, "석관동", "DONG", 17);


INSERT INTO standard (standard_id,standard,name) values (1,0,"먹구름"),(2,1000,"아기 햇님"),(3,3000,"수호 햇님"),(4,-999,"기관");
INSERT INTO avatar (avatar_id,avatar_level_name,avatar_name,standard_id,avatar_photo_url) values (1,"CLOUD_YOUNG","YOUNG",1,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/cloud_youth.png"),(2,"BABYSUN_YOUNG","YOUNG",2,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/babysun_young.png"),(3,"SUN_YOUNG","YOUNG",3,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/sun_young.png"),(4,"CLOUD_YOUTH","YOUTH",1,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/cloud_youth.png"),(5,"BABYSUN_YOUTH","YOUTH",2,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/babysun_youth.png"),(6,"SUN_YOUTH","YOUTH",3,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/sun_youth.png"),(7,"CLOUD_MIDDLE","MIDDLE",1,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/cloud_middle.png"),(8,"BABYSUN_MIDDLE","MIDDLE",2,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/babysun_middle.png"),(9,"SUN_MIDDLE","MIDDLE",3,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/sun_middle.png"),(10,"CLOUD_OLD","OLD",1,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/cloud_old.png"),(11,"BABAYSUN_OLD","OLD",2,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/babysun_old.png"),(12,"SUN_OLD","OLD",3,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/sun_old.png");
INSERT INTO avatar (avatar_id,avatar_level_name,avatar_name,standard_id,avatar_photo_url) values (13,"ORGANIZATION","ORGANIZATION",4,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/organization.png");

INSERT INTO users(user_id,email,password,role,is_banned,is_deleted) values (1,"test@email","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.","TEST",0,0),(2,"test2@email","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.","TEST",0,0);
INSERT INTO users(user_id,email,password,role,is_banned,is_deleted) values (4,"student@email","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.","ROLE_USER",0,0);
INSERT INTO users(user_id,email,password,role,is_banned,is_deleted) values (3,"Jeong3Organization","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.","ROLE_ORGANIZATION",0,0);

insert into profile(profile_id,nickname,introduction,total_sunrise_pay,avatar_id,region_id,user_id)values (1,"세윤","저는 세윤 황씨죠",0,1,36,1),(2,"정환","저는 정환 박씨죠",0,4,36,2);
insert into profile(profile_id,nickname,introduction,total_sunrise_pay,avatar_id,region_id,user_id)values (3,"[정릉3동]주민센터","정릉 3동 공식주민센터 계정",-1,13,36,3);

insert into user_bank_account(user_bank_account_id,account_name,balance,password,owner,user_id,is_freeze)values (1,"세윤황","300","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.","USER",1,0),(2,"정환박","300","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.","USER",2,0);
insert into user_bank_account(user_bank_account_id,account_name,balance,password,owner,user_id,is_freeze)values (3,"[정릉3동]주민센터","100000","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.","USER",3,0);

insert into bank_account_transaction(bank_account_transaction_id  , amount ,code ,my_current_balance ,status ,branch_bank_account_id ,my_bank_account ,create_date,transaction_type)values (1,300,"SUCCESS",300,"INITIAL",1,1,now(),"DEPOSIT");
insert into bank_account_transaction(bank_account_transaction_id  , amount ,code ,my_current_balance ,status ,branch_bank_account_id ,my_bank_account ,create_date,transaction_type)values (2,300,"SUCCESS",300,"INITIAL",1,2,now(),"DEPOSIT");
insert into bank_account_transaction(bank_account_transaction_id  , amount ,code ,my_current_balance ,status ,branch_bank_account_id ,my_bank_account ,create_date,transaction_type)values (3,100000,"SUCCESS",100000,"INITIAL",1,3,now(),"DEPOSIT");

insert into market_post(market_post_id, content, current_accepted_people, location, market_type, max_num_of_people,pay, slot, start_date, title, volunteer_time, category_id,  user_id, status,create_date,is_deleted)values (1, "바퀴벌레 잡아 줄 사람 구해요", 0, "국민 빌라", "REQUEST_HELP", 1, 30, "ALL", "2024-08-13 00:00:00", "바퀴벌레 잡아주세요", "30", 4, 1, "RECRUITING",now(),0);
insert into market_post(market_post_id, content, current_accepted_people, location, market_type, max_num_of_people,pay, slot, start_date, title, volunteer_time, category_id,  user_id, status,create_date,is_deleted)values (2, "과외 구함", 0, "국민 빌라", "PROVIDE_HELP", 4, 30, "ALL", "2024-08-13 00:00:00", "과외구함", "30", 4, 2, "RECRUITING",now(),0);
insert into market_post(market_post_id, content, current_accepted_people, location, market_type, max_num_of_people,pay, slot, start_date, title, volunteer_time, category_id, user_id, status,create_date,is_deleted)values (3, "된장찌개 재료 장 봐와주세요", 0, "국민 빌라", "PROVIDE_HELP", 1, 30, "ALL", "2024-08-13 00:00:00", "장보기", "30", 4, 1, "RECRUITING",now(),0);
insert into market_post(market_post_id, content, current_accepted_people, location, market_type, max_num_of_people,pay, slot, start_date, title, volunteer_time, category_id,  user_id, status,create_date,is_deleted)values (4, "연탄 나르기 ", 0, "국민 빌라", "REQUEST_HELP", 1, 30, "ALL", "2024-08-13 00:00:00", "연탄을 날라보자", "30", 1, 2, "RECRUITING",now(),0);
insert into market_post(market_post_id, content, current_accepted_people, location, market_type, max_num_of_people,pay, slot, start_date, title, volunteer_time, category_id,  user_id, status,create_date,is_deleted)values (5, "바퀴벌레 잡아 줄 사람 구해요", 1, "국민 빌라", "REQUEST_HELP", 1, 30, "ALL", "2024-08-13 00:00:00", "쓰레기장", "30", 4, 2, "RECRUITMENT_COMPLETED",now(),0);
insert into market_post(market_post_id, content, current_accepted_people, location, market_type, max_num_of_people,pay, slot, start_date, title, volunteer_time, category_id,  user_id, status,create_date,is_deleted)values (6, "바퀴벌레 잡아 줄 사람 구해요", 1, "국민 빌라", "PROVIDE_HELP", 1, 30, "AM", "2024-08-13 00:00:00", "재롱잔치", "30", 2, 1, "RECRUITMENT_COMPLETED",now(),0);
insert into market_post(market_post_id, content, current_accepted_people, location, market_type, max_num_of_people,pay, slot, start_date, title, volunteer_time, category_id,  user_id, status,create_date,is_deleted)values (7, "바퀴벌레 잡아 줄 사람 구해요", 0, "국민 빌라", "REQUEST_HELP", 1, 30, "PM", "2024-08-13 00:00:00", "심부름", "30", 4, 1, "RECRUITING",now(),0);
insert into market_post(market_post_id, content, current_accepted_people, location, market_type, max_num_of_people,pay, slot, start_date, title, volunteer_time, category_id,  user_id, status,create_date,is_deleted)values (8, "바퀴벌레 잡아 줄 사람 구해요", 0, "국민 빌라", "PROVIDE_HELP", 1, 30, "ALL", "2024-08-13 00:00:00", "구걸", "30", 5, 2, "RECRUITING",now(),0);
insert into market_post(market_post_id, content, current_accepted_people, location, market_type, max_num_of_people,pay, slot, start_date, title, volunteer_time, category_id,  user_id, status,create_date,is_deleted)values (19, "바퀴벌레 잡아 줄 사람 구해요", 0, "국민 빌라", "REQUEST_HELP", 1, 30, "ALL", "2024-08-13 00:00:00", "모기박멸", "30", 4, 2, "RECRUITING",now(),0);
--
-- insert into opinion_post(opinion_post_id,create_date,content,like_count,title,region_id,user_id) values (1,now(),"얼죽아 회원 모집합니다.관심 있으신분",0,"얼죽아 회원",10,1);
-- insert into opinion_post(opinion_post_id,create_date,content,like_count,title,region_id,user_id) values (2,now(),"집에 가고싶어요",0,"집",10,2);
-- insert into opinion_post(opinion_post_id,create_date,content,like_count,title,region_id,user_id) values (3,now(),"피곤합니다",0,"커피 수혈해서 다닐까",10,2);
-- insert into opinion_post(opinion_post_id,create_date,content,like_count,title,region_id,user_id) values (4,now(),"학교를 왜 와야하는 걸까요 휴강해주세요",0,"i'm 휴강 원해요",10,1);
--
-- insert into vote_post(vote_post_id,create_date,agree_count,content,disagree_count,end_time,title,total,region_id,user_id) values (1,now(),3,"학교에서 꼭 와야하는가",10,"2020-11-13 18:14:48","학교에 와야할까요",13,10,1);
-- insert into vote_post(vote_post_id,create_date,agree_count,content,disagree_count,end_time,title,total,region_id,user_id) values (2,now(),10,"당신은 지금 졸린가요",2,"2020-11-13 18:14:48","학교에 와야할까요",12,10,2);
-- insert into vote_post(vote_post_id,create_date,agree_count,content,disagree_count,end_time,title,total,region_id,user_id) values (3,now(),10,"학교에서 꼭 와야하는가",10,"2020-11-13 18:14:48","학교에 와야할까요",20,10,1);

update avatar set simple_avatar_photo_url = "https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simple_cloud_young.png" where avatar_id = 1;
update avatar set simple_avatar_photo_url = "https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simple_babaysun_young.png" where avatar_id = 2;
update avatar set simple_avatar_photo_url = "https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simple_sun_young.png" where avatar_id = 3;
update avatar set simple_avatar_photo_url ="https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simple_cloud_youth.png" where avatar_id =4;
update avatar set simple_avatar_photo_url = "https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simple_babaysun_youth.png" where avatar_id=5;
update avatar set simple_avatar_photo_url = "https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simple_sun_youth.png" where avatar_id = 6;
update avatar set simple_avatar_photo_url = "https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simple_cloud_middle.png" where avatar_id = 7;
update avatar set simple_avatar_photo_url = "https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simple_babaysun_middle.png" where avatar_id = 8;
update avatar set simple_avatar_photo_url ="https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simple_sun_middle.png" where avatar_id = 9;
update avatar set simple_avatar_photo_url ="https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simple_cloud_old.png" where avatar_id = 10;
update avatar set simple_avatar_photo_url ="https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simple_babaysun_old.png" where avatar_id = 11;
update avatar set simple_avatar_photo_url ="https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simple_sun_old.png" where avatar_id = 12;
update avatar set simple_avatar_photo_url = "https://kr.object.ncloudstorage.com/k-eum/simpleCharaterAsset/simeple_organization.png" where avatar_id =13;
update avatar set avatar_photo_url = "https://kr.object.ncloudstorage.com/k-eum/characterAsset/organization.png" where avatar_id = 13;

insert into withdrawal_category(withdrawal_category_id,content) VALUES (1,"앱을 쓰지 않아요"),(2,"알림이 너무 많이 와요"),(3,"비매너 사용자를 만났어요"),(4,"새 계정을 만들고 싶어요"),(5,"기타");
