package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard gameBoard;
    private TeamColor teamTurn;
    public ChessGame() {
        gameBoard = new ChessBoard();
        gameBoard.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (startPosition == null || gameBoard.getPiece(startPosition) == null) {
            return null;
        }
        ChessPiece Piece = gameBoard.getPiece(startPosition);
        ArrayList<ChessMove> moves = new ArrayList<>();
        for (ChessMove move : Piece.pieceMoves(gameBoard, startPosition)) {
            ChessBoard newBoard = cloneBoard(gameBoard);
            newBoard.addPiece(move.getEndPosition(), newBoard.getPiece(startPosition));
            newBoard.addPiece(startPosition, null);
            setBoard(newBoard);
            if (!isInCheck(teamTurn)) {
                moves.add(move);
            }
            setBoard(gameBoard);
        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        if (gameBoard.getPiece(start) == null || teamTurn != gameBoard.getPiece(start).getTeamColor()) {
            throw new InvalidMoveException();
        }
        if (!gameBoard.getPiece(start).pieceMoves(gameBoard, start).contains(move) || isInCheck(teamTurn) || start.equals(end) || !checkBounds(start) || !checkBounds(end)) {
            throw new InvalidMoveException();
        }
        ChessPiece movingPiece = gameBoard.getPiece(start);
        gameBoard.addPiece(start,null);
        if (move.getPromotionPiece() != null) {
            gameBoard.addPiece(end,new ChessPiece(movingPiece.getTeamColor(),move.getPromotionPiece()));
        } else {
            gameBoard.addPiece(end, movingPiece);
        }
        if (isInCheck(teamTurn)) {
            throw new InvalidMoveException();
        }
        if (teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                TeamColor enemyColor = teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
                ChessPiece testPiece = gameBoard.getPiece(new ChessPosition(i, j));
                if (testPiece == null || testPiece.getTeamColor() == teamColor) {
                    continue;
                }
                if (testPiece.getTeamColor() == enemyColor) {
                    for (ChessMove move : testPiece.pieceMoves(gameBoard, new ChessPosition(i, j))) {
                        if (gameBoard.getPiece(move.getEndPosition()) != null && gameBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING)  {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece testPiece = gameBoard.getPiece(new ChessPosition(i, j));
                if (testPiece == null) {
                    continue;
                } else if (testPiece.getTeamColor() == teamColor) {
                        for (ChessMove move : testPiece.pieceMoves(gameBoard, new ChessPosition(i, j))) {
                            ChessBoard simBoard = cloneBoard(gameBoard);
                            ChessGame simGame = new ChessGame();
                            simGame.setBoard(simBoard);
                            simGame.setTeamTurn(teamTurn);
                            try {
                                simGame.makeMove(move);
                            } catch (InvalidMoveException e) {
                                continue;
                            }
                            if (simGame.isInCheck(teamTurn)) {
                                continue;
                            } else {
                                return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    public ChessBoard cloneBoard(ChessBoard Board) {
        ChessBoard board = new ChessBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                board.addPiece(new ChessPosition(i, j), Board.getPiece(new ChessPosition(i, j)));
            }
        }
        return board;
    }

    public boolean checkBounds(ChessPosition position) {
        if (position.getColumn() > 8 || position.getColumn() < 1) {
            return false;
        } else return position.getRow() <= 8 || position.getRow() >= 1;
    }
}
