class FerParser {

	public static parser(def cadena){
		
		def array = cadena.split('\\$set')
		
		if (array.size()>1)
			return array[0]+'$set'+array[array.size()-1]
		else
			return cadena
			
	}

	
	public static void main(String[] args) {
		
		def cadena = '''
		[{ "ts" : { "$ts" : 1360957646 , "$inc" : 10} , "h" : 4439393001036361651 , "v" : 2 , "op" : "u" , "ns" : "sales.sales" , "o2" : { "_id" : 749801282} , "o" : { "$set" : { "feedback.received" : { "item" : { "id" : "MLB460418570" , "seller_id" : 17285363} , "from" : 64342308 , "to" : 17285363 , "date_created" : { "$date" : "2013-02-08T18:58:03.000Z"} , "date_last_updated" : { "$date" : "2013-02-15T19:47:25.000Z"} , "fulfilled" : true , "rating" : "positive" , "status" : "active"}} , "$set" : { "feedback.received" : { "item" : { "id" : "MLB460418570" , "seller_id" : 17285363} , "from" : 64342308 , "to" : 17285363 , "date_created" : { "$date" : "2013-02-08T18:58:03.000Z"} , "date_last_updated" : { "$date" : "2013-02-15T19:47:25.000Z"} , "fulfilled" : true , "rating" : "positive" , "status" : "active"}} , "$set" : { "feedback.received" : { "item" : { "id" : "MLB460418570" , "seller_id" : 17285363} , "from" : 64342308 , "to" : 17285363 , "date_created" : { "$date" : "2013-02-08T18:58:03.000Z"} , "date_last_updated" : { "$date" : "2013-02-15T19:47:25.000Z"} , "fulfilled" : true , "rating" : "positive" , "status" : "active"}} , "$set" : { "feedback.received" : { "item" : { "id" : "MLB460418570" , "seller_id" : 17285363} , "from" : 64342308 , "to" : 17285363 , "date_created" : { "$date" : "2013-02-08T18:58:03.000Z"} , "date_last_updated" : { "$date" : "2013-02-15T19:47:25.000Z"} , "fulfilled" : true , "rating" : "positive" , "status" : "active"}}}}]
		'''
		
		println FerParser.parser(cadena)
		
		cadena = '''
		[{ "ts" : { "$ts" : 1360957646 , "$inc" : 10} , "h" : 4439393001036361651 , "v" : 2 , "op" : "u" , "ns" : "sales.sales" , "o2" : { "_id" : 749801282} , "o" : { "$ret" : { "feedback.received" : { "item" : { "id" : "MLB460418570" , "seller_id" : 17285363} , "from" : 64342308 , "to" : 17285363 , "date_created" : { "$date" : "2013-02-08T18:58:03.000Z"} , "date_last_updated" : { "$date" : "2013-02-15T19:47:25.000Z"} , "fulfilled" : true , "rating" : "positive" , "status" : "active"}}}}]
		'''
		
		println FerParser.parser(cadena)

		cadena = '''
		[{ "ts" : { "$ts" : 1360957646 , "$inc" : 10} , "h" : 4439393001036361651 , "v" : 2 , "op" : "u" , "ns" : "sales.sales" , "o2" : { "_id" : 749801282} , "o" : { "$set" : { "feedback.received" : { "item" : { "id" : "MLB460418570" , "seller_id" : 17285363} , "from" : 64342308 , "to" : 17285363 , "date_created" : { "$date" : "2013-02-08T18:58:03.000Z"} , "date_last_updated" : { "$date" : "2013-02-15T19:47:25.000Z"} , "fulfilled" : true , "rating" : "positive" , "status" : "active"}}}}]
		'''
		
		println FerParser.parser(cadena)
		
	}
			
}
