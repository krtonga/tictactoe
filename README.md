# tictactoe-upon-the-wavy-sea
This simple tic tac toe api is deployed on heroku.

## Endpoints
To start a game, or send a move, send a GET to the following:
`https://tictactoe-upon-the-wavy-sea.herokuapp.com/tictactoe?board=+x+++++++`

Boards should be 9 characters. Client is always x, and server is always o. If you send an empty board, server will make the first move.

To abort a game, and start again from scratch, send a GET to the following:
`https://tictactoe-upon-the-wavy-sea.herokuapp.com/tictactoe/new`


## Request/Response Examples
Here are some sample requests and responses:

*A move:*
REQUEST: `https://tictactoe-upon-the-wavy-sea.herokuapp.com/tictactoe?board=+x+++++++`
RESPONSE: `{"status":200,"board":"ox       "}`

*A win:*
REQUEST: `https://tictactoe-upon-the-wavy-sea.herokuapp.com/tictactoe?board=ox++o+x++`
RESPONSE: `{"status":200,"board":"ox  o x o","winner":"o"}`

*An error:*
REQUEST: `https://tictactoe-upon-the-wavy-sea.herokuapp.com/tictactoe?board=oxx+++++++`
RESPONSE: `{"status":400,"error":"Invalid move.","lastValid":"o++++++++"}`

*A new game:*
REQUEST: `https://tictactoe-upon-the-wavy-sea.herokuapp.com/tictactoe/new`
RESPONSE: `{"status":200,"board":"+++++++++"}`
