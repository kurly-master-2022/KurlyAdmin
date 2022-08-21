# KurlyAdmin

### 상품별 메트릭 수집 및 조회 시스템 의 어드민 백엔드 시스템입니다.

어드민은 [프론트 페이지](https://github.com/kurly-master-2022/KurlyAdminFront) 를 통해 시스템과 상호작용하고, 해당 프론트 페이지와 같이 연동하는 백엔드 시스템이 이 **KurlyAdmin** 입니다.

### 서비스 역할

어드민은 다음과 같은 기능을 수행할 수 있어야 하고, 따라서 백엔드는 다음과 같은 기능을 수행합니다.

- 상품
  - 전체 상품 조회
  - 상품의 과거 데이터와, 상품에 매핑된 메트릭의 과거 데이터를 조회
  - 상품에 매핑된 메트릭의 우선순위 변경
  - 상품에 다른 메트릭을 매핑
  - 상품에 매핑된 메트릭을 제거
- 메트릭
  - 전체 메트릭 조회
  - 메트릭 생성 요청
  - 메트릭 삭제
  - 메트릭의 과거 수치 데이터 조회
  - 메트릭이 현재 역치를 넘은 상황인지 확인
  - 메트릭에 매핑된 상품 추가
  - 메트릭에 매핑된 상품 제거
- 구독자
  - 전체 구독자 조회
  - 구독자 생성
  - 구독자 제거
  - 구독자가 구독하는 메트릭 조회
  - 특정 구독자의 메트릭 구독 추가
  - 특정 구독자의 메트릭 구독 해제

<br>

- 메트릭 생성 요청 시 `Metric Workflow 관리자 (MetricHosu)` 에게 요청을 보냅니다
- 메트릭 알람 상황 확인 시 `Metric Workflow 관리자 (MetricHosu)` 에게 요청을 보냅니다
- 메트릭 과거 데이터 조회 시 `CloudWatch API` 를 사용합니다.
- 기타 요청은 DB 에 주로 접근합니다.

<br>

### 특징
- Domain-driven Design 적용 시도

<br>

### 기술 스택

- (배포) AWS CloudFormation
- (DB) AWS RDS (with AWS Aurora MySQL compatible)
- Kotlin Spring 
  - Tomcat + JDBC
- AWS CloudWatch API

