//Creating a class for storing maze.
class Maze {
	protected maze:number[][]; 
	constructor (r:number, c:number) {
		for (var i:number = 0; i < r; i++) {
			for (var j:number = 0; j < c; j++) {
				this.maze[i][j] = Math.floor(Math.random() * 10) + 1;
			}
		}	
	}
}