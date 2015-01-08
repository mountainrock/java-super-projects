Steps to run:
1. The first  build : mvn package . This should copy all the jars to war/web-inf/lib folder

Setup on eclipse
1. Ensure you have google dev plugin installed.
2. Add src and test folder to Build Path --> use as source folder
3. right click on project and say run as Web application


Technology stack
1. Velocity/Spring MVC/Spring/ Google JPA/GAE
2. velocity templates under war/web-inf/view


Data Model
----------
1. User 
     email, role(publisher/subscriber/admin), password, personal details
2. Magazine
	 name, file content, filetype(pdf),
	 publishing company, description, category, age rating, keywords, 
	 webUrl, blockCountries(list), countryPublishedFrom, 
	 pricePerIssue, subscription, language, datePublished,
	 dateToGoLive, dateUploaded, ipAddress, googleStorageId
	 
3. Subscription
	 type, frequency(weekly, monthly..), subscriptionPrice, noOfIssues, TODO:



TODO:
1. Add Issue
     Upload images for each magazine pages.
        
     Upload zip of images for entire magazine. Image should be names as 1,2,3 page numbers etc
     


http://localhost:8888/magazine/showUploadIssue/5066549580791808/6192449487634432