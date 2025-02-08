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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = pieceColor.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

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
        switch (type) {
            case KING:
                moveInDirectionsHelper(moves, board, myPosition, new int[][]{{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}}, 1);
                break;

            case QUEEN:
                moveInDirectionsHelper(moves, board, myPosition, new int[][]{{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}}, 8);
                break;

            case ROOK:
                moveInDirectionsHelper(moves, board, myPosition, new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}}, 8);
                break;

            case BISHOP:
                moveInDirectionsHelper(moves, board, myPosition, new int[][]{{1, 1}, {-1, 1}, {-1, -1}, {1, -1}}, 8);
                break;

            case KNIGHT:
                moveInDirectionsHelper(moves, board, myPosition, new int[][]{{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}}, 1);
                break;

            case PAWN:
                movePawnHelper(moves, board, myPosition);
                break;
        }
        return moves;
    }

    private void moveInDirectionsHelper(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition, int[][] directions, int maxSteps) {
        for (int[] direction : directions) {
            ChessPosition end = new ChessPosition(myPosition.getRow() + direction[0], myPosition.getColumn() + direction[1]);
            int steps = 0;

            // Checking bounds, and not moving the piece more than possible.
            while (checkBounds(end) && steps < maxSteps) {
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece == null) {
                    moves.add(new ChessMove(myPosition, end, null));
                } else {
                    if (endPiece.getTeamColor() != pieceColor) {
                        moves.add(new  ChessMove(myPosition, end, null));
                    }
                    break;
                }

                steps += 1;

                end = new ChessPosition(end.getRow() + direction[0], end.getColumn() + direction[1]);
            }
        }
    }

    private void movePawnHelper(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int forward = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        ChessPosition end = new ChessPosition(myPosition.getRow() + forward, myPosition.getColumn());
        if (checkBounds(end) && board.getPiece(end) == null)  {
            checkPromotionMoves(moves, myPosition, end);
            if ((pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) || (pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)) {
                end = new ChessPosition(myPosition.getRow() + 2 * forward, myPosition.getColumn());
                if (checkBounds(end) && board.getPiece(end) == null) {
                    moves.add(new ChessMove(myPosition, end, null));
                }
            }
        }
        // Checking the attack on both sides.
        int[] captureColumns = {myPosition.getColumn() - 1, myPosition.getColumn() + 1};
        for (int column : captureColumns) {
            end = new ChessPosition(myPosition.getRow() + forward, column);
            if (checkBounds(end)) {
                ChessPiece pieceAtEnd = board.getPiece(end);
                if (pieceAtEnd != null && pieceAtEnd.getTeamColor() != pieceColor) {
                    checkPromotionMoves(moves, myPosition, end);
                }
            }
        }
    }

    // Adding four moves if pawn is at the end of the board.
    private void checkPromotionMoves(Collection<ChessMove> moves, ChessPosition start, ChessPosition end) {
        if ((pieceColor == ChessGame.TeamColor.WHITE && end.getRow() == 8) || (pieceColor == ChessGame.TeamColor.BLACK && end.getRow() == 1)) {
            moves.add(new ChessMove(start, end, PieceType.QUEEN));
            moves.add(new ChessMove(start, end, PieceType.KNIGHT));
            moves.add(new ChessMove(start, end, PieceType.BISHOP));
            moves.add(new ChessMove(start, end, PieceType.ROOK));
        } else {
            moves.add(new ChessMove(start, end, null));
        }
    }

    private boolean checkBounds(ChessPosition position) {
        return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() > 0 && position.getColumn() <= 8;
    }
}
