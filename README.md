버전
```
java 17

springboot 2.7.15
```
**주의 : application.yml에 있는 jwt키나 restapi는 배포할때마다 설정하기 귀찮아서 
일단은 깃에 올려놓고 작업중이고 나중에 바꿀예정입니다. 카카오 키중 admin 키까지는 올려놓긴에는 위험하다고 판단되어 
환경변수로 관리하고 있으니 코드 작업할때 확인하고 올려주세요
</br>
firebase.json도 ignore처리 해놔서 올라가지는 않겠지만 공개되지 않게 주의해주세요
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