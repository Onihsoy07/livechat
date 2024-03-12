# 실시간 채팅
웹소켓을 활용한 실시간 채팅 서비스입니다.


## 개발 기간
- 2024.01.21 ~ 진행중


## 멤버구성
- 박준혁 : 프론트엔드(Vue3) 및 백엔드(Spring Boot 3)


## 개발환경
- java 17
- IDE : IntelliJ IDEA Community Edition 2023.2.6, Visual Studio Code 1.87.1
- Framework : Spring Boot 3.2.2
- Database : MySQL 8.0.32

  
# 목차
[1. 엔티티 다이어그램](#엔티티-다이어그램)

[2. 주요 기능](#주요-기능)

[3. 문제 해결](#문제-해결)

[4. 웹 사이트 화면](#웹-사이트-화면)




## 엔티티 다이어그램
![스크린샷 2024-03-12 205723](https://github.com/Onihsoy07/livechat/assets/84126411/96d26f7e-0b72-4a5e-b55b-68fbe2b8d700)



## 주요 기능
### 로그인 및 회원가입
- JWT 사용

### 대화방
- WebSocket 및 Redis Pub/Sub를 사용한 Message Queue 통신
- 대화방 별 메시지 내역 Redis 캐시 저장
- 파일 전송 및 다운



## 문제 해결

- [기본 주소 접속 시 인가 문제(403 에러)](https://github.com/Onihsoy07/livechat/wiki/%EA%B8%B0%EB%B3%B8-%EC%A3%BC%EC%86%8C-%EC%A0%91%EC%86%8D-%EC%8B%9C-%EC%9D%B8%EC%A6%9D-%EB%AC%B8%EC%A0%9C(403-%EC%97%90%EB%9F%AC))
- [웹소켓 연결 해제 시 인증 에러 로그 발생 문제](https://github.com/Onihsoy07/livechat/wiki/%EC%9B%B9%EC%86%8C%EC%BC%93-%EC%97%B0%EA%B2%B0-%ED%95%B4%EC%A0%9C-%EC%8B%9C-%EC%9D%B8%EC%A6%9D-%EC%97%90%EB%9F%AC-%EB%A1%9C%EA%B7%B8-%EB%B0%9C%EC%83%9D-%EB%AC%B8%EC%A0%9C)



# 웹 사이트 화면

![ezgif-4-8a6eb2e50d](https://github.com/Onihsoy07/livechat/assets/84126411/d0908be9-ec5b-4632-9815-bf6c0eb04305)





