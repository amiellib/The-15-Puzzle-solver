# The-15-Puzzle-solver

## How to use:

Have a text file name: "input.txt".

In the file you will have the information of the puzzle.
the information need to be in the first line till the end (no empty lines or space!). 

## What algorithm to use

### You can pick from:

DFID

A*

DFBnB

BFS

IDA*

## Time

To show the time took to solve put in the line 2 "show line". 
Not showing the time put "no time".

## Open list

To print the open list in line 3 put "with open".

Not showing the open list put "no open".

## Size of matrix

In line 4 you will pux the size of the matrix NxM (4x5 3x3 2x7 ...).

## Black tiles

Black tiles are tiles you can not move.

In line 5 you will put "Black: " if there is no black tiles.

If there is a black tile you will put the number of the black tile like this  "Black: 6". 

If there is more then one black tile you will write it like this: "Black: 2,4,3".

## Red tiles

Black tiles are tiles you can move but the cost of moving them is 30.

In line 6 you will put "Red: " if there is no red tiles.

If there is a red tile you will put the number of  the red tile like this  "Red: 2".

If there is more then one red tile you will write it like this: "Red: 1,8,11".

## Numers of matrix

From line 7 will will write the starting position of the matrix.

The empty spot will be written  "_".

For example a 3x4 matrix:

1,2,3,4

5,8,7,6

11,10,9,_

Example of a 4x4 matrix:

_,2,3,4

5,8,7,6

11,10,9,1

12,13,14,15

You can look in the input file and see how it looks

## More information 

All tiles that are not black or red the cost of moving them is 1.

A tile can move up or down or left or right.
