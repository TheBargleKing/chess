package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessGame {
    private ChessBoard gameBoard;
    private TeamColor teamTurn;

    public ChessGame() {
        this.gameBoard = new ChessBoard();
        this.gameBoard.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (startPosition == null || gameBoard.getPiece(startPosition) == null) {
            return null;
        }
        ChessPiece mainPiece = gameBoard.getPiece(startPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        Collection<ChessMove> possibleMoves = mainPiece.pieceMoves(gameBoard, startPosition);
        for (ChessMove move : possibleMoves) {
            if (simulateMove(gameBoard, move)) {
                moves.add(move);
            }
        }

        return moves;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece movingPiece = gameBoard.getPiece(start);
        if (movingPiece == null || teamTurn != movingPiece.getTeamColor() || !validMoves(start).contains(move) || start.equals(end) || !checkBounds(start) || !checkBounds(end)) {
            throw new InvalidMoveException("Invalid move");
        }
        if (move.getPromotionPiece() != null) {
            gameBoard.addPiece(end, new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece()));
        } else {
            gameBoard.addPiece(end, movingPiece);
        }
        gameBoard.addPiece(start, null);
        teamTurn = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

//package chess;
//
//import java.util.Collection;
//
    ///**
// * For a class that can manage a chess game, making moves on a board
// * <p>
// * Note: You can add to this class, but you may not alter
// * signature of the existing methods.
// */
//public class ChessGame {
//
//    public ChessGame() {
//
//    }
//
//    /**
//     * @return Which team's turn it is
//     */
//    public TeamColor getTeamTurn() {
//        throw new RuntimeException("Not implemented");
//    }
//
//    /**
//     * Set's which teams turn it is
//     *
//     * @param team the team whose turn it is
//     */
//    public void setTeamTurn(TeamColor team) {
//        throw new RuntimeException("Not implemented");
//    }
//
//    /**
//     * Enum identifying the 2 possible teams in a chess game
//     */
//    public enum TeamColor {
//        WHITE,
//        BLACK
//    }
//
//    /**
//     * Gets a valid moves for a piece at the given location
//     *
//     * @param startPosition the piece to get valid moves for
//     * @return Set of valid moves for requested piece, or null if no piece at
//     * startPosition
//     */
//    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
//        throw new RuntimeException("Not implemented");
//    }
//
//    /**
//     * Makes a move in a chess game
//     *
//     * @param move chess move to perform
//     * @throws InvalidMoveException if move is invalid
//     */
//    public void makeMove(ChessMove move) throws InvalidMoveException {
//        throw new RuntimeException("Not implemented");
//    }
//
//    /**
//     * Determines if the given team is in check
//     *
//     * @param teamColor which team to check for check
//     * @return True if the specified team is in check
//     */
//    public boolean isInCheck(TeamColor teamColor) {
//        throw new RuntimeException("Not implemented");
//    }
//
//    /**
//     * Determines if the given team is in checkmate
//     *
//     * @param teamColor which team to check for checkmate
//     * @return True if the specified team is in checkmate
//     */
//    public boolean isInCheckmate(TeamColor teamColor) {
//        throw new RuntimeException("Not implemented");
//    }
//
//    /**
//     * Determines if the given team is in stalemate, which here is defined as having
//     * no valid moves
//     *
//     * @param teamColor which team to check for stalemate
//     * @return True if the specified team is in stalemate, otherwise false
//     */
//    public boolean isInStalemate(TeamColor teamColor) {
//        throw new RuntimeException("Not implemented");
//    }
//
//    /**
//     * Sets this game's chessboard with a given board
//     *
//     * @param board the new board to use
//     */
//    public void setBoard(ChessBoard board) {
//        throw new RuntimeException("Not implemented");
//    }
//
//    /**
//     * Gets the current chessboard
//     *
//     * @return the chessboard
//     */
//    public ChessBoard getBoard() {
//        throw new RuntimeException("Not implemented");
//    }
//}

    public boolean isInCheck(TeamColor teamColor) {
        for (ChessPosition position : piecePositions(gameBoard)) {
            ChessPiece piece = gameBoard.getPiece(position);
            if (piece != null && piece.getTeamColor() != teamColor) {
                for (ChessMove move : piece.pieceMoves(gameBoard, position)) {
                    if (gameBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        if (anyValidMoves(gameBoard, teamColor)) {
            return false;
        }
        return true;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        if (anyValidMoves(gameBoard, teamColor)) {
            return false;
        }
        return true;
    }

    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    public ChessBoard getBoard() {
        return gameBoard;
    }

    public Collection<ChessPosition> piecePositions(ChessBoard board) {
        Collection<ChessPosition> piecePositions = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null) {
                    piecePositions.add(position);
                }
            }
        }
        return piecePositions;
    }

    public ChessBoard cloneBoard(ChessBoard board) {
        ChessBoard newBoard = new ChessBoard();
        for (ChessPosition pos : piecePositions(board)) {
            ChessPiece piece = board.getPiece(pos);
            if (piece != null) {
                newBoard.addPiece(pos, new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
            }
        }
        return newBoard;
    }

    public boolean checkBounds(ChessPosition position) {
        return position.getColumn() >= 1 && position.getColumn() <= 8 && position.getRow() >= 1 && position.getRow() <= 8;
    }
}
