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
-----
1. Publisher module
	   Manage magazine (in progress)
	   Add Issue       (in progress)    
	     Bulk upload images for each magazine pages.	       
	       Future : Upload zip/pdf of images for entire magazine. Image should be names as 1,2,3 page numbers etc
	   Preview issue   (in progress)
	   Manage report
	   Manage finance details
   
2. Subscriber module
		Preview magazine (in progress)
		Shopping basket 
		    Add to basket (in progress)
		    Make payment
		    Checkout (in progress)
		View subscriptions
		Display subscribed magazine in full  
		  
3. Admin module
        Review published magazine and make live
		Manage magazine/issue/finance details
		Run report
		Manage user - add/update/ban   
		Manage magazine categories

4. Security
         Publisher login (in progress)
         Subscriber login (in progress)
         Admin login (in progress)
         Authorisation (URL access)   

5. Mobile site
Ref: http://www.srccodes.com/m/p/article/25/spring-mobile-hello-world-example-that-includes-deviceresolver-sitepreference-urlpath-siteswitcher-and-litedevicedelegatingviewresolver

6. Validation, list of values 
7. Email
8. Search (in progress)

Issues:
1. Multiple upload in cloud not stable 