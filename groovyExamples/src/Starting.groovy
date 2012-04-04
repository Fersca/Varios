//Ranges:
(1..10).each {println it}

//convert data to one type to ahother with "as"
num = '100' as Integer
println "clase: "+num.class 

//define an array with the same type of elements
def xArr = ['97', '98', '99'] as Integer[]
def yArr = [2,2,3,2] as ArrayList
def zArr = [2,2,3,2] //The default array is from ArrayList class

println 'Tipo: '+xArr.class
println 'Tipo: '+yArr.class
println 'Tipo: '+zArr.class

//executing several times:
10.times {
	print "Hola"
}

//execute code block
def c= { def a= 5, b= 9; a * b }
assert c() == 45

//create threads in the middle of the code
def result = ""
Thread.start {
	10.times {
		result=result+ '1'
		sleep 1
	}
}
10.times{
	result=result+ '2'
	sleep 1
}
println result