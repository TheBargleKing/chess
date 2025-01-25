package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor team = board.getPiece(myPosition).getTeamColor();
        PieceType type = board.getPiece(myPosition).getPieceType();
        switch (type) {
            case PAWN -> {
                if (team == ChessGame.TeamColor.WHITE) {
                    ChessPosition endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    if (board.getPiece(endPosition) == null && legalLocation(endPosition)) {
                        moves.add(new ChessMove(myPosition, endPosition, null));
                        if (myPosition.getRow() == 2) {
                            endPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                            if (board.getPiece(endPosition) == null && legalLocation(endPosition)) {
                                moves.add(new ChessMove(myPosition, endPosition, null));
                            }
                        }
                    }
                } else {
                    ChessPosition endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (board.getPiece(endPosition) == null && legalLocation(endPosition)) {
                        moves.add(new ChessMove(myPosition, endPosition, null));
                        if (myPosition.getRow() == 2) {
                            endPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                            if (board.getPiece(endPosition) == null && legalLocation(endPosition)) {
                                moves.add(new ChessMove(myPosition, endPosition, null));
                            }
                        }
                    }

                }
            }
        }
//            case PAWN -> {
//                ChessPosition endPosition;
//                if (team == ChessGame.TeamColor.WHITE) {
//                    // Move 1 space upward
//                    endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
//                    if (board.getPiece(endPosition) == null && legalLocation(endPosition)) {
//                        moves.add(new ChessMove(myPosition, endPosition, null));
//                        // 2 spaces if first move
//                        if (myPosition.getRow() == 1) {
//                            endPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
//                            if (board.getPiece(endPosition) == null && legalLocation(endPosition)) {
//                                moves.add(new ChessMove(myPosition, endPosition, null));
//                            }
//                        }
//                    }
//
//                } else { // If one the BLACK team
//                    endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
//                    if (board.getPiece(endPosition) == null && legalLocation(endPosition)) {
//                        moves.add(new ChessMove(myPosition, endPosition, null));
//                        // 2 spaces if first move
//                        if (myPosition.getRow() == 6) {
//                            endPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
//                            if (board.getPiece(endPosition) == null && legalLocation(endPosition)) {
//                                moves.add(new ChessMove(myPosition, endPosition, null));
//                            }
//                        }
//                    }
//                }
//                return moves;
//            }
//            case KNIGHT -> {
//                // All possible move offsets for a knight
//                int[][] moveOffsets = {
//                        {2, 1}, {2, -1}, {1, 2}, {1, -1}, {-1, 2}, {-1, -1}, {-2, 1}, {-2, -1}
//                };
//                // Testing and adding to moves
//                for (int[] offset : moveOffsets) {
//                    int newRow = myPosition.getRow() + offset[0];
//                    int newCol = myPosition.getColumn() + offset[1];
//                    ChessPosition endPosition = new ChessPosition(newRow, newCol);
//
//                    // Check if the position is valid and the square is empty
//                    if (board.getPiece(endPosition) == null  && legalLocation(endPosition)) {
//                        moves.add(new ChessMove(myPosition, endPosition, null));
//                    }
//                }
//                return moves;
//            }
//        }
//        return moves;
        return moves;
    }
    public boolean legalLocation(ChessPosition position) {
        return position.getRow() >= 0 && position.getRow() <= 7 && position.getColumn() >= 0 && position.getColumn() <= 7;
    }
}
