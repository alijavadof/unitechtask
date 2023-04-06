# UniTech service

With this service, it is possible to register, log in and make money transfers to a bank account.

Basic auth is used for authentication (except login and register).

H2 in-memory DB is used for storage.

## Build and test

To build the service and run the tests, we run the following command:
```shell
mvnw clean test
```

## Start the service

Run the following command to start the service:
```shell
mvnw spring-boot:run
```

## REST endpoints

### 1. Register

`POST http://localhost:8080/api/v1/auth/register` 

Request body:
```
{
  "pin": "xxx",
  "password": "xxx"
}
```

Response:
If pin not exists "Successfully registered!" else "User already exists"

### 2. Login

`Post http://localhost:8080/api/v1/auth/login`

Request body:
```
{
  "pin": "xxx",
  "password": "xxx"
}
```

Response:
If credentials are correct "Welcome: xxx" else "Username or password is incorrect"

### 3. Create Account

`POST http://localhost:8080/api/v1/bank-accounts`

Request body:
```
{
  "balance": xxx
}
```

Response:
If user is exists:
```
{
  "accountNumber": "xxxxxxxx",
  "balance": xxxxxx,
  "createdDate": "xxxx-xx-xx"
}
```
else shows error "User not found"

### 4. Get accounts

`GET http://localhost:8080/api/v1/bank-accounts`

Response:
list of all active accounts of the user

### 5. Transfer money

`POST http://localhost:8080/api/v1/bank-accounts/transfer`

Request body:
```
{
  "fromAccountNumber": "xxx",
  "toAccountNumber": "xxx",
  "money": xxx
}
```

Response:
1. if fromAccountNumber and toAccountNumber are the same, throws TheSameAccountException and shows message "Can not transfer between the same accounts."
2. if toAccountNumber doesn't exist, throws BankAccountNotFoundException and shows message "Can not send money to non-existent account."
3. if fromAccountNumber doesn't exist or belongs to other user, throws BankAccountNotFoundException and shows message "Account not found in your accounts."
4. if toAccountNumber is deactive, throws NotActiveAccountException and shows message "Can not send money to deactive account."
5. if fromAccountNumber doesn't have enough balance for transfer, throws NotEnoughBalanceException and shows message "Not enough balance for transfer."
6. "Successfully transferred" message appears if no error occurs.

### 6. Get currency rate

`GET http://localhost:8080/api/v1/currency-rate?source={sourceCurrency}&target={targetCurrency}`

Response:
```
{
  "sourceCurrency": "TL",
  "targetCurrency": "AZN",
  "rate": 0.125
}
```
