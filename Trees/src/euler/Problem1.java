package euler;

class Problem1 {

    public static void main(String[] args){
    	Problem1 m = new Problem1();
        int sum = m.run(10);
        System.out.println("Result: "+sum);
        sum = m.run(10);
        assert sum==21;
    }

    private int run(int limit){
		//hold the result
		int sum=0;
	
	    //for each number
	    for(int i=0;i<=limit;i++){
	
	        //verify if is div by 3
			if (div3(i) || div5(i))
				sum=sum+i;        
	    	}
	    return sum;
	}

    private boolean div3(int num){

        if (num<10){
	        if (num==3||num==6||num==9)
	            return true;
	        return false;
        } else {
        	int prevSum = sumElements(num);
        	return div3(prevSum);
        }
    }

	private int sumElements(int num){
	    int sum=0;
	    String nums = num+"";
	    for (int i=0;i<nums.length();i++){
	        sum = sum+Integer.parseInt(""+nums.charAt(i));
	    }
	    return sum;
	}

    private boolean div5(int num){
        String nums = num+"";
        int last = Integer.parseInt(""+nums.charAt(nums.length()-1));
        if (last==5 || last==0)
            return true;
        else
            return false;
    }

}