# About

应用积分制、OKR、阿米巴等管理方法实现的企业数据化管理系统

## Features


### Users

* 用户可以使用邮箱和密码登录系统.




## Usage

* modify config.edn
* clj -M:migrate migrate
* clj -M:server


## RestAPI

### Users

- [x] /api/users/login 
- [x] /api/users/signup
- [x] /api/users/me

## Information

- [x] /api/info/companies
- [x] /api/info/companies/:id/deparements