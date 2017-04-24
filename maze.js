//Creating a class for storing maze.
var Maze = (function () {
    function Maze(Vertices) {
        //Fill this with whatever values you find suitable for connections in maze.
        this.connectionPos = [[1, 3], [3, 5]];
        this.nVertices = Vertices;
        var noConnection = Math.floor(Math.random() * (this.maxConnection - this.minConnection)) + this.minConnection;
        for (var i = 0; i < noConnection; i++) {
            var pos = Math.floor(Math.random() * this.noPos);
            this.maze[this.connectionPos[pos][0]][this.connectionPos[pos][1]] = Math.floor(Math.random() * 10) + 1;
        }
    }
    Maze.prototype.extractWeight = function (i, j) {
        return this.maze[i][j];
    };
    Maze.prototype.minDistance = function (dist, sptSet) {
        var min = Infinity;
        var mIndex;
        for (var i = 0; i < this.nVertices; i++) {
            if (sptSet[i] == false && dist[i] <= min) {
                min = dist[i];
                mIndex = i;
            }
        }
        return mIndex;
    };
    Maze.prototype.dijkstra = function (src, path) {
        //Apply dijkstra and check if destination is reachable.
        //Return true or false accordingly.
        var dist;
        var sptSet;
        for (var i = 0; i < this.nVertices; i++) {
            dist[i] = Infinity;
            sptSet[i] = false;
        }
        dist[src] = 0;
        for (var i = 0; i < this.nVertices - 1; i++) {
            //Takes the minimum distance vertex from the set of vertices that is yet to be processed
            var u = this.minDistance(dist, sptSet);
            //Marks the just explored vertex as processed
            sptSet[u] = true;
            for (var v = 0; v < this.nVertices; v++) {
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
    };
    return Maze;
}());
var maze;
maze = new Maze(50);
var path;
while (!maze.dijkstra(0, path)) {
    maze = new Maze(50);
}
