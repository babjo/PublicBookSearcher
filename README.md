# 서시 - 서울시 공공도서관 통합 도서검색 앱
본 프로젝트는 서울시에서 주최한 서울시 앱 공모전을 위해 구현한 프로젝트입니다. 플레이스토어에 올라간 상태고 많은 다운로드 부탁드려요 :)
[링크](https://play.google.com/store/apps/details?id=com.seoul.publicbooksearcher)

## 주요기능
- 도서 통합 검색
- 도서명 자동완성
- 검색내용 캐시에 저장하기
- 동일한 도서검색시 캐시 사용하기
- 가까운 도서관 찾기

## 주요화면
| 검색 화면  | 가까운 도서관 찾기 화면 |
| ------------- | ------------- |
| ![도서검색](./img/search.png)  | ![위치찾기](./img/gps.png)  |

## 구현
- 백엔드는 Spring, Spring Security, Hibernate를 이용하여 REST API 구성
- 프론트는 Bootstrap, LESS를 이용하여 화면 구성을 하고 JQuery, HandlebarsJS를 이용하여 AJAX 통신

## 디렉토리 구조
- java
    - org.lch
        - controller : Restful URL Mapping 및 예외 핸들링
        - domain : 할일, 유저 클래스 (POJO)
        - dto : 사용자에게 전달할 데이터를 담을 클래스
        - exception : 각종 예외 클래스 (로그인 실패, JWT 토큰 만료 등) 
        - repository : DB 액세스 (Hibernate 구현) 
        - service : 각종 비지니스 로직 (할일 추가, 삭제, 수정 등)
    - security : JWT 인증 구현 (스프링 시큐리티 기능 커스텀)
- webapp/WEB-INF
    - spring
        - appServlet
            - servlet-context.xml : Controller 빈 설정
            - test-context.xml : 테스트를 위한 빈 컨텍스트
        - root-context.xml : 서블릿 컨텍스트에서 사용하는 공통 빈 설정 (Service 빈, Repository 빈)
        - security.xml : 스프링 시큐리티 JWT 빈 설정
    - web.xml : 스프링 컨텍스트 로드 설정 및 각종 필터 설정 (스프링 시큐리티, 인코딩 필터)
    