import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JFrame;

public class MyMaze extends JPanel implements ActionListener{
  private int dimensionX, dimensionY; // dimension of maze
  private int gridDimensionX, gridDimensionY; // dimension of output grid
  private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);
  private char[][] grid; // output grid
  private Cell[][] cells; // 2d array of Cells
  private Random random = new Random(); // The random object
  private final int PACMAN_SPEED = 6;
  private Image ii;
  private final Color dotColor = new Color(192, 192, 0);
  private Color mazeColor;
  private boolean dying = false;
  private boolean inGame= false;
  private int pacAnimCount;
  private int pacAnimDir = 1;
  private int pacmanAnimPos = 0;
  private int pacsLeft, score;
  private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
  private Image pacman3up, pacman3down, pacman3left, pacman3right;
  private Image pacman4up, pacman4down, pacman4left, pacman4right;
  private Dimension d; 
  private final int maxSpeed = 6;
  private final int SCREEN_SIZE = 360;
  private final int BLOCK_SIZE = 24;
  private final int N_BLOCKS = 15;
  private int pacman_x, pacman_y, pacmand_x, pacmand_y;
  private int req_dx, req_dy, view_dx, view_dy;
  private int[] dx, dy;
  private int currentSpeed = 3;
  private short[] screenData;
  private boolean firstGrid = true;
  private boolean showPath = false;
  private int Score = 0;
  
  // initialize with x and y the same
  public MyMaze(int aDimension) {
      // Initialize
      this(aDimension, aDimension);
      loadImages();
      d = new Dimension(aDimension, aDimension);
      initMyMaze();
      initVariables();
  }

  public void initMyMaze(){
    setFocusable(true);
    setBackground(Color.black);
    setDoubleBuffered(true);
  }
 
  private void initVariables(){
    mazeColor= new Color(5, 100, 5);
    dx = new int[4];
    dy = new int[4];
    screenData = new short[N_BLOCKS * N_BLOCKS];
  }

  @Override
    public void addNotify() {
        super.addNotify();
        initGame();
    }
    //  private void doAnim() {

    //     pacAnimCount--;

    //     if (pacAnimCount <= 0) {
    //         pacAnimCount = PAC_ANIM_DELAY;
    //         pacmanAnimPos = pacmanAnimPos + pacAnimDir;

    //         if (pacmanAnimPos == (PACMAN_ANIM_COUNT - 1) || pacmanAnimPos == 0) {
    //             pacAnimDir = -pacAnimDir;
    //         }
    //     }
    // }
    private void playGame(Graphics2D g2d) {

        if (dying) {
            death();
        } else {
            movePacman();
            drawPacman(g2d);
            // checkMaze();
        }
    }
    
    private void showIntroScreen(Graphics2D g2d) {
        g2d.setColor(new Color(0, 32, 48));
        g2d.fillRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);
        g2d.setColor(Color.white);
        g2d.drawRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);
        String s = "Press s to start.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);
        g2d.setColor(Color.white);
        g2d.setFont(small);
        g2d.drawString(s, (SCREEN_SIZE - metr.stringWidth(s)) / 2, SCREEN_SIZE / 2);
    }
    private void drawScore(Graphics2D g) {
        int i;
        String s;
        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);
        for (i = 0; i < pacsLeft; i++) {
            g.drawImage(pacman3left, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }
    private void death() {
        pacsLeft--;
        if (pacsLeft == 0) {
            inGame = false;
        }
        continueLevel();
    }

    private void movePacman() {
        int pos;
        short ch;
        if (req_dx == -pacmand_x && req_dy == -pacmand_y) {
            pacmand_x = req_dx;
            pacmand_y = req_dy;
            view_dx = pacmand_x;
            view_dy = pacmand_y;
        }
        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
            ch = screenData[pos];
            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                score++;
            }
            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                    view_dx = pacmand_x;
                    view_dy = pacmand_y;
                }
            }
            // Check for standstill
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        }
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;
    }

