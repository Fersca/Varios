//Hello, World

println "Hello World"
println 3+4+5+6

//Variables

x = 3
y = 4
z = x*y
println z

//Lists and Maps

myList = [3,4,5,6,7,8]
println myList[2]
println myList.size()

myMap = ["pablo":"Molnar","Ignacio":4,"Matias":"Waisgold"] //Can be of different types
println myMap["pablo"]
println myMap["Ignacio"]
myMap["Ignacio"] = 5
println myMap["Ignacio"]
println myMap."Ignacio" //you can access the map like this because the . is overriden

mySecondArrayEmpty = []
mySecondMapEmpty = [:]

println mySecondArrayEmpty.size()
println mySecondMapEmpty.size()

//Conditional Execution

if (x<y){
	println "Y larger than X"	
} else {
	println "X larger than Y"
}

//Differences from java:
/*
 * == means equals. so you can use it as "Fernando".equals("fernando") as "Fernando"=="Fernando"
 * if you need to execute the == in groovy to check the identify, you can use "is"
 * 
 */

if ("fernando"=="fernando"){
	println "Same name"
} 

if (!"fernando".is("Fernando")){
	println "Not same identity"
}

//Another considerations
/*
 * Return stament is optional
 * if you use "this" in static methods it will refers to this class
 * methods are public by default
 *  
 * 
*/