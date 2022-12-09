회원가입

```
    client ->> server: 회원가입 요청
    alt 성공한 경우
    server -->> client: 성공 반환
    else 아이디가 중복된 경우 
    server -->> client: reason code와 함께 실패 반환
    
    postman으로 테스트
    @AllArgsConstructor -> @NoArgsConstructor로 변경
    @mockbean 테스트 안씀
    
    db : heidisql사용 
    end
```