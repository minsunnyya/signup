회원가입

```
sequenceDiagram
autonumber
client ->> server: 회원가입 요청
alt 성공한 경우
server -->> client: 성공 반환
else 아이디가 중복된 경우 
server -->> client: reason code와 함께 실패 반환
end

```

로그인
```
sequenceDiagram
autonumber
client ->> server: 로그인
alt 성공한 경우
server ->> client: 성공 반환
else 아이디가 존재하지 않는 경우
server -->> client: reason code와 함께 실패 반환
else 패스워드가 틀린 경우
server -->> client: reason code와 함께 실패 반환
end
```