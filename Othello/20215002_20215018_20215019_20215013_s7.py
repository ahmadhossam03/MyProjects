# Ahmed Hossam Aldin 20215002
# Asem Ahmed Ibrahim 20215018
# Rahma Hussein 20215013
# Abdelrahman Khaled AbdelAzeem 20215019

import math
import copy
import tkinter as tk


# RUN THE CODE FROM THE TERMINAL TO AVOID BUGS OR CRASHES
# RUN THE CODE FROM THE TERMINAL TO AVOID BUGS OR CRASHES
# RUN THE CODE FROM THE TERMINAL TO AVOID BUGS OR CRASHES


# Empty : 0, Black : 1, White : 2
class Board:  # BOARD CLASS
    def __init__(self):
        self.board = [[0 for _ in range(8)] for _ in range(8)]
        self.board[3][3] = self.board[4][4] = 1
        self.board[3][4] = self.board[4][3] = 2

    def print_board(self):  # PRINT BOARD FUNCTION
        for row in self.board:
            print(' '.join(map(str, row)))

    def is_valid_move(self, row, col, color):  # CHECK IF THE MOVE IS VALID
        if self.board[row][col] != 0:
            return False
        # Only check horizontal and vertical directions
        for dr, dc in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
            r, c = row + dr, col + dc
            while 0 <= r < 8 and 0 <= c < 8 and self.board[r][c] == 3 - color:
                r += dr
                c += dc
            if 0 <= r < 8 and 0 <= c < 8 and self.board[r][c] == color and (r, c) != (row + dr, col + dc):
                return True
        return False

    def print_available_moves(self, color):  # PRINT AVAILABLE MOVES (POSSIBLE OUTFLANKING MOVES)
        empty_locations = []
        available_moves = []
        for row in range(8):
            for col in range(8):
                if self.board[row][col] == 0:
                    empty_locations.append((row, col))
        for row, col in empty_locations:
            if self.is_valid_move(row, col, color):
                available_moves.append((row, col))
        return available_moves

    def make_move(self, row, col, color):  # MAKE THE MOVE ON THE BOARD
        moves = self.print_available_moves(color)
        if (row, col) in moves:
            self.board[row][col] = color
            # Only flip in horizontal and vertical directions
            for dr, dc in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
                r, c = row + dr, col + dc
                to_flip = []
                while 0 <= r < 8 and 0 <= c < 8 and self.board[r][c] == 3 - color:
                    to_flip.append((r, c))
                    r += dr
                    c += dc
                if 0 <= r < 8 and 0 <= c < 8 and self.board[r][c] == color:
                    for flip_row, flip_col in to_flip:
                        self.board[flip_row][flip_col] = color
            return 1
        else:
            print("Invalid Move")
            return 0

    def is_game_over(self):  # CHECK IF THE GAME IS OVER
        return len(self.print_available_moves(1)) == 0 and len(self.print_available_moves(2)) == 0

    def get_num_of_color(self, color):  # GET THE NUMBER OF PIECES OF A CERTAIN COLOR USED TO DECIDE THE WINNER
        count = 0
        for row in self.board:
            for cell in row:
                if cell == color:
                    count += 1
        return count


class Player:  # PLAYER CLASS
    def __init__(self, color):
        self.color = color

    def make_move(self,
                  board):  # MAKE THE MOVE METHOD USES THE MAKE MOVE FUNCTION IN THE BOARD CLASS TO UPDATE THE BOARD
        available_moves = board.print_available_moves(self.color)
        print("Available Moves:", available_moves)
        if len(available_moves) == 0:
            print("No available moves for", "Black" if self.color == 1 else "White")
            return

        row = int(input("Enter the row: "))
        col = int(input("Enter the col: "))
        x = board.make_move(row, col, self.color)
        while x == 0:
            row = int(input("Enter the row: "))
            col = int(input("Enter the col: "))
            x = board.make_move(row, col, self.color)

        board.print_board()


