# UserService
	UserService Deals with the authenticationa and authorization requests with the help of Spring OAuth.
	Provide API to manage roles and user details such as user_id, email, password.
	End user needs to signup and login to access the resources from other services

# Flow:
	User Sign Up 
	-> User login (JWT token is returned from UserService)\
	-> User can call API 
		-> Token is validated (Authroization checked, Role checked)
			Successfully
				Users request processed.
			Else 
				Request Denied.
![E-Commerce](https://github.com/viddy09/UserService/assets/70717147/032797bf-f952-4c81-9b77-79ca27521f94)


# Pre Requisites:
	JDK 12+, Spring and spring boot Framework, Maven dependency, Redis for Windows is required.

# Supported PlatForms:
	Windows

# References:
	Spring Data : https://docs.spring.io/spring-data/jpa/reference/jpa/getting-started.html
	Spring Security : https://docs.spring.io/spring-authorization-server/reference/getting-started.html
	Others : StackOverFlow, Spring Docs, etc

