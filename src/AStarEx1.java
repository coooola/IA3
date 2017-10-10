import java.util.*;
import java.io.*;


//source code from http://www.codebytes.in/2015/02/a-shortest-path-finding-algorithm.html
//We changed the algorithm so we don't take into account the diagonal

public class AStarEx1 {
    public static final int V_H_COST = 10; //Used of a static cost for the cell
    
    static class Cell{  
        int heuristicCost = 0; //Heuristic cost
        int finalCost = 0; //G+H
        int i, j; //Coordinates
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
    
    //Blocked cells are just null Cell values in grid.
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
        int t_final_cost = t.heuristicCost+cost;
        
        boolean inOpen = open.contains(t);
        if(!inOpen || t_final_cost<t.finalCost){
            t.finalCost = t_final_cost;
            t.parent = current;
            if(!inOpen)open.add(t);
        }
    }
    

    //Astar algorithm.
	public static void Astar(){ 

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
				checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
			} 

			if(current.j-1>=0){
				t = grid[current.i][current.j-1];
				checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
			}

			if(current.j+1<grid[0].length){
				t = grid[current.i][current.j+1];
				checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
			}

			if(current.i+1<grid.length){
				t = grid[current.i+1][current.j];
				checkAndUpdateCost(current, t, current.finalCost+V_H_COST);   
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
           grid = new Cell[x][y];
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

		for(int i=0;i<x;++i){
			for(int j=0;j<y;++j){
				grid[i][j] = new Cell(i, j);
				grid[i][j].heuristicCost = Math.abs(i-endI)+Math.abs(j-endJ);
				//                  System.out.print(grid[i][j].heuristicCost+" ");
			}
			//              System.out.println();
		}
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
           
           Astar();
           
           //Display score of cells at the end if you want
           /**
           System.out.println("\nScores for cells: ");
           for(int i=0;i<x;++i){
               for(int j=0;j<x;++j){
                   if(grid[i][j]!=null)System.out.printf("%-3d ", grid[i][j].finalCost);
                   else System.out.print("BL  ");
               }
               System.out.println();
           }
           System.out.println();
            
           //Display the path.
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
	              // Creation of the file result
	        	  resultFile.createNewFile();
	              // creation of the writer
	              final FileWriter writer = new FileWriter(resultFile);
	              try {
	            	  //Writing the result
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
        ReadFromFileUsingScanner("board-1-1.txt");
        ReadFromFileUsingScanner("board-1-2.txt");
        ReadFromFileUsingScanner("board-1-3.txt");
        ReadFromFileUsingScanner("board-1-4.txt");
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
		
		//Reading the file
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
		
		System.out.println("Coordinates of A : [ "+ xa +" ; "+ ya + " ]");
		System.out.println("Coordinates of B : [ "+ xb +" ; "+ yb + " ]");
		System.out.println("Number of blocked elements : " + nb_of_BL);
		System.out.println("List of blocked elements : " + list_blocked.toString());
		System.out.println("Size of the grid : [ " + x_size + " - " + size_of_grid + " ]");
		test(x_size, y_size, xa, ya, xb, yb, list_blocked, "result_" + fileName); 
		System.out.println();
		System.out.println();
		sc.close();
		
	}
}

