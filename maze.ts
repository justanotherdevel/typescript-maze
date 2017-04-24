//Creating a class for storing maze.
class Maze {
	//Update the maze to a pseudo dynamic one that will solve lots of
	//problem that we'll face with visualization.
	protected maze:number[][];
	protected row:number;
	protected column:number;
	//Fill this with whatever values you find suitable for connections in maze.
	protected connectionPos:number[][] = [[1,3],[3,5]];
	protected noPos:number;
	//Also update this with suitable range of connections.
	private minConnection:number;
	private maxConnection:number;
	constructor (r:number, c:number) {
		this.row = r;
		this.column = c;
		let noConnection:number = Math.floor(Math.random()*(this.maxConnection - this.minConnection)) + this.minConnection;		
		for (let i:number = 0; i < noConnection; i++) {
			let pos:number = Math.floor(Math.random()*this.noPos);
			this.maze[this.connectionPos[pos][0]][this.connectionPos[pos][1]] = Math.floor(Math.random()*10)+1;
		}
	}
	public extractWeight (i:number, j:number) {
		return this.maze[i][j]; 
	}
	public dijkstra (sRow:number, sColumn:number, dRow:number, dColumn:number) {
		//Apply dijkstra and check if destination is reachable.
		//Return true or false accordingly.
	}
}

let maze;
maze = new Maze(50, 50);
while (!maze.dijkstra(0, 0, 49, 49)) {
	maze = new Maze(50, 50);		
}
