
```
java 17

springboot 2.7.15
```
### 실행을 위한 추가 설정
application.yml 파일에서 이 어드민 키는 카카오에서 받아오면 됩니다
```
admin-key: ${admin-key}
```
DB 컨테이너 실행
```
docker-compose up -d
```
firebase 설정파일 경로 
```
src/main/resources/firebase.json
```