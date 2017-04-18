//Creating a class for storing maze.
var Maze = (function () {
    function Maze(r, c) {
        //Fill this with whatever values you find suitable for connections in maze.
        this.connectionPos = [[1, 3], [3, 5]];
        this.row = r;
        this.column = c;
        var noConnection = Math.floor(Math.random() * (this.maxConnection - this.minConnection)) + this.minConnection;
        for (var i = 0; i < noConnection; i++) {
            var pos = Math.floor(Math.random() * this.noPos);
            this.maze[this.connectionPos[pos][0]][this.connectionPos[pos][1]] = Math.floor(Math.random() * 10) + 1;
        }
    }
    Maze.prototype.extractWeight = function (i, j) {
        return this.maze[i][j];
    };
    Maze.prototype.dijkstra = function (source, destination) {
        //Apply dijkstra and check if destination is reachable.
        //Return true or false accordingly.
    };
    return Maze;
}());
var maze;
maze = new Maze(50, 50);
while (!maze.dijkstra(0, 0, 49, 49)) {
    maze = new Maze(50, 50);
}