class Computer:  # COMPUTER CLASS
    def __init__(self, color, difficulty):
        self.color = color
        self.difficulty = difficulty
        if self.difficulty == 1:
            self.depth = 1
        elif self.difficulty == 3:
            self.depth = 3
        else:
            self.depth = 5

    def get_color(self):
        return self.color

    def evaluate(self,
                 board):  # EVALUATE FUNCTION TO EVALUATE THE BOARD , HIGHER DIFFRENCE BETWEEN THE TWO COLORS MEANS BETTER POSITION
        return board.get_num_of_color(self.color) - board.get_num_of_color(3 - self.color)

    def alpha_beta_pruning(self, new_board, depth, alpha, beta, color):
        if depth == 0:
            return self.evaluate(new_board)
        if color == self.color:
            max_eval = -math.inf
            for move in new_board.print_available_moves(color):
                temp_board = copy.deepcopy(new_board)
                temp_board.make_move(move[0], move[1], color)
                eval = self.alpha_beta_pruning(temp_board, depth - 1, alpha, beta, 3 - color)
                max_eval = max(max_eval, eval)  # MAXIMIZING FOR THE COMPUTER
                alpha = max(alpha, eval)
                if beta <= alpha:  # PRUNING
                    break
            return max_eval
        else:
            min_eval = math.inf
            for move in new_board.print_available_moves(color):
                temp_board = copy.deepcopy(new_board)
                temp_board.make_move(move[0], move[1], color)
                eval = self.alpha_beta_pruning(temp_board, depth - 1, alpha, beta, 3 - color)
                min_eval = min(min_eval, eval)  # MINIMIZING FOR THE OPPONENT
                beta = min(beta, eval)
                if beta <= alpha:
                    break
            return min_eval

    def alphaBeta(self, board, color):
        alpha = -math.inf
        beta = math.inf
        new_board = copy.deepcopy(board)  # COPY THE BOARD TO AVOID CHANGING THE ORIGINAL BOARD
        return self.alpha_beta_pruning(new_board, self.depth, alpha, beta, color)

    def make_move(self, board):  # MAKE THE MOVE METHOD FOR THE COMPUTER
        available_moves = board.print_available_moves(self.color)
        if len(available_moves) == 0:
            print("No available moves for", "Black" if self.color == 1 else "White")
            return
        evals = []
        for move in available_moves:
            new_board = copy.deepcopy(board)
            new_board.make_move(move[0], move[1], self.color)
            evals.append(self.alphaBeta(new_board, 3 - self.color))
        index = evals.index(max(evals))
        board.make_move(available_moves[index][0], available_moves[index][1], self.color)
        print("Computer's Move:", available_moves[index])


