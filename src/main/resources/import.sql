INSERT INTO market_category (category_id,contents) VALUES (1,"이동"),(2,"심부름"),(3,"교육"),(4,"청소"),(5,"돌봄"),(6,"수리"),(7,"물건"),(8,"의견"),(9,"기타");

INSERT INTO branch_bank_account (branch_bank_account_id,account_name,owner,password) VALUES (1,"[햇살마을] 시작 햇살","ADMIN","admin");

INSERT INTO city (city_id, name) values (1,"서울특별시");
INSERT INTO town (town_id,name,city_id) values (1,"강남구",1), (2,"강동구",1), (3,"강서구",1), (4,"강북구",1), (5,"관악구",1), (6,"광진구",1),(7,"구로구",1),(8,"금천구",1),(9,"노원구",1),(10,"동대문구",1),(11,"도봉구",1),(12,"동작구",1),(13,"마포구",1),(14,"서대문구",1),(15,"성동구",1),(16,"성북구",1),(17,"서초구",1),(18,"송파구",1),(19,"영등포구",1),(20,"용산구",1),(21,"양천구",1),(22,"은평구",1),(23,"종로구",1),(24,"중구",1),(25,"중랑구",1);
INSERT INTO township (township_id,name,town_id) VALUES (1,"성북동",16),(2,"삼선동",16),(3,"동선동",16),(4,"돈암1동",16),(5,"돈암2동",16),(6,"안암동",16),(7,"보문동",16),(8,"정릉1동",16),(9,"정릉2동",16),(10,"정릉3동",16),(11,"정릉4동",16),(12,"길음1동",16),(13,"길음2동",16),(14,"종암동",16),(15,"월곡1동",16),(16,"월곡2동",16),(17,"장위1동",16),(18,"장위2동",16),(19,"장위3동",16),(20,"석관동",16);


INSERT INTO standard (standard_id,standard,name) values (1,500,"먹구름"),(2,1500,"아기 햇님"),(3,3000,"수호 햇님"),(4,-1,"기관");
INSERT INTO avatar (avatar_id,avatar_level_name,avatar_name,standard_id,avatar_photo_url) values (1,"CLOUD_YOUNG","YOUNG",1,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/cloud_youth.png"),(2,"BABYSUN_YOUNG","YOUNG",2,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/babysun_young.png"),(3,"SUN_YOUNG","YOUNG",3,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/sun_young.png"),(4,"CLOUD_YOUTH","YOUTH",1,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/cloud_youth.png"),(5,"BABYSUN_YOUTH","YOUTH",2,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/babysun_youth.png"),(6,"SUN_YOUTH","YOUTH",3,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/sun_youth.png"),(7,"CLOUD_MIDDLE","MIDDLE",1,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/cloud_middle.png"),(8,"BABYSUN_MIDDLE","MIDDLE",2,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/babysun_middle.png"),(9,"SUN_MIDDLE","MIDDLE",3,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/sun_middle.png"),(10,"CLOUD_OLD","OLD",1,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/cloud_old.png"),(11,"BABAYSUN_OLD","OLD",2,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/babysun_old.png"),(12,"SUN_OLD","OLD",3,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/sun_old.png");
INSERT INTO avatar (avatar_id,avatar_level_name,avatar_name,standard_id,avatar_photo_url) values (13,"ORGANIZATION","ORGANIZATION",1,"https://kr.object.ncloudstorage.com/k-eum/characterAsset/cloud_youth.png");

INSERT INTO users(user_id,email,password,role,banned) values (1,"test@email","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.","TEST",0),(2,"test2@email","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.","TEST",0);
INSERT INTO users(user_id,email,password,role,banned) values (3,"Jeong3Organization","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.","ROLE_ORGANIZATION",0);

insert into profile(profile_id,nickname,introduction,total_sunrise_pay,avatar_id,township_id,user_id)values (1,"세윤","저는 세윤 황씨죠",0,1,10,1),(2,"정환","저는 정환 박씨죠",0,4,10,2);
insert into profile(profile_id,nickname,introduction,total_sunrise_pay,avatar_id,township_id,user_id)values (3,"[정릉3동]주민센터","정릉 3동 공식주민센터 계정",-1,13,1,3);

insert into user_bank_account(user_bank_account_id,account_name,balance,password,user_id) values (1,"솔","300","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.",1),(2,"버섯모둠","300","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.",2);
insert into user_bank_account(user_bank_account_id,account_name,balance,password,user_id) values (3,"[정릉3동]주민센터","10000000","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.",3);

-- insert into market_post()
--     transaction_post_id,contents                ,current_accepted_people,location,market_type,max_num_of_people ,pay,slot,start_date
-- | title
-- | volunteer_time
-- | category_id
-- | township_id
-- | user_id