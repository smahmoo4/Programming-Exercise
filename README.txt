__________________________________
__________________________________
**				**	
** SAAD MAHMOOD			**    		
** WELLSPRING CODE SUBMISSION	**
** MAY 24, 2015			**
**				**
__________________________________
__________________________________

============================
 HOW TO RUN THE APPLICATION
============================
> ***IMPORTANT***
    - Keep all files in project folder
    - CRUD functionality is merely a REPRESENTATION of a database, since I did not have access to a DB connection or server.

> EXECUTION
	1. Run Entry.java, which will create output.txt
	2. Execute CRUD Functionality
		- NOTE: any input should be kept in CSV format,
		- Example: “El, Blue Line, A000, JDoe” is accepted format.
	3. perform any CRUD functionality before step 4. 
	4. open WellSpring.html in the browser
	5. choose 'output.txt' as the file
	
============================
  WHAT THE PACKAGE CONTAINS
============================
> Entry.java			=> .java file which reads the 'trains.csv' file and executes program to create the output file
> output.txt			=>  a text file which holds the result of the operation to be processed on the webpage
> WellSpring.html		=>  basic webpage which allows file selection
> WellSpring.js			=> .js file which parses and loads the contents of output.txt into browser
> trains.csv			=>  original input file which is read by Entry.java, modified through CRUD functionality
> style.css			=>  .css file for UI purposes
