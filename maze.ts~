//Creating a class for storing maze.
class Maze {
	//Update the maze to a pseudo dynamic one that will solve lots of
	//problem that we'll face with visualization.
	protected maze:number[][];
	protected nVertices:number;
	//Fill this with whatever values you find suitable for connections in maze.
	protected connectionPos:number[][] = [[1,3],[3,5]];
	protected noPos:number;
	//Also update this with suitable range of connections.
	private minConnection:number;
	private maxConnection:number;
	constructor (Vertices:number) {
		this.nVertices = Vertices;
		let noConnection:number = Math.floor(Math.random()*(this.maxConnection - this.minConnection)) + this.minConnection;		
		for (let i:number = 0; i < noConnection; i++) {
			let pos:number = Math.floor(Math.random()*this.noPos);
			this.maze[this.connectionPos[pos][0]][this.connectionPos[pos][1]] = Math.floor(Math.random()*10)+1;
		}
	}
	public extractWeight (i:number, j:number) {
		return this.maze[i][j]; 
	}
	private minDistance (dist:number[], sptSet:Boolean[]) {
		let min:number = Infinity;
		let mIndex:number;
		for (var i:number = 0; i < this.nVertices; i++) {
			if (sptSet[i] == false && dist[i] <= min) {
				min = dist[i];
				mIndex = i;
			}
		}
		return mIndex;
	}
	public dijkstra (src:number, path:number[]) {
		//Apply dijkstra and check if destination is reachable.
		//Return true or false accordingly.
		let dist:number[];
		let sptSet:Boolean[];
		for (var i:number = 0; i < this.nVertices; i++) {
			dist[i] = Infinity;
			sptSet[i] = false;
		}
		dist[src] = 0;
		for (var i:number = 0; i < this.nVertices - 1; i++) {
			//Takes the minimum distance vertex from the set of vertices that is yet to be processed
			let u:number = this.minDistance (dist, sptSet);
			//Marks the just explored vertex as processed
			sptSet[u] = true;
			for (var v:number = 0; v < this.nVertices; v++) {
				//Update dist[v] only if it is not in sptSet,
				//there is an edge from u to v,
				//and total weight of path from source to v through u is
				//smaller than the current value of dist[v]
				if (!sptSet[v] && this.maze[u][v] && dist[u] != Infinity && dist[u] + this.maze[u][v] < dist[v])
					dist[v] = dist[u] + this.maze[u][v];
				path[v] = u;
			}
			path[src] = -1;
		}
	}
}

let maze;
maze = new Maze(50);
let path:number[];
while (!maze.dijkstra(0, path)) {
	maze = new Maze(50);		
}
