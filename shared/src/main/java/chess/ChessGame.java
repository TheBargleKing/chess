package chess;

import java.util.ArrayList;
import java.util.Collection;

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
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (gameBoard.getPiece(startPosition) == null) {
            return null;
        } else {
            ChessPiece movingPiece = gameBoard.getPiece(startPosition);
            if (movingPiece.checkBounds(startPosition) && movingPiece.getTeamColor() == teamTurn) {
                ChessBoard simBoard = cloneBoard(gameBoard);
                for (ChessMove move : movingPiece.pieceMoves(simBoard, startPosition)) {
                    try {
                        makeMove(move);
                    } catch (InvalidMoveException e) {
                        continue;
                    }
                    if (isInCheck(teamTurn) || isInStalemate(teamTurn)) {
                        continue;
                    } else {
                        possibleMoves.add(move);
                    }
                }
            }
        }
        return possibleMoves;
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
        if (gameBoard.getPiece(end) != null && teamTurn == gameBoard.getPiece(end).getTeamColor()) {
            throw new InvalidMoveException();
        }
        if (!gameBoard.getPiece(start).pieceMoves(gameBoard, start).contains(move) || isInCheck(teamTurn) || start.equals(end) || start.equals(null) || end.equals(null) || !checkBounds(start) || !checkBounds(end)) {
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
                ChessPosition testPosition = new ChessPosition(i, j);
                ChessPiece testPiece = gameBoard.getPiece(testPosition);
                if (testPiece == null || testPiece.getTeamColor() == teamColor) {
                    continue;
                }
                if (testPiece.getTeamColor() == enemyColor) {
                    ChessBoard simBoard = cloneBoard(gameBoard);
                    ChessGame simGame = new ChessGame();
                    simGame.setTeamTurn(teamTurn);
                    simGame.setBoard(simBoard);
                    for (ChessMove move : testPiece.pieceMoves(simBoard, testPosition)) {
                        try {
                            simGame.makeMove(move);
                        } catch (InvalidMoveException e) {
                            continue;
                        }
                        if (gameBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
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
        return false;
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
        board = Board;
        return board;
    }

    public boolean checkBounds(ChessPosition position) {
        if (position.getColumn() > 8 || position.getColumn() < 1) {
            return false;
        } else return position.getRow() <= 8 || position.getRow() >= 1;
    }
}
