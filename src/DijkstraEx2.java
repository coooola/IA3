import java.util.*;

import java.io.*;


//Dijkstra algorithm

public class DijkstraEx2 {
    
    static class Cell{  
    	//No more heuristic cost
        int finalCost = 0; //G+H
        int cost = 0; //Basic cost of the cell
        int i, j;//Coordinates
        Cell parent; //Useful to make a dynamic list of the final path.
        
        //Constructor
        Cell(int i, int j){
            this.i = i;
            this.j = j;
        }
        
        @Override
        public String toString(){
            return "["+this.i+", "+this.j+"]";
        }
    }
    
    //Blocked cells are just null Cell values in grid
    static Cell [][] grid;
    
    static PriorityQueue<Cell> open;
     
    static boolean closed[][];
    //Coordinates of start point.
    static int startI, startJ;
  //Coordinates of end point.
    static int endI, endJ;
            
    //Block a cell in the grid.
    public static void setBlocked(int i, int j){
        grid[i][j] = null;
    }
    
    //Set coordinates of the starting cell.
    public static void setStartCell(int i, int j){
        startI = i;
        startJ = j;
    }
    
    //Set coordinates of the ending cell.
    public static void setEndCell(int i, int j){
        endI = i;
        endJ = j; 
    }
    
    //Check and update the cost of the current cell with the cost in parameter.
    static void checkAndUpdateCost(Cell current, Cell t, int cost){
        if(t == null || closed[t.i][t.j])return;
        int t_final_cost = cost; //modification of the calcul of the final cost.
        
        boolean inOpen = open.contains(t);
        if(!inOpen || t_final_cost<t.finalCost){
            t.finalCost = t_final_cost;
            t.parent = current;
            if(!inOpen)open.add(t);
        }
    }
    

    //Dijkstra algorithm.
	public static void Dijkstra(){ 

		//add the start location to open list.
		open.add(grid[startI][startJ]);

		Cell current;

		while(true){ 
			current = open.poll();
			
			//if there is not more cell in the queue.
			if(current==null)break;
			closed[current.i][current.j]=true; 
			
			if(current.equals(grid[endI][endJ])){
				return; 
			} 

			//Checking around the current cell
			Cell t;  
			if(current.i-1>=0){
				t = grid[current.i-1][current.j];
				checkAndUpdateCost(current, t, current.finalCost+current.cost); 
			} 

			if(current.j-1>=0){
				t = grid[current.i][current.j-1];
				checkAndUpdateCost(current, t, current.finalCost+current.cost); 
			}

			if(current.j+1<grid[0].length){
				t = grid[current.i][current.j+1];
				checkAndUpdateCost(current, t, current.finalCost+current.cost); 
			}

			if(current.i+1<grid.length){
				t = grid[current.i+1][current.j];
				checkAndUpdateCost(current, t, current.finalCost+current.cost);   
			}
		} 
	}

	/*
    Params :
    x, y = Board's dimensions
    si, sj = start location's x and y coordinates
    ei, ej = end location's x and y coordinates
    ArrayList<Cell> blocked = list containing inaccessible cell coordinates
    FileName = where we have to put the result
    */
    public static void test(int x, int y, int si, int sj, int ei, int ej, ArrayList<Cell> blocked, String fileName){
            //Reset
           closed = new boolean[x][y];
           open = new PriorityQueue<>((Object o1, Object o2) -> {
                Cell c1 = (Cell)o1;
                Cell c2 = (Cell)o2;

			return c1.finalCost<c2.finalCost?-1:
				c1.finalCost>c2.finalCost?1:0;
		});
		//Set start position
		setStartCell(si, sj);  //Setting to 0,0 by default. Will be useful for the UI part

		//Set End Location
		setEndCell(ei, ej); 

		grid[si][sj].finalCost = 0;

		/*
             Set blocked cells. Simply set the cell values to null
             for blocked cells.
           */
           for(int i=0;i<blocked.size();++i){
               setBlocked(blocked.get(i).i, blocked.get(i).j);
           }
           
           
           //Display initial map
           /**
           System.out.println("Grid: ");
           for(int i=0;i<x;++i){
               for(int j=0;j<y;++j){
                  if(i==si&&j==sj)System.out.print("SO  "); //Source
                  else if(i==ei && j==ej)System.out.print("DE  ");  //Destination
                  else if(grid[i][j]!=null)System.out.printf("%-3d ", 0);
                  else System.out.print("BL  "); 
               }
               System.out.println();
           } 
           System.out.println();
           **/
           
           Dijkstra();
          
           //Display score of cells at the end if you want
           System.out.println("\nScores for cells: ");
           for(int i=0;i<x;++i){
               for(int j=0;j<y;++j){
                   if(grid[i][j]!=null)System.out.printf("%-3d ", grid[i][j].finalCost);
                   else System.out.print("BL  ");
               }
               System.out.println();
           }
           System.out.println();
           
           //Display the path.
            /**
           if(closed[endI][endJ]){
               //Trace back the path 
                System.out.println("Path: ");
                Cell current = grid[endI][endJ];
                System.out.print(current);
                while(current.parent!=null){
                    System.out.print(" -> "+current.parent);
                    current = current.parent;
                } 
                System.out.println();
           }else System.out.println("No possible path");
           **/
           
          
          if(closed[endI][endJ]){
	          final File resultFile =new File(fileName); 
	          try {
	        	  //Creation of the file result
	        	  resultFile.createNewFile();
	        	  //creation of the writer
	              final FileWriter writer = new FileWriter(resultFile);
	              try {
	            	  for(int i=0;i<x;++i){
	                      for(int j=0;j<y;++j){
	                         if(i==si&&j==sj)
	                        	 writer.write("A");
	                         else if(i==ei && j==ej)
	                        	 writer.write("B");
	                         else if (pathContainCell(i, j, grid[endI][endJ]))
	                        	 writer.write("O");
	                         else if(grid[i][j]!=null)
	                        	 writer.write(".");
	                         else
	                        	 writer.write("#");
	                      }
	                      writer.write("\n");
	                  } 
	                  System.out.println("Result created ! You can find the result in " + fileName );
	              } finally {
	            	  // We close the file in any case.
	                  writer.close();
	                  
	              }
	          } catch (Exception e) {
	              System.out.println("The program was not able to create the file.");
	          }
          }
          else 
          {
        	  System.out.println("No possible path. The result file was not created.");
          }
        	  
    }
    
