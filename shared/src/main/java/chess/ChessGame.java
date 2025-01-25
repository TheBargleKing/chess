package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor TeamTurn;
    private ChessBoard Board;
    public ChessGame() {
        this.Board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return TeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        TeamTurn = team;
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
        if (startPosition == null || getBoard().getPiece(startPosition) == null) {
            return null;
        }
        return getBoard().getPiece(startPosition).pieceMoves(getBoard(), startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // Check for Invalid Moves
        if (validMoves(move.getStartPosition()) == null || !validMoves(move.getStartPosition()).contains(move.getEndPosition())) {
            throw new InvalidMoveException("The requested move is invalid.");
        }
        // Determine movingPiece
        ChessPiece movingPiece = Board.getPiece(move.getStartPosition());
        if (move.getPromotionPiece() == null) {
            // Move the piece (if not promoting)
            Board.addPiece(move.getEndPosition(), movingPiece);
            Board.addPiece(move.getStartPosition(), null);
        }
        else {
            // Creates the promoted piece at the end location
            Board.addPiece(move.getEndPosition(), new ChessPiece(TeamTurn, move.getPromotionPiece()));
            Board.addPiece(move.getStartPosition(), null);
        }
    }

    /**
     * Find the location of the king for a TeamColor
     *
     * @param teamColor Which king to look for
     * @return Position of the king
     */
    private ChessPosition findKingPosition(ChessGame.TeamColor teamColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = Board.getPiece(new ChessPosition(row, col));

                // Check if this piece is the King of the specified team
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return new ChessPosition(row, col); // Return the King's position
                }
            }
        }
        return null;
    }

        /**
         * Determines if the given team is in check
         *
         * @param teamColor which team to check for check
         * @return True if the specified team is in check
         */
    public boolean isInCheck(TeamColor teamColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (Board.getPiece(new ChessPosition(row, col)).getTeamColor() != teamColor) {
                    if (validMoves(new ChessPosition(row, col)).contains(findKingPosition(teamColor))) {
                        return true;
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
        if (isInCheck(teamColor)) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    ChessPosition currentPosition = new ChessPosition(row, col);
                    ChessPiece piece = Board.getPiece(currentPosition);

                    // Skip empty positions or positions with pieces of the opposite team
                    if (piece == null || piece.getTeamColor() != teamColor) {
                        continue;
                    }

                    // Step 3: Get valid moves for this piece
                    Collection<ChessMove> validMoves = validMoves(currentPosition);
                    if (validMoves == null || validMoves.isEmpty()) continue;

                    // Step 4: Simulate each move
                    for (ChessMove move : validMoves) {
                        ChessBoard simulatedBoard = cloneBoard(Board); // Clone current board
                        ChessGame simulatedGame = new ChessGame();
                        simulatedGame.setBoard(simulatedBoard);

                        // Apply the move to the simulated game
                        if (validMoves(move.getStartPosition()) == null || !validMoves(move.getStartPosition()).contains(move.getEndPosition())) {
                            throw new InvalidMoveException("The requested move is invalid.");
                        }
                        // Determine movingPiece
                        ChessPiece movingPiece = Board.getPiece(move.getStartPosition());
                        if (move.getPromotionPiece() == null) {
                            // Move the piece (if not promoting)
                            Board.addPiece(move.getEndPosition(), movingPiece);
                            Board.addPiece(move.getStartPosition(), null);
                        }
                        else {
                            // Creates the promoted piece at the end location
                            Board.addPiece(move.getEndPosition(), new ChessPiece(TeamTurn, move.getPromotionPiece()));
                            Board.addPiece(move.getStartPosition(), null);
                        }

                        // Check if the team is still in check
                        if (!simulatedGame.isInCheck(teamColor)) {
                            return false; // If a move removes check, not checkmate
                        }
                    }
                }
            }
        }
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        Board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard cloneBoard() {
        ChessBoard newBoard = new ChessBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                newBoard.addPiece(new ChessPosition(row, col), Board.getPiece(new ChessPosition(row, col)));
            }
        }
        return newBoard;
    }
    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return Board;
    }
}
