var Maze = (function () {
    function Maze(r, c) {
        for (var i = 0; i < r; i++) {
            for (var j = 0; j < c; j++) {
                this.maze[i][j] = Math.floor(Math.random() * 10) + 1;
            }
        }
    }
    return Maze;
}());
