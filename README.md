Import the project into your favorite IDE. Ensure that you have JDK 11 and Maven 2.7.5. 

If you face any issue while importing the project, check the Java SDK versions in your IDE's build options. 

The import should install all the dependencies (the main dependencies are spring boot and junit). 

The project uses the Spring framework for building the REST API that implements consuming and resetting the quota values. 

The application can be run using your preferred IDE and can be tested using Postman (Sample Postman collection included in the GitHub repo) or cURL. 

The application can also be tested without starting it by using the testing framework. The unit tests are implemented with MockMVC and are ordered to test the consumption and resetting of the quota values. 

The base URL is http://localhost:8080/api