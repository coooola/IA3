import java.util.*;
import java.io.*;


//source code from http://www.codebytes.in/2015/02/a-shortest-path-finding-algorithm.html
//We changed the algorithm so we don't take into account the diagonal

public class AStarEx1 {
    public static final int DIAGONAL_COST = 14;
    public static final int V_H_COST = 10;
    
    static class Cell{  
        int heuristicCost = 0; //Heuristic cost
        int finalCost = 0; //G+H
        int i, j;
        Cell parent; 
        
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
    static Cell [][] grid = new Cell[5][5];
    
    static PriorityQueue<Cell> open;
     
    static boolean closed[][];
    static int startI, startJ;
    static int endI, endJ;
            
    public static void setBlocked(int i, int j){
        grid[i][j] = null;
    }
    
    public static void setStartCell(int i, int j){
        startI = i;
        startJ = j;
    }
    
    public static void setEndCell(int i, int j){
        endI = i;
        endJ = j; 
    }
    
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
    


	public static void Astar(){ 

		//add the start location to open list.
		open.add(grid[startI][startJ]);

		Cell current;

		while(true){ 
			current = open.poll();
			if(current==null)break;
			closed[current.i][current.j]=true; 

			if(current.equals(grid[endI][endJ])){
				return; 
			} 

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
    tCase = test case No.
    x, y = Board's dimensions
    si, sj = start location's x and y coordinates
    ei, ej = end location's x and y coordinates
    int[][] blocked = array containing inaccessible cell coordinates
    */
    public static void test(int tCase, int x, int y, int si, int sj, int ei, int ej, ArrayList<Cell> blocked, String fileName){
           System.out.println("\n\nTest Case #"+tCase);
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
	              // Creation du fichier
	        	  resultFile.createNewFile();
	              // creation d'un writer (un �crivain)
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
	                  // quoiqu'il arrive, on ferme le fichier
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
    
    public static boolean pathContainCell(int i, int j, Cell c)
    {
    	Cell current = c;
    	while(current.parent != null && (current.i != i || current.j != j)){
            current = current.parent;
        }
    	return(current != null && current.i == i && current.j == j);
    }
     
    public static void main(String[] args) throws Exception{
    	ArrayList<Cell> list = new ArrayList<Cell>();
    	list.add(new Cell(0,4));
    	list.add(new Cell(2,2));
    	list.add(new Cell(3,1));
    	list.add(new Cell(3,3));
    	list.add(new Cell(4,1));
        test(1, 5, 5, 0, 0, 3, 2, list, "result1.txt"); 
        /*test(2, 5, 5, 0, 0, 4, 4, new int[][]{{0,4},{2,2},{3,1},{3,3}}, "result2.txt");   
        test(3, 7, 7, 2, 1, 5, 4, new int[][]{{4,1},{4,3},{5,3},{2,3}}, "result3.txt");
        
        test(1, 5, 5, 0, 0, 4, 4, new int[][]{{3,4},{3,3},{4,3}}, "result4.txt");*/
        ReadFromFileUsingScanner("board-1-1.txt");
    }
	public static void ReadFromFileUsingScanner(String fileName) throws IOException {
		File file = new File(fileName);
		Scanner sc = new Scanner(file);
		int nb_of_A =0 ;
		int size_of_grid = 0;
		int nb_of_B =0 ;
		int nb_of_BL =0 ;
		ArrayList<Cell> list_blocked = new ArrayList<Cell>();
		//int x = 0;
		//int y=0;
		int xb = 0;
		int xa=0;
		int ya =0;
		int yb =0;
		int x =0;
		int x_size;
		int y_size;
		while(sc.hasNextLine()){
			
			char[] charArray = sc.nextLine().toCharArray();
			//int length = sc.nextLine().length();
			size_of_grid=charArray.length - 1 ; // We dont want '\n' being counted 

				//System.out.println(charArray[i]);
			for (int i =0; i< charArray.length;i++) {
				if (charArray[i] == 'A'){
					nb_of_A++;
					xa=x;
					ya=i;
				}
				if (charArray[i] == 'B'){
					nb_of_B++;
					xb=x;
					yb=i;
				}
					
				if (charArray[i] == '#'){
					nb_of_BL++;
					
					list_blocked.add(new Cell(x,i));
				}
				
		
			}
			x++;
		}
		x_size=x;
		y_size=size_of_grid;
		
		System.out.println("Coord de A " + "X:" + xa +"  "+  "Y:"+ ya);
		System.out.println("Nb of A " + nb_of_A);
		System.out.println("Nb of B " + nb_of_B);
		System.out.println("Coord de B " + "X:" + xb +"  "+  "Y:"+ yb);
		System.out.println("Nb of BL " + nb_of_BL);
		System.out.println("LA LISTE DES ELEMENTS BLOKES " + list_blocked.toString());
		System.out.println("Size of grid " + size_of_grid);
		System.out.println("X et Y de la grille : " + x_size + size_of_grid);
		test(1, x_size, y_size, xa, ya, xb, yb, list_blocked, "result8.txt"); 
		
		sc.close();
		
	}
}

