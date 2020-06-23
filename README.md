# [REST API 기반 쿠폰 시스템]
개발자 - 경찬주. 2020.06.23


## 목차
- [개발 환경](#개발-환경)
- [빌드 및 실행방법](#빌드-및-실행방법)
- [사용한 Entity 정의서](#사용한-Entity-정의서)
- [선택기능 요구사항](#선택기능-요구사항)
- [필수기능 요구사항](#필수기능-요구사항)

---

## 개발 환경
- 기본 환경
    - IDE: IntelliJ IDEA Community 2019.03
    - OS: Mac OS X
    - GIT
- Server
    - Java8
    - Spring Boot 2.2.4
    - JPA
    - H2
    - Gradle
    - Junit4
    - commons-lang3:3.9
    - jjwt-api:0.11.1


## 빌드 및 실행방법
1. 깃에서소스를 다운받는다.
2. 바로 실행가능.
3. 혹시 실행에서 오류난다면 아래 jar를 다운받아 추가해준다.
http://commons.apache.org/proper/commons-lang/
4. 접속 Base URI: `http://localhost:8080`


## 사용한 Entity 정의서
### 1. 쿠폰-[Coupon]
- 쿠폰번호와 사용,취소등 관련 정보를 관리하는 테이블

|컬럼명|컬럼한글명|데이터타입|설명|
| --- | --- | --- | --- |
|id|일련번호|Long(PK)|자동으로 asc번호 생성|
|couponNum|쿠폰번호|String(PK)|15자리 랜덤한 쿠폰번가 저장됨(범위:0~9,a~z,A~Z)|
|userId|회원일련번호|Long|지급된 회원의 일련번호|
|expDate|만료일자|String|쿠폰만료일자.예)20200101|
|useYN|사용여부|String|사용했으면Y, 미사용이면N, default값=N|
|useDate|사용일자|String|사용처리한 일자.예)20200101|
|cnclYN|취소여부|String|취소했으면Y, 미이면N, default값=null|
|cnclDate|취소일자|String|취소처리한 일자.예)20200101, default값=null|

* index추가함 -> IDX_COUPON_NUM(columnList = "id,couponNum, userId")


### 2. 회원-[User]
|컬럼명|컬럼한글명|데이터타입|설명|
| --- | --- | --- | --- |
|id|일련번호|Long(PK)|자동으로 asc번호 생성|
|email|이메일|String|가입을 이메일로 함|
|password|비밀번호|String|암호화해서 저장됨|
|userName|회원이름|String||
|signInDate|가입일자|String|가입한 일자 예(20200101)|

* 테이블 관계 -> [user] 1:N [coupon]

### 3. h2 DB에 로그인하는 방법
- http://localhost:8080/h2-console/
- driver class -> org.h2.Driver
- jdbc url -> jdbc:h2:~/data/kyungCoupon
- user name -> sa
- password는 없다.


## 선택기능 요구사항
- API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 인증 기능을 개발하고 각 API 호출 시에 HTTP Header 에 발급받은 토큰을 가지고 호출한다.
    - **signup 계정생성 API**: 입력으로 ID, PW 받아 내부 DB 에 계정 저장하고 토큰 생성하여 출력
        - 단, 패스워드는 인코딩하여 저장한다.
        - 단, 토큰은 특정 secret 으로 서명하여 생성한다.
    - **signin 로그인 API**: 입력으로 생성된 계정 (ID, PW)으로 로그인 요청하면 토큰을 발급한다.
    - **refresh 토큰 재발급 API**: 기존에 발급받은 토큰을 Authorization 헤더에 “Bearer Token”으로 입력 요청을 하면 토큰을 재발급한다.

### 1. 회원가입
- 관련소스 : kyungCoupon.controller.UserController.java
- 기능설명 :
1. 이메일주소 검증
2. 비밀번호 암호화처리-PasswordEncoder.encode()
- @input : email=?, password=?, userName=? 
- @output : 가입상태값, 메시지
- 실행명령어 : http POST localhost:8080/signIn email=test3@gmail.com password=test userName=test3
- 결과 : 
```
{
    "description": "가입 성공했습니다.",
    "resultCode": "0"
}
```
|ID|EMAIL|PASSWORD|SIGN_IN_DATE|USER_NAME|  
| --- | --- | --- | --- | --- |
|23|test3@gmail.com|$2a$10$N6OZWRQ1G2i3Ib9yOc1d9.qsJ9uSFzlNKxmPZjnUWeRF4mFYXGkkO|20200623|test3|


### 2. 로그인
- 관련소스 : kyungCoupon.controller.UserController.java
- 기능설명 :
1. 이메일주소, 비밀번호 검증
2. accessToken생성-Jwt
- @input : email=?, password=? 
- @output : accessTocken, 로그인 상태값, 메시지
- 실행명령어 : http POST localhost:8080/logIn email=test3@gmail.com password=test
- 결과 : 
```
{
    "data": {
        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MyIsImlkIjoyMywiZW1haWwiOiJ0ZXN0M0BnbWFpbC5jb20iLCJ1c2VyTmFtZSI6InRlc3QzIn0.RDHXnfz5tHHw5OW27vwvdcLlYmuMUaNzVB2Guryx5tc"
    },
    "description": "test3님, 환영합니다.",
    "resultCode": "0"
}

```


### 7. 발급된 쿠폰중 당일 만료3일전 사용자에게 메세지 전송
- 관련소스 : kyungCoupon.controller.SndMsg3DayBfreExpController.java
- 기능설명 : 
1. 사용자가 지정된 쿠폰중 3일전 유효기간인 대상조회
2. 메시지 세팅
3.System.out.으로 출력
- @input : 
- @output : 
- 실행명령어 : http POST localhost:8080/SndMsg3DayBfreExp/20200621
- 결과 : 
```
{2건이 전송되었습니다.}
******************* snd email *********************
To : test@gmail.com
t**t 고객님, 쿠폰[QhHeP]이 3일 후 만료됩니다.
From : admin@kyungcoupon.com
***************************************************
******************* snd email *********************
To : test3@gmail.com
t***3 고객님, 쿠폰[jj6a4]이 3일 후 만료됩니다.
From : admin@kyungcoupon.com
***************************************************
```
    


## 필수기능 요구사항
### 1. 랜덤한 코드의 쿠폰을 N개 생성하여 DB에 저장하는 API
- 관련소스 : kyungCoupon.controller.CouponController.java
- 기능설명 :
1. 쿠폰랜범 번호 생성-RandomStringUtils.randomAlphanumeric()
2. HashSet에 담아 저장
- @input : int couponCnt
- @output : 결과메시지
- 실행명령어 : http POST localhost:8080/generateCoupon/1
- 결과 : 
```
{1개의 쿠폰이 생성완료함.}
```
|COUPON_NUM|ID|CNCL_DATE|CNCLYN|EXP_DATE|USE_DATE|USEYN|USER_ID|  
| --- | --- | --- | --- | --- | --- | --- | --- |
|1DqziwUtHeV5kgk|2|null|null|20200624|null|N|null|


### 2. 생성된 쿠폰중 하나를 사용자에게 지급하는 API
- 관련소스 : kyungCoupon.controller.CouponController.java
- @input : String accessToken
- @output : XXXXX-XXXXX-XXXXX
- 실행명령어 : http PATCH localhost:8080/setCouponUser "Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MyIsImlkIjoyMywiZW1haWwiOiJ0ZXN0M0BnbWFpbC5jb20iLCJ1c2VyTmFtZSI6InRlc3QzIn0.RDHXnfz5tHHw5OW27vwvdcLlYmuMUaNzVB2Guryx5tc"
- 결과 : 
```
{
    "data": "jj6a4-kdE7Q-Myqez",
    "description": "정상처리되었습니다.",
    "resultCode": "0"
}
```
|COUPON_NUM|ID|CNCL_DATE|CNCLYN|EXP_DATE|USE_DATE|USEYN|USER_ID|  
| --- | --- | --- | --- | --- | --- | --- | --- |
|jj6a4kdE7QMyqez|24|null|null|20200624|null|N|23|


### 3. 지급된 쿠폰을 조회하는 API
- 관련소스 : kyungCoupon.controller.CouponController.java
- @input : String accessToken 
- @output : List<Coupon>
- 실행명령어 : http GET localhost:8080/getMyCouponInfo "Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MyIsImlkIjoyMywiZW1haWwiOiJ0ZXN0M0BnbWFpbC5jb20iLCJ1c2VyTmFtZSI6InRlc3QzIn0.RDHXnfz5tHHw5OW27vwvdcLlYmuMUaNzVB2Guryx5tc"
- 결과 : 
```
{
    "dataList": [
        {
            "cnclDate": null,
            "cnclYN": null,
            "couponNum": "jj6a4kdE7QMyqez",
            "expDate": "20200624",
            "id": 24,
            "useDate": null,
            "useYN": "N"
        }
    ],
    "dataListSize": 1,
    "description": "정상처리되었습니다.",
    "resultCode": "0"
}
```
|COUPON_NUM|ID|CNCL_DATE|CNCLYN|EXP_DATE|USE_DATE|USEYN|USER_ID|  
| --- | --- | --- | --- | --- | --- | --- | --- |
|jj6a4kdE7QMyqez|24|null|null|20200624|null|N|23|



### 4. 지급된 쿠폰중 하나를 사용하는 API
- 관련소스 : kyungCoupon.controller.CouponController.java
- @input : String couponNum, String accessToken
- @output : Coupon
- 실행명령어 : http PATCH localhost:8080/useCoupon/jj6a4kdE7QMyqez "Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MyIsImlkIjoyMywiZW1haWwiOiJ0ZXN0M0BnbWFpbC5jb20iLCJ1c2VyTmFtZSI6InRlc3QzIn0.RDHXnfz5tHHw5OW27vwvdcLlYmuMUaNzVB2Guryx5tc"
- 결과 : 
```
{
    "data": {
        "cnclDate": null,
        "cnclYN": null,
        "couponNum": "jj6a4kdE7QMyqez",
        "expDate": "20200624",
        "id": 24,
update->"useDate": "20200623", 
update->"useYN": "Y" 
    },
    "description": "정상처리되었습니다.",
    "resultCode": "0"
}
```
|COUPON_NUM|ID|CNCL_DATE|CNCLYN|EXP_DATE|USE_DATE|USEYN|USER_ID|  
| --- | --- | --- | --- | --- | --- | --- | --- |
|jj6a4kdE7QMyqez|24|null|null|20200624|20200623|Y|23|  
```
재사용불가 확인:
[kyungCoupon.exception.CouponAllUsedException: 해당 쿠폰은 이미 사용완료되었습니다.[쿠론번호 : jj6a4-kdE7Q-Myqez]]  
```


### 5. 지급된 쿠폰중 하나를 사용취소하는 API(취소된 쿠폰재사용가능)
- 관련소스 : kyungCoupon.controller.CouponController.java
- @input : String couponNum, String accessToken
- @output : Coupon
- 실행명령어 : http PATCH localhost:8080/cnclUseCoupon/jj6a4kdE7QMyqez "Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MyIsImlkIjoyMywiZW1haWwiOiJ0ZXN0M0BnbWFpbC5jb20iLCJ1c2VyTmFtZSI6InRlc3QzIn0.RDHXnfz5tHHw5OW27vwvdcLlYmuMUaNzVB2Guryx5tc"
- 결과 : 
```
{
    "data": {
update->"cnclDate": "20200623",<-update
update->"cnclYN": "Y",<-update
        "couponNum": "jj6a4kdE7QMyqez",
        "expDate": "20200624",
        "id": 24,
update->"useDate": null,
update->"useYN": "N"
    },
    "description": "정상처리되었습니다.",
    "resultCode": "0"
}
```
|COUPON_NUM|ID|CNCL_DATE|CNCLYN|EXP_DATE|USE_DATE|USEYN|USER_ID|  
| --- | --- | --- | --- | --- | --- | --- | --- |
|jj6a4kdE7QMyqez|24|20200623|Y|20200624|null|N|23|  
```
취소된 쿠폰재사용가능 확인:
{
    "data": {
update->"cnclDate": null,
update->"cnclYN": null,
        "couponNum": "jj6a4kdE7QMyqez",
        "expDate": "20200624",
        "id": 24,
update->"useDate": "20200623",
update->"useYN": "Y"
    },
    "description": "정상처리되었습니다.",
    "resultCode": "0"
}
```



### 6. 발급된 쿠폰중 당일 만료된 전체목록조회 API
- 관련소스 : kyungCoupon.controller.CouponController.java
- @input : 
- @output : List<Coupon>
- 실행명령어 : http GET localhost:8080/getCouponExpiredToday
- 결과 : 
```
{
    "dataList": [
        {
            "cnclDate": null,
            "cnclYN": null,
            "couponNum": "FHoS9E7wfC5QUZp",
            "expDate": "20200623",
            "id": 20,
            "useDate": null,
            "useYN": "N"
        }
    ],
    "dataListSize": 1,
    "description": "정상처리되었습니다.",
    "resultCode": "0"
}
```
|COUPON_NUM|ID|CNCL_DATE|CNCLYN|EXP_DATE|USE_DATE|USEYN|USER_ID|  
| --- | --- | --- | --- | --- | --- | --- | --- |
|FHoS9E7wfC5QUZp|20|null|null|20200623|null|N|null|  




