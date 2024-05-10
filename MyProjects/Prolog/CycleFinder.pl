/*
In The query, we can search for a cycle by calling cycle_search([X,Y]) where X and Y are the coordinates of the cell we want to start from.
*/

/*
The program is a cycle finder ,  A cycle which is a path that starts and ends at the same cell , all cells carry same color.
*/

%INTIAL CELLS REPRESENTATION

cell([0,0],yellow). cell([0,1],yellow). cell([0,2],yellow). cell([0,3],red).
cell([1,0],blue).   cell([1,1],yellow). cell([1,2],blue).   cell([1,3],yellow).
cell([2,0],blue).   cell([2,1],blue).   cell([2,2],blue).   cell([2,3],yellow).
cell([3,0],blue).   cell([3,1],blue).   cell([3,2],red).   cell([3,3],red).
cell([4,0],blue).   cell([4,1],blue).   cell([4,2],red).   cell([4,3],red).




extractCellsWithSameColor(Color,Nodes):-
    findall([X,Y],cell([X,Y],Color),Nodes).


    
    

findUP_adjacent_cells([X,Y], AdjacentCells,Color):- 
    extractCellsWithSameColor(Color,Nodes),
    X1 is X-1,
    member([X1,Y],Nodes),
    append([],[[X1,Y]],AdjacentCells).
findUP_adjacent_cells([X,Y], AdjacentCells,Color):-
    append([],[],AdjacentCells).



findDown_adjacent_cells([X,Y], AdjacentCells,Color):- 
    extractCellsWithSameColor(Color,Nodes),
    X1 is X+1,
    member([X1,Y],Nodes),
    append([],[[X1,Y]],AdjacentCells).
findDown_adjacent_cells([X,Y], AdjacentCells,Color):- 
    append([],[],AdjacentCells).


findLeft_adjacent_cells([X,Y], AdjacentCells,Color):-
    extractCellsWithSameColor(Color,Nodes),
    Y1 is Y-1,
    member([X,Y1],Nodes),
    append([],[[X,Y1]],AdjacentCells).
findLeft_adjacent_cells([X,Y], AdjacentCells,Color):-
    append([],[],AdjacentCells).


findRight_adjacent_cells([X,Y], AdjacentCells,Color):-
    extractCellsWithSameColor(Color,Nodes),
    Y1 is Y+1,
    member([X,Y1],Nodes),
    append([],[[X,Y1]],AdjacentCells).
findRight_adjacent_cells([X,Y], AdjacentCells,Color):-
    append([],[],AdjacentCells).


    
findAdjacentCells([X,Y], AdjacentCells,Color):-
    
        findUP_adjacent_cells([X,Y], AdjacentUp,Color) 
    ,
    
        findRight_adjacent_cells([X,Y], AdjacentRight,Color) 
    ,
    
        findLeft_adjacent_cells([X,Y], AdjacentLeft,Color) 
    ,
    
        findDown_adjacent_cells([X,Y], AdjacentDown,Color) 
    ,
    append(AdjacentUp,AdjacentRight,Temp),
    append(Temp,AdjacentLeft,Temp1),
    append(Temp1,AdjacentDown,AdjacentCells),
    length(AdjacentCells, L3),
    L3>1,!.



    




extractNodesWithMin2AdjacentCells(Color,New_Cells):-
    extractCellsWithSameColor(Color,Cells),
    extractNodesWithMin2AdjacentCells_helper(Cells,Color,New_Cells),!.

extractNodesWithMin2AdjacentCells_helper([],_,[]).
extractNodesWithMin2AdjacentCells_helper([Cell|Cells],Color,[Cell|New_Cells]):-
    findAdjacentCells(Cell,AdjacentCells,Color),
    extractNodesWithMin2AdjacentCells_helper(Cells,Color,New_Cells).
extractNodesWithMin2AdjacentCells_helper([Cell|Cells],Color,New_Cells):-
    extractNodesWithMin2AdjacentCells_helper(Cells,Color,New_Cells).





delete_element(Element, List1, List2) :-
    delete(List1, Element, List2).

search([],Closed,Color):- %Goal State , We search for a visited node that is not the parent node
    [CurrentNode|Tail]=Closed,
    [LastVisited|_]=Tail, 
    findAdjacentCells_new(CurrentNode,AdjacentCells,Color),
    delete_element(LastVisited,AdjacentCells,New),
    %make sure New is not empty
    not(New = []),
    append(New,Closed,NewClosed),
    reverse(NewClosed,Reversed),
    write('Cycle Detected in Path : '),write(Reversed),nl,
    cycle_extraction_helper(Reversed,Reversed2),
    remove_Last_untill_match_First(Reversed2,Cycle),%To extract cycle from the path
    
    write('Cycle : '),write(Cycle),nl,!.


