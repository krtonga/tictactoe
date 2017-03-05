# tictactoe-upon-the-wavy-sea
This simple tic tac toe api is deployed on heroku.
The following link will start a game where the server makes the first move: https://tictactoe-upon-the-wavy-sea.herokuapp.com/tictactoe?board=+++++++++

## Endpoints
To get the optimal move for a given board, send a GET to the following:
`https://tictactoe-upon-the-wavy-sea.herokuapp.com/move?board=+x+++++++`

To start a game, or send a move, send a GET to the following:
`https://tictactoe-upon-the-wavy-sea.herokuapp.com/play?board=+x+++++++`

*Note: Boards should be 9 characters. Client is always x, and server is always o. If you send an empty board, server will make the first move.*

To abort a game, and start again from scratch, send a GET to the following:
`https://tictactoe-upon-the-wavy-sea.herokuapp.com/play-again`


## Request/Response Examples
Here are some sample requests and responses:

**A move query:**
- REQUEST: `https://tictactoe-upon-the-wavy-sea.herokuapp.com/move?board=+x+++++++`
- RESPONSE: `ox       `

**A move error:**
- REQUEST: `https://tictactoe-upon-the-wavy-sea.herokuapp.com/move?board=+x+++++++`
- RESPONSE: `Error 400. Some error message`

**A play. A win:**
- REQUEST: `https://tictactoe-upon-the-wavy-sea.herokuapp.com/play?board=ox++o+x++`
- RESPONSE: `{"status":200,"board":"ox  o x o","winner":"o"}`

**A play. An error:**
- REQUEST: `https://tictactoe-upon-the-wavy-sea.herokuapp.com/play?board=oxx+++++++`
- RESPONSE: `{"status":400,"error":"Invalid move.","lastValid":"o++++++++"}`

**A new game request:**
- REQUEST: `https://tictactoe-upon-the-wavy-sea.herokuapp.com/play-again`
- RESPONSE: `{"status":200,"board":"+++++++++"}`