// PACMAN DRAWINGS STARTING FROM HERE. WE CAN CLEARLY SEE THE PACMAN MOVING IN FROM HERE.
    private void drawPacman(Graphics2D g2d) {
        if (view_dx == -1) {
            drawPacmanLeft(g2d);
        } else if (view_dx == 1) {
            drawPacmanRight(g2d);
        } else if (view_dy == -1) {
            drawPacmanUp(g2d);
        } else {
            drawPacmanDown(g2d);
        }
    }
    private void drawPacmanUp(Graphics2D g2d) {
        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2up, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3up, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4up, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacmanDown(Graphics2D g2d) {
        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2down, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3down, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4down, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacmanLeft(Graphics2D g2d) {
        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2left, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3left, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4left, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacmanRight(Graphics2D g2d) {
        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2right, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3right, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4right, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }
  // constructor
  public MyMaze(int xDimension, int yDimension) {
      dimensionX = xDimension;
      dimensionY = yDimension;
      gridDimensionX = xDimension * 4 + 1;
      gridDimensionY = yDimension * 2 + 1;
      grid = new char[gridDimensionX][gridDimensionY];
      init();
      generateMaze();
  }

  private void init() {
      // create cells
      cells = new Cell[dimensionX][dimensionY];
      for (int x = 0; x < dimensionX; x++) {
          for (int y = 0; y < dimensionY; y++) {
              cells[x][y] = new Cell(x, y, false); // create cell (see Cell constructor)
          }
      }
  }

  // inner class to represent a cell
  private class Cell {
    int x, y; // coordinates
    // cells this cell is connected to
    ArrayList<Cell> neighbors = new ArrayList<>();
    // solver: if already used
    boolean visited = false;
    // solver: the Cell before this one in the path
    Cell parent = null;
    // solver: if used in last attempt to solve path
    boolean inPath = false;
    // solver: distance travelled this far
    double travelled;
    // solver: projected distance to end
    double projectedDist;
    // impassable cell
    boolean wall = true;
    // if true, has yet to be used in generation
    boolean open = true;
    // construct Cell at x, y
    Cell(int x, int y) {
        this(x, y, true);
    }
    // construct Cell at x, y and with whether it isWall
    Cell(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.wall = isWall;
    }
    // add a neighbor to this cell, and this cell as a neighbor to the other
    void addNeighbor(Cell other) {
        if (!this.neighbors.contains(other)) { // avoid duplicates
            this.neighbors.add(other);
        }
        if (!other.neighbors.contains(this)) { // avoid duplicates
            other.neighbors.add(this);
        }
    }
    // used in updateGrid()
    boolean isCellBelowNeighbor() {
        return this.neighbors.contains(new Cell(this.x, this.y + 1));
    }
    // used in updateGrid()
    boolean isCellRightNeighbor() {
        return this.neighbors.contains(new Cell(this.x + 1, this.y));
    }
    // useful Cell representation
    @Override
    public String toString() {
        return String.format("Cell(%s, %s)", x, y);
    }
    // useful Cell equivalence
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Cell)) return false;
        Cell otherCell = (Cell) other;
        return (this.x == otherCell.x && this.y == otherCell.y);
    }
    // should be overridden with equals
    @Override
    public int hashCode() {
        // random hash code method designed to be usually unique
        return this.x + this.y * 256;
    }
  }
  // generate from upper left (In computing the y increases down often)
  private void generateMaze() {
      generateMaze(0, 0);
  }
  // generate the maze from coordinates x, y
  private void generateMaze(int x, int y) {
      generateMaze(getCell(x, y)); // generate from Cell
  }
  private void generateMaze(Cell startAt) {
      // don't generate from cell not there
      if (startAt == null) return;
      startAt.open = false; // indicate cell closed for generation
      ArrayList<Cell> cells = new ArrayList<>();
      cells.add(startAt);

      while (!cells.isEmpty()) {
          Cell cell;
          // this is to reduce but not completely eliminate the number
          //   of long twisting halls with short easy to detect branches
          //   which results in easy mazes
          if (random.nextInt(10)==0)
              cell = cells.remove(random.nextInt(cells.size()));
          else cell = cells.remove(cells.size() - 1);
          // for collection
          ArrayList<Cell> neighbors = new ArrayList<>();
          // cells that could potentially be neighbors
          Cell[] potentialNeighbors = new Cell[]{
              getCell(cell.x + 1, cell.y),
              getCell(cell.x, cell.y + 1),
              getCell(cell.x - 1, cell.y),
              getCell(cell.x, cell.y - 1)
          };
          for (Cell other : potentialNeighbors) {
              // skip if outside, is a wall or is not opened
              if (other==null || other.wall || !other.open) continue;
              neighbors.add(other);
          }
          if (neighbors.isEmpty()) continue;
          // get random cell
          Cell selected = neighbors.get(random.nextInt(neighbors.size()));
          // add as neighbor
          selected.open = false; // indicate cell closed for generation
          cell.addNeighbor(selected);
          cells.add(cell);
          cells.add(selected);
      }
  }
  // used to get a Cell at x, y; returns null out of bounds
  public Cell getCell(int x, int y) {
      try {
          return cells[x][y];
      } catch (ArrayIndexOutOfBoundsException e) { // catch out of bounds
          return null;
      }
  }

  public void solve() {
      // default solve top left to bottom right
      this.solve(0, 0, dimensionX - 1, dimensionY -1);
  }
  // solve the maze starting from the start state (A-star algorithm)
  public void solve(int startX, int startY, int endX, int endY) {
      // re initialize cells for path finding
      for (Cell[] cellrow : this.cells) {
          for (Cell cell : cellrow) {
              cell.parent = null;
              cell.visited = false;
              cell.inPath = false;
              cell.travelled = 0;
              cell.projectedDist = -1;
          }
      }
      // cells still being considered
      ArrayList<Cell> openCells = new ArrayList<>();
      // cell being considered
      Cell endCell = getCell(endX, endY);
      if (endCell == null) return; // quit if end out of bounds
      { // anonymous block to delete start, because not used later
          Cell start = getCell(startX, startY);
          if (start == null) return; // quit if start out of bounds
          start.projectedDist = getProjectedDistance(start, 0, endCell);
          start.visited = true;
          openCells.add(start);
      }
      boolean solving = true;
      while (solving) {
          if (openCells.isEmpty()) return; // quit, no path
          // sort openCells according to least projected distance
          Collections.sort(openCells, new Comparator<Cell>(){
              @Override
              public int compare(Cell cell1, Cell cell2) {
                  double diff = cell1.projectedDist - cell2.projectedDist;
                  if (diff > 0) return 1;
                  else if (diff < 0) return -1;
                  else return 0;
              }
          });
          Cell current = openCells.remove(0); // pop cell least projectedDist
          if (current == endCell) break; // at end
          for (Cell neighbor : current.neighbors) {
              double projDist = getProjectedDistance(neighbor,
                      current.travelled + 1, endCell);
              if (!neighbor.visited || // not visited yet
                      projDist < neighbor.projectedDist) { // better path
                  neighbor.parent = current;
                  neighbor.visited = true;
		  Score++;
                  neighbor.projectedDist = projDist;
                  neighbor.travelled = current.travelled + 1;
                  if (!openCells.contains(neighbor))
                      openCells.add(neighbor);
              }
          }
      }

      // private void checkMaze() {

      //   short i = 0;
      //   boolean finished = true;

      //   while (i < N_BLOCKS * N_BLOCKS && finished) {

      //       if ((screenData[i] & 48) != 0) {
      //           finished = false;
      //       }

      //       i++;
      //     } if (finished) {

     //         score += 50;

      //         if (N_GHOSTS < MAX_GHOSTS) {
      //             N_GHOSTS++;
      //         }

      //         if (currentSpeed < maxSpeed) {
      //             currentSpeed++;
      //         }

      //         initLevel();
      //     }
      // }
      //create path from end to beginning
      Cell backtracking = endCell;
      backtracking.inPath = true;
      while (backtracking.parent != null) {
          backtracking = backtracking.parent;
          backtracking.inPath = true;
      }
  // get the projected distance
  // (A star algorithm consistent)
  } 
  public double getProjectedDistance(Cell current, double travelled, Cell end) {
      return travelled + Math.abs(current.x - end.x) + 
              Math.abs(current.y - current.x);
  }

  // draw the maze
  public void updateGrid() {
      char backChar = ' ', wallChar = 'x', cellChar = ' ', pathChar = ' ';
      if(showPath){
	      pathChar = '*';
	      showPath = false;
      }
      // fill background
      for (int x = 0; x < gridDimensionX; x ++) {
          for (int y = 0; y < gridDimensionY; y ++) {
              grid[x][y] = backChar;
          }
      }
      // build walls
      for (int x = 0; x < gridDimensionX; x ++) {
          for (int y = 0; y < gridDimensionY; y ++) {
              if (x % 4 == 0 || y % 2 == 0)
                  grid[x][y] = wallChar;
          }
      }
      // make meaningful representation
      for (int x = 0; x < dimensionX; x++) {
          for (int y = 0; y < dimensionY; y++) {
              Cell current = getCell(x, y);
              int gridX = x * 4 + 2, gridY = y * 2 + 1;
              if (current.inPath) {
                  grid[gridX][gridY] = pathChar;
                  if (current.isCellBelowNeighbor())
                      if (getCell(x, y + 1).inPath) {
                          grid[gridX][gridY + 1] = pathChar;
                          grid[gridX + 1][gridY + 1] = backChar;
                          grid[gridX - 1][gridY + 1] = backChar;
                      } else {
                          grid[gridX][gridY + 1] = cellChar;
                          grid[gridX + 1][gridY + 1] = backChar;
                          grid[gridX - 1][gridY + 1] = backChar;
                      }
                  if (current.isCellRightNeighbor())
                      if (getCell(x + 1, y).inPath) {
                          grid[gridX + 2][gridY] = pathChar;
                          grid[gridX + 1][gridY] = pathChar;
                          grid[gridX + 3][gridY] = pathChar;
                      } else {
                          grid[gridX + 2][gridY] = cellChar;
                          grid[gridX + 1][gridY] = cellChar;
                          grid[gridX + 3][gridY] = cellChar;
                      }
              } else {
                  grid[gridX][gridY] = cellChar;
                  if (current.isCellBelowNeighbor()) {
                      grid[gridX][gridY + 1] = cellChar;
                      grid[gridX + 1][gridY + 1] = backChar;
                      grid[gridX - 1][gridY + 1] = backChar;
                  }
                  if (current.isCellRightNeighbor()) {
                      grid[gridX + 2][gridY] = cellChar;
                      grid[gridX + 1][gridY] = cellChar;
                      grid[gridX + 3][gridY] = cellChar;
                  }
              }
          }
      }
      grid[1][0] = ' ';
      grid[gridDimensionX-1][gridDimensionY-2] = ' ';
  }
  // simply prints the map
  public void draw() {
      System.out.print(this);
  }
  // forms a meaningful representation
  @Override
  public String toString() {
      if (firstGrid){
	      updateGrid();
	      firstGrid = false;
      }
      String output = "";
      for (int y = 0; y < gridDimensionY; y++) {
          for (int x = 0; x < gridDimensionX; x++) {
              output += grid[x][y];
          }
          output += "\n";
      }
      return output;
  }

  // run it
  public static void main(String[] args) {
      MyMaze maze = new MyMaze(20);
      maze.solve();
      int score = 0;
      char path = '*';
      int currentX = 1;
      int currentY = 0;
      boolean flag = false;
      Scanner sc = new Scanner(System.in).useDelimiter("\\s*");
      while(true){
	      	System.out.print("\033[H\033[2J");		//Clears the console 
    		System.out.flush(); 
      		if (currentX == (maze.gridDimensionX - 1) && currentY == (maze.gridDimensionY - 2)){
			System.out.println("You win\n You Score: " + (score - maze.Score));
			break;
	      	}
		maze.draw();
		char a = sc.next().charAt(0);
		switch (a) {
			case 'A':
				if (currentY -1 >= 0 && (maze.grid[currentX][currentY-1] == ' ' || maze.grid[currentX][currentY-1] == path)){
					currentY = currentY - 1;
					if (maze.grid[currentX][currentY] == path){
						maze.grid[currentX][currentY] = '&';
					}else{
						maze.grid[currentX][currentY] = path;
					}
				}
				score++;
				break;
			case 'D':
				if (currentX - 1 >= 0 && (maze.grid[currentX-1][currentY] == ' ' || maze.grid[currentX-1][currentY] == path )){
					currentX = currentX - 1;
					if (maze.grid[currentX][currentY] == path){
						maze.grid[currentX][currentY] = '&';
					}else{
						maze.grid[currentX][currentY] = path;
					}
				}
				score++;
				break;
			case 'B':
				if (currentY+1 < maze.dimensionY && maze.grid[currentX][currentY+1] == ' ' || (maze.grid[currentX][currentY+1] == path)){
					currentY = currentY + 1;
					if (maze.grid[currentX][currentY] == path){
						maze.grid[currentX][currentY] = '&';
					}else{
						maze.grid[currentX][currentY] = path;
					}
				}
				score++;
				break;
			case 'C':
				if (currentX+1 < maze.dimensionX && (maze.grid[currentX+1][currentY] == ' ' || maze.grid[currentX+1][currentY] == path)){
					currentX = currentX + 1;
					if (maze.grid[currentX][currentY] == path){
						maze.grid[currentX][currentY] = '&';
					}else{
						maze.grid[currentX][currentY] = path;
					}
				}
				score++;
				break;
			case 'z':
				maze.firstGrid = true;
				maze.showPath = true;
				System.out.print("\033[H\033[2J");		//Clears the console 
    				System.out.flush(); 
				System.out.println("\t\t\t\tYOU LOSE!!!");
				sc.next();
				System.out.print("\033[H\033[2J");		//Clears the console 
    				System.out.flush(); 
				maze.draw();
				flag = true;
				break;
			default:
		}
		if (flag)
			break;
      }
  }

 private void initGame() {

        pacsLeft = 3;
        score = 0;
        // initLevel();
        currentSpeed = 3;
    }

    // private void initLevel() {

    //     int i;
    //     for (i = 0; i <225; i++) {
    //         screenData[i] = levelData[i];
    //     }

    //     continueLevel();
    // }
    private void continueLevel() {
        pacman_x = 7 * BLOCK_SIZE;
        pacman_y = 11 * BLOCK_SIZE;
        pacmand_x = 0;
        pacmand_y = 0;
        req_dx = 0;
        req_dy = 0;
        view_dx = -1;
        view_dy = 0;
        dying = false;

    }
    private void loadImages() {
        pacman1 = pacman2up = pacman2left = pacman2right = pacman2down = pacman3up = pacman3down = pacman3left = pacman3right = pacman4up = pacman4down = pacman4left = pacman4right = new ImageIcon("/home/shashwat/Programs/Web/typescript-maze/game/942849.original.png").getImage();
    }
     @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        // drawMaze(g2d);
        // drawScore(g2d);
        // doAnim();

        if (inGame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }

        g2d.drawImage(ii, 5, 5, this);
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }
    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE) {
                    inGame = false;
                } else {
                	if (key == 's' || key == 'S') {
                	    inGame = true;
                	    initGame();
                	}
            	}
       	    }
	}
        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == Event.LEFT || key == Event.RIGHT
                    || key == Event.UP || key == Event.DOWN) {
                req_dx = 0;
                req_dy = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        repaint();
    }
}
