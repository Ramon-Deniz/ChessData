# PGN Language
##### GameList &#8594; game GameList | ε
##### game &#8594; tagList moveList
##### tagList &#8594; tag tagList | ε
##### turnList &#8594; turn turnList | ε
##### tag &#8594; [ key value ]
##### turn &#8594; [number . move move | number . move]
##### key &#8594; string
##### value &#8594; " string "
##### move &#8594; [piece root capture square promote | castleType] checkType
##### piece &#8594; K | Q | R | K | B | ε
##### root &#8594; file | rank
##### capture &#8594; x | ε
##### square &#8594; file rank
##### promote &#8594; = piece
##### checkType &#8594; check | checkmate
##### castleType &#8594; O - O | O - O - O
##### file &#8594; a | b | c | d | e | f | g | h
##### rank &#8594; 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8