search(Open, Closed,Color):-
   [CurrentNode|_]=Open, %choose the first node in the open list
   not(member(CurrentNode, Closed)), %check if the node is not visited
   state_update(CurrentNode,Color,NewOpen,Closed), %Get the new Open List from the current node
    append([CurrentNode], Closed, NewClosed),%Add the current node to the visited list
    search(NewOpen, NewClosed,Color).%traverse to the next node



   

remove_common_elements([], _, []).
remove_common_elements([H|T], L2, L3) :- member(H, L2), !, remove_common_elements(T, L2, L3).
remove_common_elements([H|T], L2, [H|L3]) :- remove_common_elements(T, L2, L3).


reverse(Xs, Ys) :- reverse(Xs, [], Ys).
reverse([], A, A).
reverse([H|T], R, A) :- reverse(T, [H|R], A).


state_update(Cell,Color,NewOpen,Closed):-
    findAdjacentCells_new(Cell,AdjacentCells,Color),%find the adjacent cells that has more than 1 adjacent cells and has the same color
    remove_common_elements(AdjacentCells,Closed,Temp),%remove the visited nodes from the adjacent cells 
    append([],Temp,NewOpen),!.


findUP2_adjacent_cells([X,Y], AdjacentCells,Color):- 
    extractNodesWithMin2AdjacentCells(Color,Nodes),
    X1 is X-1,
    member([X1,Y],Nodes),
    append([],[[X1,Y]],AdjacentCells).
findUP2_adjacent_cells([X,Y], AdjacentCells,Color):-
    append([],[],AdjacentCells).



findDown2_adjacent_cells([X,Y], AdjacentCells,Color):- 
    extractNodesWithMin2AdjacentCells(Color,Nodes),
    X1 is X+1,
    member([X1,Y],Nodes),
    append([],[[X1,Y]],AdjacentCells).
findDown2_adjacent_cells([X,Y], AdjacentCells,Color):- 
    append([],[],AdjacentCells).


findLeft2_adjacent_cells([X,Y], AdjacentCells,Color):-
    extractNodesWithMin2AdjacentCells(Color,Nodes),
    Y1 is Y-1,
    member([X,Y1],Nodes),
    append([],[[X,Y1]],AdjacentCells).
findLeft2_adjacent_cells([X,Y], AdjacentCells,Color):-
    append([],[],AdjacentCells).


findRight2_adjacent_cells([X,Y], AdjacentCells,Color):-
    extractNodesWithMin2AdjacentCells(Color,Nodes),
    Y1 is Y+1,
    member([X,Y1],Nodes),
    append([],[[X,Y1]],AdjacentCells).
findRight2_adjacent_cells([X,Y], AdjacentCells,Color):-
    append([],[],AdjacentCells).


    
findAdjacentCells_new([X,Y], AdjacentCells,Color):-
    
        findUP2_adjacent_cells([X,Y], AdjacentUp,Color) 
    ,
    
        findRight2_adjacent_cells([X,Y], AdjacentRight,Color) 
    ,
    
        findLeft2_adjacent_cells([X,Y], AdjacentLeft,Color) 
    ,
    
        findDown2_adjacent_cells([X,Y], AdjacentDown,Color) 
    ,
    append(AdjacentUp,AdjacentRight,Temp),
    append(Temp,AdjacentLeft,Temp1),
    
    
    
    append(Temp1,AdjacentDown,AdjacentCells),!.

remove_Last_untill_match_First([],[]).
remove_Last_untill_match_First(List,List):-
    reverse(List,Reversed),
    [First|_]=List,
    [Last|_]=Reversed,
    First = Last,
    length(List, L),
    L>1.
    
remove_Last_untill_match_First(List,Out):-
    
    reverse(List,Reversed),
    [Last|TAIL]=Reversed,
    reverse(TAIL,NewReversed),
    remove_Last_untill_match_First(NewReversed,Out).
cycle_extraction_helper([],[]).
cycle_extraction_helper(List,List):-
    [First|Tail]=List,
    member(First,Tail).
cycle_extraction_helper(List,Out):-
    [First|Tail]=List,
    not(member(First,Tail)),
    cycle_extraction_helper(Tail,Out).



cycle_search([X,Y]):- %TO search for a cycle by starting from a specific cell
    Closed = [],
    cell([X,Y],Color),
    write('Color : '),write(Color),nl,
    append([[X,Y]],[],Open),
    search(Open,Closed,Color),!.