class OthelloGUI:
    def __init__(self, difficulty):
        self.root = tk.Tk()

        self.board = Board()
        self.player1 = Player(1)
        diff = ""
        if difficulty == 1:
            diff = "Easy"
        elif difficulty == 3:
            diff = "Medium"
        else:
            diff = "Hard"

        self.player2 = Computer(2, difficulty)
        self.root.title("Othello Game" + " - Difficulty: " + diff)

        self.canvas = tk.Canvas(self.root, width=400, height=450)
        self.canvas.pack()

        self.status_label = tk.Label(self.root, text="", font=("Helvetica", 16))
        self.status_label.pack()

        self.draw_board()
        self.draw_pieces()
        self.update_status()
        self.highlight_valid_moves()

        self.canvas.bind("<Button-1>", self.on_click)
        self.root.mainloop()

    def draw_board(self):
        for row in range(8):
            for col in range(8):
                x1 = col * 50
                y1 = row * 50
                x2 = x1 + 50
                y2 = y1 + 50
                self.canvas.create_rectangle(x1, y1, x2, y2, fill="green")

    def draw_pieces(self):
        self.canvas.delete("piece")
        for row in range(8):
            for col in range(8):
                if self.board.board[row][col] == 1:
                    x = col * 50 + 25
                    y = row * 50 + 25
                    self.canvas.create_oval(x - 20, y - 20, x + 20, y + 20, fill="black", tags="piece")
                elif self.board.board[row][col] == 2:
                    x = col * 50 + 25
                    y = row * 50 + 25
                    self.canvas.create_oval(x - 20, y - 20, x + 20, y + 20, fill="white", tags="piece")

    def highlight_valid_moves(self):
        valid_moves = self.board.print_available_moves(self.player1.color)
        for move in valid_moves:
            x = move[1] * 50 + 25
            y = move[0] * 50 + 25
            self.canvas.create_oval(x - 6, y - 6, x + 6, y + 6, fill="yellow", tags="highlight")

    def on_click(self, event):
        col = event.x // 50
        row = event.y // 50

        if self.board.is_valid_move(row, col, self.player1.color):
            self.board.make_move(row, col, self.player1.color)
            self.draw_pieces()
            self.update_status()
            self.highlight_valid_moves()

            self.root.after(750, self.check_computer_move)  # Delay for computer move
            self.canvas.delete("highlight")

            if self.board.is_game_over():
                self.display_winner()

    def check_computer_move(self):
        if len(self.board.print_available_moves(self.player2.color)) > 0:
            self.computer_move()
        elif len(self.board.print_available_moves(self.player1.color)) > 0:
            # If computer has no moves, but player has, switch back to player
            self.highlight_valid_moves()
        else:
            self.display_winner()  # No valid moves for both players, game is over

    def computer_move(self):
        self.player2.make_move(self.board)
        self.draw_pieces()
        self.update_status()
        self.highlight_valid_moves()
        if len(self.board.print_available_moves(self.player1.color)) == 0:
            self.root.after(500, self.check_computer_move)
        elif self.board.is_game_over():
            self.display_winner()

    def update_status(self):
        black_count = self.board.get_num_of_color(1)
        white_count = self.board.get_num_of_color(2)
        self.status_label.config(text=f"Black: {black_count}  White: {white_count}")

    def display_winner(self):
        black_count = self.board.get_num_of_color(1)
        white_count = self.board.get_num_of_color(2)
        if black_count > white_count:
            winner_text = "Black Wins"
        elif white_count > black_count:
            winner_text = "White Wins"
        else:
            winner_text = "Draw"
        self.status_label.config(text=f"Game Over! {winner_text}")


mode = 0
while mode not in [1, 2]:
    print("Enter 1 TO Play in gui mode")
    print("Enter 2 TO Play in console mode")
    mode = int(input())
if mode == 1:
    
    print("Welcome to Othello Game!")
    difficulty = int(input("Choose Difficulty: 1 (Easy), 3 (Medium), 5 (Hard)\n"))
    print("--------------------")
    print("Check the taskbar for the game window")
    print("--------------------")
    gui = OthelloGUI(difficulty)
else:
    print("Welcome to Othello Game!")
    print("Black: 1, White: 2")
    print("--------------------")
    board = Board()
    board.print_board()
    print("--------------------")
    player1 = Player(1)
    print("Choose Difficulty: 1 (Easy), 3 (Medium), 5 (Hard)")
    difficulty = int(input())
    while difficulty not in [1, 3, 5]:
        print("Invalid Difficulty! Choose Difficulty: 1 (Easy), 3 (Medium), 5 (Hard)")
        difficulty = int(input())
    player2 = Computer(2, difficulty)

    while not board.is_game_over():
        print("--------------------")
        player1.make_move(board)
        print("--------------------")
        if board.is_game_over():
            break
        player2.make_move(board)
        board.print_board()
        print("--------------------")
        if board.is_game_over():
            break
    print("--------------------")
    print("Game Over!")
    black_count = board.get_num_of_color(1)
    white_count = board.get_num_of_color(2)
    print("Black Count:", black_count)
    print("White Count:", white_count)
    if black_count > white_count:
        print("Black Wins")
    elif white_count > black_count:
        print("White Wins")
    else:
        print("Draw")