    //Return true if a cell is in the final path.
    public static boolean pathContainCell(int i, int j, Cell c)
    {
    	Cell current = c;
    	while(current.parent != null && (current.i != i || current.j != j)){
            current = current.parent;
        }
    	return(current != null && current.i == i && current.j == j);
    }
     
    public static void main(String[] args) throws Exception{
    	ReadFromFileUsingScanner("board-2-1.txt");
        ReadFromFileUsingScanner("board-2-2.txt");
        ReadFromFileUsingScanner("board-2-3.txt");
        ReadFromFileUsingScanner("board-2-4.txt");
    }
    
    //Used to read from the txt file the board.
	public static void ReadFromFileUsingScanner(String fileName) throws IOException {
		File file = new File(fileName);
		Scanner sc = new Scanner(file);
		
		//Variable used to stock datas from the file
		int size_of_grid = 0;
		int nb_of_BL =0 ;
		ArrayList<Cell> list_blocked = new ArrayList<Cell>();	
		int xb = 0;
		int xa=0;
		int ya =0;
		int yb =0;
		int x =0;
		int x_size;
		int y_size;
		
		//Reading a first time the file to get starting point, ending point and get the size of the board.
		while(sc.hasNextLine()){
			
			char[] charArray = sc.nextLine().toCharArray();
			size_of_grid=charArray.length;
			for (int i =0; i< charArray.length;i++) {
				//Getting starting cell
				if (charArray[i] == 'A'){
					xa=x;
					ya=i;
				}
				//Getting ending cell
				if (charArray[i] == 'B'){
					xb=x;
					yb=i;
				}
				//Getting blocked cell	
				if (charArray[i] == '#'){
					nb_of_BL++;
					
					list_blocked.add(new Cell(x,i));
				}
				
				
				
		
			}
			x++;
		}
			
		x_size=x;
		y_size=size_of_grid;
		
		grid = new Cell[x_size][y_size];
		x =0;
		
		Scanner sc2 = new Scanner(file);
		
		//Reading a second time the file to set the initial cost of the cell.
		while(sc2.hasNextLine()){
			
			char[] charArray = sc2.nextLine().toCharArray();
			size_of_grid=charArray.length;

			for (int i =0; i< charArray.length;i++) {
				grid[x][i] = new Cell(x, i);
				//moutain
				if (charArray[i] == 'm')
				{			
					grid[x][i].cost = 50;
				}
				//forest
				else if (charArray[i] == 'f')
				{
					grid[x][i].cost = 10;
				}
				//ground
				else if (charArray[i] == 'g')
				{
					grid[x][i].cost = 5;
				}
				//road
				else if (charArray[i] == 'r')
				{
					grid[x][i].cost = 1;
				}
				//water
				else if (charArray[i] == 'w')
				{
					grid[x][i].cost = 100;
				}
			}
			x++;
		}
		
		System.out.println("Coordinates of A : [ "+ xa +" ; "+ ya + " ]");
		System.out.println("Coordinates of B : [ "+ xb +" ; "+ yb + " ]");
		System.out.println("Number of blocked elements : " + nb_of_BL);
		System.out.println("List of blocked elements : " + list_blocked.toString());
		System.out.println("Size of the grid : [ " + x_size + " - " + size_of_grid + " ]");
		test(x_size, y_size, xa, ya, xb, yb, list_blocked, "result_dijkstra_" + fileName); 
		System.out.println();
		System.out.println();
	
		sc.close();
		sc2.close();
		
	}
}