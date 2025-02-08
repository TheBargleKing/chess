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
        this.gameBoard = new ChessBoard();
        this.gameBoard.resetBoard();
        this.teamTurn = TeamColor.WHITE;
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
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE, BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        if (startPosition == null || gameBoard.getPiece(startPosition) == null) {
            return null;
        }
        ChessPiece piece = gameBoard.getPiece(startPosition);
        for (ChessMove move : piece.pieceMoves(gameBoard, startPosition)) {
            if (!isInCheckAfterMove(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * Checks if the move will cause check
     *
     * @param move the move to check for check after
     * @return true if will end up in check, else return false
     */
    private boolean isInCheckAfterMove(ChessMove move) {
        ChessBoard simulatedBoard = cloneBoard(gameBoard);
        ChessPiece piece = simulatedBoard.getPiece(move.getStartPosition());
        simulatedBoard.addPiece(move.getEndPosition(), piece);
        simulatedBoard.addPiece(move.getStartPosition(), null);
        return isInCheck(piece.getTeamColor(), simulatedBoard);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (move == null) {
            throw new InvalidMoveException("Invalid Move: Move cannot be null.");
        }
        ChessPiece movingPiece = gameBoard.getPiece(move.getStartPosition());
        if (movingPiece == null || teamTurn != movingPiece.getTeamColor()) {
            throw new InvalidMoveException("Invalid move: No piece at position or not your turn.");
        }
        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Invalid move: Move not valid.");
        }

        // Perform the move
        gameBoard.addPiece(move.getStartPosition(), null);
        if (move.getPromotionPiece() != null) {
            gameBoard.addPiece(move.getEndPosition(), new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece()));
        } else {
            gameBoard.addPiece(move.getEndPosition(), movingPiece);
        }

        // Ensure the move doesn't leave the king in check
        if (isInCheck(teamTurn)) {
            throw new InvalidMoveException("Invalid move: Cannot move into check.");
        }

        // Switch turns
        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, gameBoard);
    }

    /**
     * Including an additional parameter
     *
     * @param board to specify which board to check for check
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    private boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = findKingPosition(teamColor, board);
        if (kingPosition == null) return false;

        TeamColor enemyColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == enemyColor) {
                    for (ChessMove move : piece.pieceMoves(board, position)) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Find the king position for easier time finding check
     *
     * @param board to specify which board to look for the king
     * @param teamColor the team whose king is to be found
     * @return the position of the king
     */
    private ChessPosition findKingPosition(TeamColor teamColor, ChessBoard board) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return (isInCheck(teamColor) && anyValidMoves(teamColor));
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return (!isInCheck(teamColor) && anyValidMoves(teamColor));
    }

    /**
     * Additional help to make more concise Stalemate and Checkmate checks
     *
     * @param teamColor to check for validMoves
     */
    public boolean anyValidMoves(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = gameBoard.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor && !validMoves(position).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    /**
     * Clones the chessboard that is input, to prevent wrongful manipulation
     *
     * @param board the board to be cloned
     * @return the cloned chessboard
     */
    private ChessBoard cloneBoard(ChessBoard board) {
        ChessBoard newBoard = new ChessBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null) {
                    newBoard.addPiece(position, new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
                }
            }
        }
        return newBoard;
    }
}