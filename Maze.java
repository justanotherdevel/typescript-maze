import  java.util.Random;

public class Maze {
	protected int maze[][];
	protected int nVertices;
	//Update connectionPos and noPos where noPos stands for the no. of pos.
	protected int connectionPos[][];
	protected int noPos;
	private int minConnection;
	private int maxConnection;
	Maze (int Vertices) {
		this.nVertices = Vertices;
		Random rand = new Random();
		int noConnection = rand.nextInt(this.maxConnection - this. minConnection) + this.minConnection;
		for (int i = 0; i < noConnection; i++) {
			int pos = rand.nextInt(this.noPos);
			this.maze[this.connectionPos[pos][0]][this.connectionPos[pos][1]] = rand.nextInt(10) + 1;
		}
	}
	
	public int extractWeight (int i, int j) {
		return this.maze[i][j];
	}

	private int minDistance (int dist[], boolean sptSet[]) {
		int min = Integer.MAX_VALUE;
		int mIndex = -1;
		for (int i = 0; i < this.nVertices; i++) {
			if (!sptSet[i] && dist[i] <= min) {
				min = dist[i];
				mIndex = i;
			}
		}
		return mIndex;
	}

	public boolean dijkstra (int src, int dest, int path[]) {
		int dist[] = new int[nVertices];
		boolean sptSet[] = new boolean[nVertices];
		for (int i = 0; i < this.nVertices; i++) {
			dist[i] = Integer.MAX_VALUE;
			sptSet[i] = false;
		}
		dist[src] = 0;
		for (int i = 0; i < this.nVertices - 1; i++) {
			int u = this.minDistance(dist, sptSet);
			sptSet[u] = true;
			for (int v = 0; v < this.nVertices; v++) {
				if (!sptSet[v] && this.maze[u][v] != 0 && dist[u] != Integer.MAX_VALUE && ((dist[u] + this.maze[u][v]) < dist[v])) {
					dist[v] = dist[u] + this.maze[u][v];
					path[v] = u;
				}
			}
		}
		path[src] = -1;
		return (dist[dest] != Integer.MAX_VALUE);
	} 
}
