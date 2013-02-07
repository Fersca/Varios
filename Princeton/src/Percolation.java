public class Percolation {

		WeightedQuickUnionUF grid;
		boolean[][] opened;  
		int N;
		
		public Percolation(int N){
			grid = new WeightedQuickUnionUF((N*N)+2);
			opened = new boolean[N][N];
			this.N=N;
		}
		
		public void open(int i, int j){
		 			
			//calculate the node position
			int pos = getPosition(i,j);
		
			i--;
			j--;

			//mark this node as open
			opened[i][j]=true;
			
			//check if the left node is open, in this case, join
			if (j>0){
				if (opened[i][j-1]){
					grid.union(pos, pos-1);
				}
			}

			//check if the right node is open, in this case, join
			if (j<N-1){
				if (opened[i][j+1]){
					grid.union(pos, pos+1);
				}
			}
				
			//Union with the previous row
			if (i>0){
				//We are not in the first row
				if (opened[i-1][j]){
					//join the node with the previous row
					grid.union(pos, pos-N);
				}
			} else {
				//we are in the first row, join the node with the virtual top node
				grid.union(pos, 0);
			}
			
			//Union with the next line
			if (i<N-1){
				//we are not in the last row so we join with the next row node,
				if (opened[i+1][j]){
					grid.union(pos, pos+N);
				}
			} else {
				//the bottom virtual node
				grid.union(pos, grid.count()-1);
			}
			
		}
	   
		public boolean isOpen(int i, int j){
			return opened[i][j];
		}
	   
		public boolean isFull(int i, int j){
			return grid.connected(0, getPosition(i,j));
		}
	   
		public boolean percolates(){
			return grid.connected(0, grid.count()-1);
		}
	
		private int getPosition(int i, int j){
			return ((i-1)*N)+j;
		}
		
		public static void main(String[] args) {
		
			Percolation per = new Percolation(4);
			per.open(1, 1);
			per.open(1, 2);
			per.open(1, 3);
			per.open(1, 4);
			
			System.out.println(per.percolates());
		}
}
